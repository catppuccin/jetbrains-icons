package com.github.catppuccin.jetbrains_icons

import com.intellij.ide.IconProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiUtilCore
import javax.swing.Icon

class IconProvider : IconProvider() {
    private var icons = IconPack.instance.icons

    private val fileTypes = mapOf(
        "Dockerfile" to icons.docker,
    )

    override fun getIcon(element: PsiElement, flags: Int): Icon? {
        val virtualFile = PsiUtilCore.getVirtualFile(element)

        // File types
        if (virtualFile != null) {
            val file = PsiManager.getInstance(element.project).findFile(virtualFile)
            if (file != null) {
                val fileType = when {
                   file.name.endsWith(".java") -> {
                       return null
                   }
                    else -> {
                        file.fileType.name
                    }
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

}
