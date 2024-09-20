package com.github.catppuccin.jetbrains_icons.providers

import com.github.catppuccin.jetbrains_icons.IconPack
import com.github.catppuccin.jetbrains_icons.settings.PluginSettingsState
import com.github.catppuccin.jetbrains_icons.util.PsiClassUtils
import com.intellij.ide.IconProvider
import com.intellij.psi.*
import com.intellij.psi.util.PsiUtilCore
import javax.swing.Icon

class JavaIconProvider : IconProvider() {
    private val icons = IconPack.instance.icons

    override fun getIcon(element: PsiElement, flags: Int): Icon? {
        if (!PluginSettingsState.instance.javaSupport) return icons.java

        if (element !is PsiClass) return null

        val virtualFile = PsiUtilCore.getVirtualFile(element) ?: return null
        if (!virtualFile.name.endsWith(".java")) return null

        return getJavaIcon(element)
    }

    private fun getJavaIcon(aClass: PsiClass): Icon = when {
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
