package com.github.catppuccin.jetbrains_icons.providers

import com.github.catppuccin.jetbrains_icons.IconPack.icons
import com.github.catppuccin.jetbrains_icons.settings.PluginSettingsState
import com.github.catppuccin.jetbrains_icons.util.PsiClassUtils
import com.intellij.icons.AllIcons
import com.intellij.ide.IconProvider
import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.util.Iconable.IconFlags
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiModifier
import com.intellij.psi.util.PsiUtilCore
import com.intellij.ui.LayeredIcon
import com.intellij.ui.RowIcon
import javax.swing.Icon
import org.jetbrains.annotations.NotNull

/** Provides icons for Java classes in the IDE. */
class JavaIconProvider : IconProvider() {
  /**
   * Returns an icon for the given PsiElement if it's a Java class.
   *
   * @param element The PsiElement to get an icon for.
   * @param flags Additional flags for icon retrieval.
   * @return The icon for the element, or null if no suitable icon is found.
   */
  override fun getIcon(@NotNull element: PsiElement, @IconFlags flags: Int): Icon? {
    return when {
      !PluginSettingsState.instance.javaSupport -> icons.java
      element !is PsiClass -> null
      PsiUtilCore.getVirtualFile(element)?.name?.endsWith(".java") != true -> null
      else -> getJavaClassIcon(element)
    }
  }

  /**
   * Gets the appropriate icon for a Java class, including static and visibility markers.
   *
   * @param element The PsiClass to get an icon for.
   * @return The icon for the Java class.
   */
  private fun getJavaClassIcon(element: PsiClass): Icon {
    val baseIcon = getJavaIcon(element)
    val iconWithStaticMark = addStaticMarkIfNeeded(element, baseIcon)
    return addVisibilityIconIfNeeded(element, iconWithStaticMark)
  }

  /**
   * Adds a static mark to the icon if the class is static.
   *
   * @param element The PsiClass to check for static modifier.
   * @param baseIcon The base icon to add the static mark to.
   * @return The icon with static mark added if needed.
   */
  private fun addStaticMarkIfNeeded(element: PsiClass, baseIcon: Icon): Icon {
    val staticMark = getStaticMark(element)
    return if (staticMark != null) {
      LayeredIcon(2).apply {
        setIcon(baseIcon, 0)
        setIcon(staticMark, 1)
      }
    } else {
      baseIcon
    }
  }

  /**
   * Adds a visibility icon to the class icon if visibility icons are enabled in the project view.
   *
   * @param element The PsiClass to get the visibility for.
   * @param icon The icon to add the visibility icon to.
   * @return The icon with visibility icon added if needed.
   */
  private fun addVisibilityIconIfNeeded(element: PsiClass, icon: Icon): Icon {
    val visibilityIconsEnabled =
      ProjectView.getInstance(element.project)?.isShowVisibilityIcons("ProjectPane") == true
    return if (visibilityIconsEnabled) {
      RowIcon(2).apply {
        setIcon(icon, 0)
        getVisibilityIcon(element)?.let { setIcon(it, 1) }
      }
    } else {
      icon
    }
  }

  /**
   * Gets the static mark icon if the class is static.
   *
   * @param element The PsiClass to check for static modifier.
   * @return The static mark icon if the class is static, null otherwise.
   */
  private fun getStaticMark(element: PsiClass): Icon? =
    if (PsiClassUtils.isStatic(element)) AllIcons.Nodes.StaticMark else null

  /**
   * Gets the visibility icon for the given PsiClass.
   *
   * @param psiElement The PsiClass to get the visibility icon for.
   * @return The visibility icon based on the class's modifier, or null if not applicable.
   */
  private fun getVisibilityIcon(psiElement: PsiClass): Icon? =
    when {
      psiElement.hasModifierProperty(PsiModifier.PUBLIC) -> AllIcons.Nodes.Public
      psiElement.hasModifierProperty(PsiModifier.PRIVATE) -> AllIcons.Nodes.Private
      psiElement.hasModifierProperty(PsiModifier.PROTECTED) -> AllIcons.Nodes.Protected
      PsiClassUtils.isPackagePrivate(psiElement) -> AllIcons.Nodes.PackageLocal
      else -> null
    }

  /**
   * Gets the appropriate Java icon based on the class type.
   *
   * @param aClass The PsiClass to get the icon for.
   * @return The icon representing the Java class type.
   */
  private fun getJavaIcon(aClass: PsiClass): Icon =
    when {
      aClass.isAnnotationType -> icons.java_annotation
      aClass.isInterface -> icons.java_interface
      aClass.isEnum -> icons.java_enum
      aClass.isRecord -> icons.java_record
      PsiClassUtils.isException(aClass) -> icons.java_exception
      PsiClassUtils.isSealed(aClass) -> icons.java_class_sealed
      PsiClassUtils.isFinal(aClass) -> icons.java_class_final
      PsiClassUtils.isAbstract(aClass) -> icons.java_class_abstract
      else -> icons.java_class
    }
}
