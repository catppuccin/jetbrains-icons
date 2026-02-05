package com.github.com.catppuccin.jetbrains_icons.util

import com.github.catppuccin.jetbrains_icons.util.PsiClassUtils
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase5
import com.intellij.testFramework.runInEdtAndGet
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PsiClassUtilsTest : LightJavaCodeInsightFixtureTestCase5() {
  override fun getTestDataPath(): String = "src/test/testData"

  @Test
  fun isAbstract_abstractClass_returnsTrue() {
    fixture.addFileToProject(
      "SomeClass.java",
      """
      abstract class SomeClass { }
      """
        .trimIndent(),
    )

    val isAbstract = runInEdtAndGet { PsiClassUtils.isAbstract(fixture.findClass("SomeClass")) }

    assertTrue(isAbstract)
  }

  @Test
  fun isAbstract_regularClass_returnsFalse() {
    fixture.addFileToProject(
      "SomeClass.java",
      """
      class SomeClass { }
      """
        .trimIndent(),
    )

    val isAbstract = runInEdtAndGet { PsiClassUtils.isAbstract(fixture.findClass("SomeClass")) }

    assertFalse(isAbstract)
  }

  @Test
  fun isSealed_sealedClass_returnsTrue() {
    fixture.addFileToProject(
      "SomeClass.java",
      """
      sealed class SomeClass { }
      """
        .trimIndent(),
    )

    val isSealed = runInEdtAndGet { PsiClassUtils.isSealed(fixture.findClass("SomeClass")) }

    assertTrue(isSealed)
  }

  @Test
  fun isSealed_regularClass_returnsFalse() {
    fixture.addFileToProject(
      "SomeClass.java",
      """
      class SomeClass { }
      """
        .trimIndent(),
    )

    val isSealed = runInEdtAndGet { PsiClassUtils.isSealed(fixture.findClass("SomeClass")) }

    assertFalse(isSealed)
  }

  @Test
  fun isFinal_finalClass_returnsTrue() {
    fixture.addFileToProject(
      "SomeClass.java",
      """
      final class SomeClass { }
      """
        .trimIndent(),
    )

    val isFinal = runInEdtAndGet { PsiClassUtils.isFinal(fixture.findClass("SomeClass")) }

    assertTrue(isFinal)
  }

  @Test
  fun isFinal_regularClass_returnsFalse() {
    fixture.addFileToProject(
      "SomeClass.java",
      """
      class SomeClass { }
      """
        .trimIndent(),
    )

    val isFinal = runInEdtAndGet { PsiClassUtils.isFinal(fixture.findClass("SomeClass")) }

    assertFalse(isFinal)
  }

  @Test
  fun isStatic_staticClass_returnsTrue() {
    fixture.addFileToProject(
      "SomeClass.java",
      """
      final static class SomeClass { }
      """
        .trimIndent(),
    )

    val isStatic = runInEdtAndGet { PsiClassUtils.isStatic(fixture.findClass("SomeClass")) }

    assertTrue(isStatic)
  }

  @Test
  fun isStatic_regularClass_returnsFalse() {
    fixture.addFileToProject(
      "SomeClass.java",
      """
      class SomeClass { }
      """
        .trimIndent(),
    )

    val isStatic = runInEdtAndGet { PsiClassUtils.isStatic(fixture.findClass("SomeClass")) }

    assertFalse(isStatic)
  }

  @Test
  fun isPackagePrivate_packagePrivateClass_returnsTrue() {
    fixture.addFileToProject(
      "SomeClass.java",
      """
      class SomeClass { }
      """
        .trimIndent(),
    )

    val isPackagePrivate = runInEdtAndGet {
      PsiClassUtils.isPackagePrivate(fixture.findClass("SomeClass"))
    }

    assertTrue(isPackagePrivate)
  }

  @Test
  fun isPackagePrivate_publicClass_returnsFalse() {
    fixture.addFileToProject(
      "SomeClass.java",
      """
      public class SomeClass { }
      """
        .trimIndent(),
    )

    val isPackagePrivate = runInEdtAndGet {
      PsiClassUtils.isPackagePrivate(fixture.findClass("SomeClass"))
    }

    assertFalse(isPackagePrivate)
  }

  @Test
  fun isException_extendsException_returnsTrue() {
    fixture.addFileToProject(
      "SomeClass.java",
      """
      class SomeClass extends Exception { }
      """
        .trimIndent(),
    )

    val isException = runInEdtAndGet { PsiClassUtils.isException(fixture.findClass("SomeClass")) }

    assertTrue(isException)
  }

  @Test
  fun isException_extendsThrowable_returnsTrue() {
    fixture.addFileToProject(
      "SomeClass.java",
      """
      class SomeClass extends Throwable { }
      """
        .trimIndent(),
    )

    val isException = runInEdtAndGet { PsiClassUtils.isException(fixture.findClass("SomeClass")) }

    assertTrue(isException)
  }

  @Test
  fun isException_inheritance_returnsTrue() {
    fixture.addFileToProject(
      "OtherClass.java",
      """
      class OtherClass extends Exception { }
      """
        .trimIndent(),
    )
    fixture.addFileToProject(
      "SomeClass.java",
      """
      class SomeClass extends OtherClass { }
      """
        .trimIndent(),
    )

    val isException = runInEdtAndGet { PsiClassUtils.isException(fixture.findClass("SomeClass")) }

    assertTrue(isException)
  }
}
