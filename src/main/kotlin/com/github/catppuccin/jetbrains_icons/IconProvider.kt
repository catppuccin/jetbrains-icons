package com.github.catppuccin.jetbrains_icons

import com.github.catppuccin.jetbrains_icons.settings.PluginSettingsState
import com.intellij.ide.IconProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiUtilCore
import javax.swing.Icon

class IconProvider : IconProvider() {
    private var icons = Icons(PluginSettingsState.instance.variant)

    override fun getIcon(element: PsiElement, flags: Int): Icon {
        val file = PsiUtilCore.getVirtualFile(element)

        // Folders
        if (file?.isDirectory == true) {
            return icons.FOLDER_TO_ICONS[file.name.lowercase()] ?: icons.folder
        }

        // Files
        val icon = icons.FILE_TO_ICONS[file?.name?.lowercase()]
        if (icon != null) {
            return icon
        }

        // Extensions
        // if the file is abc.test.tsx, try abc.test.tsx, then test.tsx, then tsx
        val parts = file?.name?.split(".")
        if (parts != null) {
            for (i in parts.indices) {
                val path = parts.subList(i, parts.size).joinToString(".")
                val icon = icons.EXT_TO_ICONS[path]
                if (icon != null) {
                    return icon
                }
            }
        }

        if (file?.fileType?.isBinary == true) {
            return icons.binary
        }

        return icons.file
    }
}
