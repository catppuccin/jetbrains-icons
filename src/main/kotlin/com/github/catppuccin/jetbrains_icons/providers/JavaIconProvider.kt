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
    private val javaIcons = mapOf(
        "JAVA_INTERFACE" to icons.java_interface,
        "JAVA_ENUM" to icons.java_enum,
        "JAVA_ANNOTATION" to icons.java_annotation,
        "JAVA_RECORD" to icons.java_record,
        "JAVA_EXCEPTION" to icons.java_exception,
        "JAVA_ABSTRACT" to icons.java_class_abstract,
        "JAVA_SEALED" to icons.java_class_sealed,
        "JAVA_FINAL" to icons.java_class_final,
        "JAVA_CLASS" to icons.java_class,
    )

    override fun getIcon(element: PsiElement, flags: Int): Icon? {
        if (!PluginSettingsState.instance.javaSupport) return icons.java

        if (element !is PsiClass) return null

        val virtualFile = PsiUtilCore.getVirtualFile(element) ?: return null
        if (!virtualFile.name.endsWith(".java")) return null

        return javaIcons[determineJavaFileType(element)]
    }

    private fun determineJavaFileType(aClass: PsiClass): String = when {
        aClass.isAnnotationType -> "JAVA_ANNOTATION"
        aClass.isInterface -> "JAVA_INTERFACE"
        aClass.isEnum -> "JAVA_ENUM"
        aClass.isRecord -> "JAVA_RECORD"
        PsiClassUtils.isException(aClass) -> "JAVA_EXCEPTION"
        PsiClassUtils.isSealed(aClass) -> "JAVA_SEALED"
        PsiClassUtils.isFinal(aClass) -> "JAVA_FINAL"
        PsiClassUtils.isAbstract(aClass) -> "JAVA_ABSTRACT"
        else -> "JAVA_CLASS"
    }
}
