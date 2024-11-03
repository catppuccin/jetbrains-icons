package com.github.com.catppuccin.jetbrains_icons.providers

import com.github.catppuccin.jetbrains_icons.Icons
import com.github.catppuccin.jetbrains_icons.providers.JavaIconProvider
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase5
import com.intellij.testFramework.runInEdtAndGet
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class JavaIconProviderTest : LightJavaCodeInsightFixtureTestCase5() {

  override fun getTestDataPath(): String = "src/test/testData"

  private val icons = Icons("mocha")

  @Test
  fun getIconForJavaClass() {
    val provider = JavaIconProvider()

    fixture.addFileToProject(
      "SomeClass.java",
      """
      class SomeClass { }
      """
        .trimIndent(),
    )

    val icon = runInEdtAndGet { provider.getIcon(fixture.findClass("SomeClass"), 1) }

    assertEquals(icons.java_class, icon)
  }
}
