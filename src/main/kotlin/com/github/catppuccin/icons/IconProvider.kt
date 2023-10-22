package com.github.catppuccin.icons

import com.intellij.ide.IconProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiUtilCore
import javax.swing.Icon

class IconProvider : IconProvider() {
    override fun getIcon(element: PsiElement, flags: Int): Icon? {
        val file = PsiUtilCore.getVirtualFile(element)

        // Folders
        if (file?.isDirectory == true) {
            return Icons.FOLDER_TO_ICONS[file.name] ?: Icons.FOLDER
        }

        // Files
        val icon = Icons.FILE_TO_ICONS[file?.name]
        if (icon != null) {
            return icon
        }

        // Extensions
        // if the file is abc.test.tsx, try abc.test.tsx, then test.tsx, then tsx
        val parts = file?.name?.split(".")
        if (parts != null) {
            for (i in parts.indices) {
                val path = parts.subList(i, parts.size).joinToString(".")
                val icon = Icons.EXT_TO_ICONS[path]
                if (icon != null) {
                    return icon
                }
            }
        }

        if (file?.fileType?.isBinary == true) {
            return Icons.BINARY
        }

        return Icons.FILE
    }
}
