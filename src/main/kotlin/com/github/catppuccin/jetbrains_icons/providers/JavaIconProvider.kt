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

class JavaIconProvider : IconProvider() {
  override fun getIcon(@NotNull element: PsiElement, @IconFlags flags: Int): Icon? {
    if (!PluginSettingsState.instance.javaSupport) return icons.java

    if (element !is PsiClass) return null

    val virtualFile = PsiUtilCore.getVirtualFile(element) ?: return null
    if (!virtualFile.name.endsWith(".java")) return null

    val baseIcon = getJavaIcon(element)
    val staticMark = getStaticMark(element)

    val icon =
      when {
        staticMark != null -> {
          LayeredIcon(2).apply {
            setIcon(baseIcon, 0)
            setIcon(staticMark, 1)
          }
        }
        else -> baseIcon
      }

    val visibilityIconsEnabled =
      ProjectView.getInstance(element.project)?.isShowVisibilityIcons("ProjectPane") == true

    return when {
      visibilityIconsEnabled -> {
        RowIcon(2).apply {
          setIcon(icon, 0)
          getVisibilityIcon(element)?.let { setIcon(it, 1) }
        }
      }
      else -> icon
    }
  }

  private fun getStaticMark(element: PsiClass): Icon? =
    if (PsiClassUtils.isStatic(element)) AllIcons.Nodes.StaticMark else null

  private fun getVisibilityIcon(psiElement: PsiClass): Icon? =
    when {
      psiElement.hasModifierProperty(PsiModifier.PUBLIC) -> AllIcons.Nodes.Public
      psiElement.hasModifierProperty(PsiModifier.PRIVATE) -> AllIcons.Nodes.Private
      psiElement.hasModifierProperty(PsiModifier.PROTECTED) -> AllIcons.Nodes.Protected
      PsiClassUtils.isPackagePrivate(psiElement) -> AllIcons.Nodes.PackageLocal
      else -> null
    }

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
