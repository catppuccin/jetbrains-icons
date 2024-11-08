package com.github.com.catppuccin.jetbrains_icons

import com.github.catppuccin.jetbrains_icons.IconProvider
import com.github.catppuccin.jetbrains_icons.Icons
import com.intellij.psi.PsiElement
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase5
import com.intellij.testFramework.runInEdtAndGet
import javax.swing.Icon
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class IconProviderTest : LightJavaCodeInsightFixtureTestCase5() {
  override fun getTestDataPath(): String = "src/test/testData"

  private val icons = Icons("mocha")

  @Test
  fun getIcon_binaryFile() {
    assertIconProvided(expected = icons.binary) {
      fixture.addFileToProject("image.bin", String(ByteArray(10)))
    }
  }

  @Test
  fun getIcon_dockerFile() {
    assertIconProvided(expected = icons.docker) { fixture.addFileToProject("Dockerfile", "") }
  }

  @Test
  fun getIcon_directory() {
    assertIconProvided(expected = icons.folder_src) {
      fixture.addFileToProject("foobar", "").containingDirectory
    }
  }

  /** Checks that the [expected] icon is returned by the provider for the given [element]. */
  private fun assertIconProvided(expected: Icon, element: () -> PsiElement) {
    val provider = IconProvider()
    val icon = runInEdtAndGet { provider.getIcon(element(), 1) }

    assertEquals(expected, icon)
  }
}
