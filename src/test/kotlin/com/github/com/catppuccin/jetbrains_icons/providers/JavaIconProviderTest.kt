package com.github.com.catppuccin.jetbrains_icons.providers

import com.github.catppuccin.jetbrains_icons.Icons
import com.github.catppuccin.jetbrains_icons.providers.JavaIconProvider
import com.github.catppuccin.jetbrains_icons.settings.PluginSettingsState
import com.intellij.icons.AllIcons
import com.intellij.ide.projectView.impl.ProjectViewState
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase5
import com.intellij.testFramework.runInEdtAndGet
import com.intellij.ui.LayeredIcon
import com.intellij.ui.RowIcon
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

/**
 * Test class for JavaIconProvider. This class tests various scenarios for icon provision for Java
 * elements.
 */
@Tag("fast")
@Tag("javaIcons")
class JavaIconProviderTest : LightJavaCodeInsightFixtureTestCase5() {

  override fun getTestDataPath(): String = "src/test/testData"

  private val icons = Icons("mocha")
  private val provider = JavaIconProvider()

  @BeforeEach
  fun setUp() {
    PluginSettingsState.instance.javaSupport = true
    ProjectViewState.getInstance(fixture.project).showVisibilityIcons = false
  }

  @Test
  @DisplayName("Test icon provision for a standard Java class")
  fun getIcon_javaClass() {
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

  @Test
  @DisplayName("Test icon provision for a Java interface")
  fun getIcon_javaInterface() {
    fixture.addFileToProject(
      "SomeInterface.java",
      """
      interface SomeInterface { }
      """
        .trimIndent(),
    )

    val icon = runInEdtAndGet { provider.getIcon(fixture.findClass("SomeInterface"), 1) }

    assertEquals(icons.java_interface, icon)
  }

  @Test
  @DisplayName("Test icon provision for a Java enum")
  fun getIcon_javaEnum() {
    fixture.addFileToProject(
      "SomeEnum.java",
      """
      enum SomeEnum { }
      """
        .trimIndent(),
    )

    val icon = runInEdtAndGet { provider.getIcon(fixture.findClass("SomeEnum"), 1) }

    assertEquals(icons.java_enum, icon)
  }

  @Test
  @DisplayName("Test icon provision for a Java annotation")
  fun getIcon_javaAnnotation() {
    fixture.addFileToProject(
      "SomeAnnotation.java",
      """
      @interface SomeAnnotation { }
      """
        .trimIndent(),
    )

    val icon = runInEdtAndGet { provider.getIcon(fixture.findClass("SomeAnnotation"), 1) }

    assertEquals(icons.java_annotation, icon)
  }

  @Test
  @DisplayName("Test icon provision for a Java record")
  fun getIcon_javaRecord() {
    fixture.addFileToProject(
      "SomeRecord.java",
      """
      record SomeRecord() { }
      """
        .trimIndent(),
    )

    val icon = runInEdtAndGet { provider.getIcon(fixture.findClass("SomeRecord"), 1) }

    assertEquals(icons.java_record, icon)
  }

  @Test
  @DisplayName("Test icon provision for a Java exception class")
  fun getIcon_javaException() {
    fixture.addFileToProject(
      "SomeException.java",
      """
      class SomeException extends Exception { }
      """
        .trimIndent(),
    )

    val icon = runInEdtAndGet { provider.getIcon(fixture.findClass("SomeException"), 1) }

    assertEquals(icons.java_exception, icon)
  }

  @Test
  @DisplayName("Test icon provision for a Java sealed class")
  fun getIcon_javaSealedClass() {
    fixture.addFileToProject(
      "SomeSealedClass.java",
      """
      sealed class SomeSealedClass { }
      """
        .trimIndent(),
    )

    val icon = runInEdtAndGet { provider.getIcon(fixture.findClass("SomeSealedClass"), 1) }

    assertEquals(icons.java_class_sealed, icon)
  }

  @Test
  @DisplayName("Test icon provision for a Java final class")
  fun getIcon_javaFinalClass() {
    fixture.addFileToProject(
      "SomeFinalClass.java",
      """
      final class SomeFinalClass { }
      """
        .trimIndent(),
    )

    val icon = runInEdtAndGet { provider.getIcon(fixture.findClass("SomeFinalClass"), 1) }

    assertEquals(icons.java_class_final, icon)
  }

  @Test
  @DisplayName("Test icon provision for a Java abstract class")
  fun getIcon_javaAbstractClass() {
    fixture.addFileToProject(
      "SomeAbstractClass.java",
      """
      abstract class SomeAbstractClass { }
      """
        .trimIndent(),
    )

    val icon = runInEdtAndGet { provider.getIcon(fixture.findClass("SomeAbstractClass"), 1) }

    assertEquals(icons.java_class_abstract, icon)
  }

  @Test
  @DisplayName("Test that a static class icon includes a static mark")
  fun getIcon_staticClass_hasStaticMark() {
    fixture.addFileToProject(
      "StaticClass.java",
      """
      public class OuterClass {
        static class StaticClass { }
      }
      """
        .trimIndent(),
    )

    val icon = runInEdtAndGet { provider.getIcon(fixture.findClass("OuterClass.StaticClass"), 1) }

    // The icon should be a LayeredIcon with 2 layers - base icon and static mark
    assertTrue(icon is LayeredIcon)
    assertEquals(2, (icon as LayeredIcon).allLayers.size)
    assertEquals(AllIcons.Nodes.StaticMark, icon.allLayers[1])
  }

  @Test
  @DisplayName("Test that a non-static class icon does not include a static mark")
  fun getIcon_nonStaticClass_hasNoStaticMark() {
    fixture.addFileToProject(
      "RegularClass.java",
      """
      public class RegularClass { }
      """
        .trimIndent(),
    )

    val icon = runInEdtAndGet { provider.getIcon(fixture.findClass("RegularClass"), 1) }

    // For non-static classes, the icon should not be layered
    assertFalse(icon is LayeredIcon)
  }

  @Test
  @DisplayName("Test that requesting an icon for a non-PsiClass element returns null")
  fun getIcon_notPsiClass_returnsNull() {
    fixture.addFileToProject(
      "SomeClass.java",
      """
      class SomeClass {
        private int someField;
      }
      """
        .trimIndent(),
    )

    val icon = runInEdtAndGet { provider.getIcon(fixture.findClass("SomeClass").fields[0], 1) }

    assertNull(icon)
  }

  @Test
  @DisplayName("Test that requesting an icon for a non-Java file returns null")
  fun getIcon_nonJavaFile_returnsNull() {
    fixture.addFileToProject(
      "SomeClass.kt",
      """
      class SomeClass { }
      """
        .trimIndent(),
    )

    val icon = runInEdtAndGet { provider.getIcon(fixture.findClass("SomeClass"), 1) }

    assertNull(icon)
  }

  @Test
  @DisplayName("Test that when Java support is disabled, a generic Java icon is returned")
  fun getIcon_javaDisabled_returnsJavaIcon() {
    fixture.addFileToProject(
      "SomeClass.java",
      """
      interface SomeClass { }
      """
        .trimIndent(),
    )

    // Temporarily disable Java support
    PluginSettingsState.instance.javaSupport = false

    val icon = runInEdtAndGet { provider.getIcon(fixture.findClass("SomeClass"), 1) }

    assertEquals(icons.java, icon)
  }

  @Test
  @DisplayName("Test visibility icon for a public class")
  fun getVisibilityIconForTesting_publicClass_returnsPublicIcon() {
    fixture.addFileToProject(
      "PublicClass.java",
      """
    public class PublicClass { }
    """
        .trimIndent(),
    )

    ProjectViewState.getInstance(fixture.project).showVisibilityIcons = true

    val icon = runInEdtAndGet { provider.getIcon(fixture.findClass("PublicClass"), 1) }

    assertEquals(AllIcons.Nodes.Public, (icon as RowIcon).getIcon(1))
  }

  @Test
  @DisplayName("Test visibility icon for a private class")
  fun getVisibilityIconForTesting_privateClass_returnsPrivateIcon() {
    fixture.addFileToProject(
      "OuterClass.java",
      """
    public class OuterClass {
      private class PrivateClass { }
    }
    """
        .trimIndent(),
    )

    ProjectViewState.getInstance(fixture.project).showVisibilityIcons = true

    val icon = runInEdtAndGet { provider.getIcon(fixture.findClass("OuterClass.PrivateClass"), 1) }

    assertEquals(AllIcons.Nodes.Private, (icon as RowIcon).getIcon(1))
  }

  @Test
  @DisplayName("Test visibility icon for a protected class")
  fun getVisibilityIconForTesting_protectedClass_returnsProtectedIcon() {
    fixture.addFileToProject(
      "OuterClass.java",
      """
    public class OuterClass {
      protected class ProtectedClass { }
    }
    """
        .trimIndent(),
    )

    ProjectViewState.getInstance(fixture.project).showVisibilityIcons = true

    val icon = runInEdtAndGet {
      provider.getIcon(fixture.findClass("OuterClass.ProtectedClass"), 1)
    }

    assertEquals(AllIcons.Nodes.Protected, (icon as RowIcon).getIcon(1))
  }

  @Test
  @DisplayName("Test visibility icon for a package-private class")
  fun getVisibilityIconForTesting_packagePrivateClass_returnsPackageLocalIcon() {
    fixture.addFileToProject(
      "PackagePrivateClass.java",
      """
    class PackagePrivateClass { }
    """
        .trimIndent(),
    )

    ProjectViewState.getInstance(fixture.project).showVisibilityIcons = true

    val icon = runInEdtAndGet { provider.getIcon(fixture.findClass("PackagePrivateClass"), 1) }

    assertEquals(AllIcons.Nodes.PackageLocal, (icon as RowIcon).getIcon(1))
  }
}
