plugins {
  // Lets Gradle download the JDK 21 the `test` task's toolchain asks for when none is installed.
  id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "Catppuccin Icons"
