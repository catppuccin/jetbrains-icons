import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
  kotlin("jvm")
}

group = "com.github.catppuccin.jetbrains_icons"
version = "1.10.0"

val platformType: String by project
val platformVersion: String by project

dependencies {
  intellijPlatform {
    create(platformType, platformVersion, useInstaller = false)
    instrumentationTools()

    pluginVerifier()
    zipSigner()
    testFramework(TestFrameworkType.Platform)
    testFramework(TestFrameworkType.Plugin.Java)
  }

  //Test dependencies
  testImplementation(platform("org.junit:junit-bom:5.11.3"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher") {
    because("Only needed to run tests in a version of IntelliJ IDEA that bundles older versions")
  }
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
  testRuntimeOnly("org.junit.vintage:junit-vintage-engine")
}

tasks {
  verifyPlugin {
    enabled = false
  }

  publishPlugin {
    enabled = false
  }

  signPlugin {
    enabled = true
  }

  runIde {
    enabled = false
  }
}

kotlin {
  jvmToolchain(17)
}
