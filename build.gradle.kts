import com.ncorti.ktfmt.gradle.tasks.KtfmtCheckTask
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.date
import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun properties(key: String) = project.findProperty(key).toString()

plugins {
  // Java support
  id("java")
  // Kotlin support
  id("org.jetbrains.kotlin.jvm") version "2.1.20"
  // Gradle IntelliJ Plugin
  id("org.jetbrains.intellij.platform") version "2.5.0"
  // Gradle Changelog Plugin
  id("org.jetbrains.changelog") version "2.2.1"

  // Code Quality
  // ktfmt
  id("com.ncorti.ktfmt.gradle") version "0.22.0"
  //detekt
  id("io.gitlab.arturbosch.detekt").version("1.23.8")
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
    instrumentationTools()
    pluginVerifier()
    testFramework(TestFrameworkType.Platform)
    testFramework(TestFrameworkType.Plugin.Java)
  }

  testImplementation(platform("org.junit:junit-bom:5.12.1"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher") {
    because("Only needed to run tests in a version of IntelliJ IDEA that bundles older versions")
  }
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
  testRuntimeOnly("org.junit.vintage:junit-vintage-engine")
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

  pluginVerification { ides { recommended() } }
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
        apiVersion = KotlinVersion.KOTLIN_1_8
        jvmTarget = JvmTarget.fromTarget(properties("javaVersion"))
      }
    }
  }

  wrapper { gradleVersion = "8.13" }

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
