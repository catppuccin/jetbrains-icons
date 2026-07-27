import com.ncorti.ktfmt.gradle.tasks.KtfmtCheckTask
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.date
import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.intellij.platform.gradle.models.ProductRelease
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun properties(key: String) = project.findProperty(key).toString()

plugins {
  // Java support
  id("java")
  // Kotlin support
  id("org.jetbrains.kotlin.jvm") version "2.4.10"
  // Gradle IntelliJ Plugin
  id("org.jetbrains.intellij.platform") version "2.18.1"
  // Gradle Changelog Plugin
  id("org.jetbrains.changelog") version "2.5.0"

  // Code Quality
  // ktfmt
  id("com.ncorti.ktfmt.gradle") version "0.25.0"
  // detekt
  id("io.gitlab.arturbosch.detekt").version("1.23.8")

  // Kotlin Serialization
  id("org.jetbrains.kotlin.plugin.serialization") version "2.4.10"
}

group = properties("pluginGroup")

version = properties("pluginVersion")

repositories {
  mavenCentral()
  intellijPlatform { defaultRepositories() }
}

dependencies {
  intellijPlatform {
    create(providers.gradleProperty("platformType"), providers.gradleProperty("platformVersion"))
    bundledPlugins(providers.gradleProperty("platformPlugins").map { it.split(',') })
    pluginVerifier()
    testFramework(TestFrameworkType.Platform)
    testFramework(TestFrameworkType.Plugin.Java)
  }

  testImplementation(platform("org.junit:junit-bom:6.1.2"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher") {
    because("Only needed to run tests in a version of IntelliJ IDEA that bundles older versions")
  }
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
  testRuntimeOnly("org.junit.vintage:junit-vintage-engine")

  // kotlinx-serialization for JSONC
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
}

// kotlinx-serialization drags in kotlin-stdlib (and the ancient org.jetbrains:annotations 13.0 that
// stdlib depends on) transitively, which lands them in the plugin distribution's lib/ directory and
// shadows the copies the IntelliJ Platform already ships. `kotlin.stdlib.default.dependency=false`
// in gradle.properties only stops the Kotlin plugin from adding stdlib directly, not transitively.
// The runtime classpath is what the distribution is assembled from, so exclude them there; the
// compile classpath is left alone, since it takes both from the IntelliJ Platform dependency.
configurations.runtimeClasspath {
  exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib")
  exclude(group = "org.jetbrains", module = "annotations")
}

intellijPlatform {
  pluginConfiguration {
    id.set(properties("pluginGroup"))
    name.set(properties("pluginName"))
    version.set(properties("pluginVersion"))

    changelog {
      version.set(properties("pluginVersion"))
      path.set(file("CHANGELOG.md").canonicalPath)
      header.set(provider { "${version.get()} - ${date()}" })
      headerParserRegex.set("""(\d\.\d+\.\d+)""".toRegex())
      itemPrefix.set("-")
      keepUnreleasedSection.set(true)
      unreleasedTerm.set("[Unreleased]")
      groups.set(listOf("Added", "Changed", "Deprecated", "Removed", "Fixed", "Security"))
    }
  }

  pluginVerification {
    ides {
      // Covers the IntelliJ IDEA Community releases in range, which stop at 2025.2: the separate
      // Community distribution is no longer published for 2025.3 and later.
      recommended()

      // Without this, verification silently stops at 2025.2 while the plugin advertises support up
      // to `pluginUntilBuild`, because recommended() has no Community releases left to offer. The
      // unified IntelliJ IDEA distribution replaces the Community one from 2025.3 (253) onwards.
      select {
        types = listOf(IntelliJPlatformType.IntellijIdea)
        channels = listOf(ProductRelease.Channel.RELEASE)
        sinceBuild = "253"
        untilBuild = properties("pluginUntilBuild")
      }
    }
  }
}

intellijPlatformTesting {
  runIde {
    register("runLatestIde") {
      // The separate Community (IC) distribution is no longer published since 2025.3;
      // the unified IntelliJ IDEA distribution replaces it.
      type = IntelliJPlatformType.IntellijIdea
      version = "2026.1.4"
      task { jvmArgs("-Xmx2048m") }
    }
  }
}

tasks {
  // Set the JVM compatibility versions
  properties("javaVersion").let {
    withType<JavaCompile> {
      sourceCompatibility = it
      targetCompatibility = it
    }
    withType<KotlinCompile> {
      compilerOptions {
        // Must not exceed the Kotlin stdlib bundled with the IDE at pluginSinceBuild, since the
        // plugin does not bundle its own stdlib (see the runtimeClasspath exclusions above) and
        // resolves against the IDE's. 2025.2 (252) bundles stdlib 2.2.0, so this matches exactly.
        // Raising it requires raising pluginSinceBuild to an IDE that bundles that stdlib.
        apiVersion = KotlinVersion.KOTLIN_2_2
        jvmTarget = JvmTarget.fromTarget(properties("javaVersion"))
      }
    }
  }

  wrapper { gradleVersion = "9.6.1" }

  // Keep the sandbox heap above the 750MB threshold of MemorySizeConfigurator, which otherwise
  // fails with an IOException trying to write vmoptions the sandbox does not have.
  runIde { jvmArgs("-Xmx2048m") }

  patchPluginXml {
    pluginVersion.set(properties("pluginVersion"))
    sinceBuild.set(properties("pluginSinceBuild"))
    untilBuild.set(properties("pluginUntilBuild"))

    // Get the latest available change notes from the changelog file
    changeNotes.set(
      provider { changelog.renderItem(changelog.getLatest(), Changelog.OutputType.HTML) }
    )
  }

  test {
    useJUnitPlatform()
    testLogging { events("passed", "skipped", "failed") }

    // Run the tests on a stock JDK instead of the JetBrains Runtime the platform would otherwise
    // supply. JBR starts a "SystemPropertyWatcher" thread (sun.awt.UNIXToolkit) that the test
    // framework's leak detector does not recognise, so every run fails on a leaked thread that has
    // nothing to do with the plugin. Suppressing it instead would mean calling ThreadLeakTracker,
    // which is @ApiStatus.Internal. These tests cover PSI and icon resolution rather than real UI
    // rendering, so a stock JDK of the same major version as the bundled JBR is close enough.
    javaLauncher =
      project.the<JavaToolchainService>().launcherFor {
        languageVersion = JavaLanguageVersion.of(21)
      }
  }

  buildPlugin { dependsOn(test) }

  signPlugin {
    certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
    privateKey.set(System.getenv("PRIVATE_KEY"))
    password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
  }

  publishPlugin {
    dependsOn("patchChangelog")
    token.set(System.getenv("PUBLISH_TOKEN"))
    channels.set(listOf("default"))
  }
}

tasks.buildSearchableOptions { enabled = false }

// Code quality settings

// Formatting settings
ktfmt { googleStyle() }

// This is used over the "ktfmtCheck" task to exclude the autogenerated file Icons.kt.
tasks.register<KtfmtCheckTask>("checkFormatting") {
  source = project.fileTree(rootDir)
  include("**/*.kt")
  exclude("src/main/kotlin/com/github/catppuccin/jetbrains_icons/Icons.kt")
}

// Static Analysis config
detekt {
  parallel = true
  config.setFrom("detekt.yaml")
  buildUponDefaultConfig = true
}

// This is used over the "detekt" task to exclude the autogenerated file Icons.kt.
tasks.withType<Detekt>().configureEach {
  source = project.fileTree(rootDir)
  include("**/*.kt")
  exclude("src/main/kotlin/com/github/catppuccin/jetbrains_icons/Icons.kt")
}

tasks.register<Detekt>("runStaticAnalysis") {
  parallel = true
  config.setFrom("detekt.yaml")
  buildUponDefaultConfig = true

  source = project.fileTree(rootDir)
  include("**/*.kt")
  exclude("src/main/kotlin/com/github/catppuccin/jetbrains_icons/Icons.kt")
}
