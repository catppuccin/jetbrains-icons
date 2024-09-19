package com.github.catppuccin.jetbrains_icons.providers

import com.github.catppuccin.jetbrains_icons.IconPack
import com.github.catppuccin.jetbrains_icons.settings.PluginSettingsState
import com.github.catppuccin.jetbrains_icons.util.PsiClassUtils
import com.intellij.ide.IconProvider
import com.intellij.psi.*
import com.intellij.psi.util.PsiUtilCore
import javax.swing.Icon

class JavaIconProvider : IconProvider() {
    private var icons = IconPack.instance.icons

    override fun getIcon(element: PsiElement, flags: Int): Icon? {
        val virtualFile = PsiUtilCore.getVirtualFile(element)
        if (virtualFile != null) {
            if (!virtualFile.name.endsWith(".java")) {
                return null
            }
        }
        // Only process Java files
        val file: PsiFile = (virtualFile?.let { PsiManager.getInstance(element.project).findFile(it) } ?: return null)
        val fileType = determineJavaFileType(file)
        return provideJavaIcons()[fileType]
    }

    private fun determineJavaFileType(file: PsiFile): String {
        if (!PluginSettingsState.instance.javaSupport) return "JAVA_CLASS"
        var fileType = "JAVA_CLASS"
        file.accept(object : JavaRecursiveElementVisitor() {
            override fun visitClass(aClass: PsiClass) {
                when {
                    aClass.isInterface -> fileType = "JAVA_INTERFACE"
                    aClass.isEnum -> fileType = "JAVA_ENUM"
                    aClass.isAnnotationType -> fileType = "JAVA_ANNOTATION"
                    aClass.isRecord -> fileType = "JAVA_RECORD"
                    PsiClassUtils.isException(aClass) -> fileType = "JAVA_EXCEPTION"
                    PsiClassUtils.isSealed(aClass) -> fileType = "JAVA_SEALED"
                    PsiClassUtils.isFinal(aClass) -> fileType = "JAVA_FINAL"
                    PsiClassUtils.isAbstract(aClass) -> fileType = "JAVA_ABSTRACT"
                }
            }
        })
        return fileType
    }

    private fun provideJavaIcons(): Map<String, Icon> {
        return mapOf(
            "JAVA_INTERFACE" to icons.java_interface,
            "JAVA_ENUM" to icons.java_enum,
            "JAVA_ANNOTATION" to icons.java_annotation,
            "JAVA_RECORD" to icons.java_record,
            "JAVA_EXCEPTION" to icons.java_exception,
            "JAVA_ABSTRACT" to icons.java_class_abstract,
            "JAVA_SEALED" to icons.java_class_sealed,
            "JAVA_FINAL" to icons.java_class_final,
            "JAVA_CLASS" to icons.java,
        )
    }
}
