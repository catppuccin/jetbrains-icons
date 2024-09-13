package com.github.catppuccin.jetbrains_icons

import com.github.catppuccin.jetbrains_icons.settings.PluginSettingsState
import com.github.catppuccin.jetbrains_icons.util.PsiClassUtils
import com.intellij.ide.IconProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.JavaRecursiveElementVisitor
import com.intellij.psi.util.PsiUtilCore
import javax.swing.Icon

class IconProvider : IconProvider() {
    private var icons = IconPack.instance.icons

    private val fileTypes = mapOf(
        "Dockerfile" to icons.docker,
    ) + provideJavaIcons()

    override fun getIcon(element: PsiElement, flags: Int): Icon {
        val virtualFile = PsiUtilCore.getVirtualFile(element)

        // File types
        if (virtualFile != null) {
            val file = PsiManager.getInstance(element.project).findFile(virtualFile)
            if (file != null) {
                val fileType = when {
                    file.name.endsWith(".java") -> determineJavaFileType(file)
                    else -> file.fileType.name
                }
                val icon = fileTypes[fileType]
                if (icon != null) {
                    return icon
                }
            }
        }

        // Folders
        if (virtualFile?.isDirectory == true) {
            return icons.FOLDER_TO_ICONS[virtualFile.name.lowercase()] ?: icons._folder
        }

        // Files
        val icon = icons.FILE_TO_ICONS[virtualFile?.name?.lowercase()]
        if (icon != null) {
            return icon
        }

        // Extensions
        // if the file is abc.test.tsx, try abc.test.tsx, then test.tsx, then tsx
        val parts = virtualFile?.name?.split(".")
        if (parts != null) {
            for (i in parts.indices) {
                val path = parts.subList(i, parts.size).joinToString(".")
                val icon = icons.EXT_TO_ICONS[path]
                if (icon != null) {
                    return icon
                }
            }
        }

        if (virtualFile?.fileType?.isBinary == true) {
            return icons.binary
        }

        return icons._file
    }


    private fun determineJavaFileType(file: PsiFile): String {
        var fileType = "JAVA_CLASS"
        if (!PluginSettingsState.instance.javaSupport) return fileType
        file.accept(object : JavaRecursiveElementVisitor() {
            override fun visitClass(aClass: PsiClass) {
                when {
                    aClass.isAnnotationType -> fileType = "JAVA_ANNOTATION"
                    aClass.isInterface -> fileType = "JAVA_INTERFACE"
                    aClass.isEnum -> fileType = "JAVA_ENUM"
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
            "JAVA_ANNOTATION" to icons.java_annotation,
            "JAVA_INTERFACE" to icons.java_interface,
            "JAVA_ENUM" to icons.java_enum,
            "JAVA_RECORD" to icons.java_record,
            "JAVA_EXCEPTION" to icons.java_exception,
            "JAVA_ABSTRACT" to icons.java_class_abstract,
            "JAVA_SEALED" to icons.java_class_sealed,
            "JAVA_FINAL" to icons.java_class_final,
            "JAVA_CLASS" to icons.java_class,
        )
    }
}
