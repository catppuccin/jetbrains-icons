package com.github.catppuccin.jetbrains_icons

import com.github.catppuccin.jetbrains_icons.IconPack.icons
import com.intellij.ide.IconProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiUtilCore
import javax.swing.Icon

/**
 * Provides icons for all file types unless a class under `providers/` handles something more
 * specific.
 */
class IconProvider : IconProvider() {
  /**
   * Overrides of filenames to icons. If the filename matches (case-insensitive), then return this
   * icon.
   *
   * All keys should be lowercase so file names can be matched.
   */
  private val iconOverrides = mapOf("dockerfile" to icons.docker)

  /** File extensions that are handled by more specific providers (not this class). */
  private val fileTypesByProviders = listOf(".java")

  override fun getIcon(element: PsiElement, flags: Int): Icon? {
    val virtualFile = PsiUtilCore.getVirtualFile(element)
    val file = virtualFile?.let { PsiManager.getInstance(element.project).findFile(it) }

    // Some icons are handled by other providers, so return null here if the file ends in any of
    // them.
    if (fileTypesByProviders.any { file?.name?.endsWith(it) == true }) {
      return null
    }

    // Check if the name of the file is overridden by anything, if so return that icon.
    if (iconOverrides.containsKey(file?.fileType?.name?.lowercase())) {
      return iconOverrides[file?.fileType?.name?.lowercase()]
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
