plugins {
    kotlin("jvm")
}

group = "com.github.catppuccin.jetbrains_icons"
version = "1.10.0"

val pycharmType: String by project
val pycharmVersion: String by project

dependencies {
  intellijPlatform {
    create(pycharmType, pycharmVersion, useInstaller = false)
    instrumentationTools()

    pluginVerifier()
    zipSigner()
  }

  implementation(project(":commons"))
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
