package com.github.catppuccin.jetbrains_icons

import com.github.catppuccin.jetbrains_icons.IconPack.icons
import com.github.catppuccin.jetbrains_icons.settings.PluginSettingsState
import com.intellij.ide.IconProvider
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiUtilCore
import javax.swing.Icon

/**
 * Provides icons for all file types unless a class under `providers/` handles something more
 * specific.
 */
class CatppuccinIconProvider : IconProvider() {
  /**
   * Overrides of filenames to icons. If the filename matches (case-insensitive), then return this
   * icon.
   *
   * All keys should be lowercase so file names can be matched.
   */
  private val iconOverrides = mapOf("dockerfile" to icons.docker)

  /** File extensions that are handled by more specific providers (not this class). */
  private val fileTypesByProviders = listOf(".java")

  /**
   * Returns an icon for the given [PsiElement].
   *
   * @param element the [PsiElement] to get an icon for.
   * @param flags additional flags for icon retrieval (not used in this implementation). It is only
   *   used because the method signature requires it.
   * @return the [Icon] corresponding to the file type
   */
  override fun getIcon(element: PsiElement, flags: Int): Icon? {
    val virtualFile = PsiUtilCore.getVirtualFile(element) ?: return null

    // Some icons are handled by other providers, so return null here if the file ends in any of
    // them.
    if (fileTypesByProviders.any { virtualFile.name.endsWith(it) }) {
      return null
    }

    if (!virtualFile.isDirectory && isIgnoredFile(virtualFile.name)) {
      return null
    }

    return findIcon(virtualFile)
  }

  private fun isIgnoredFile(fileName: String): Boolean {
    return PluginSettingsState.instance.ignoredFileMatchers.any {
      it.acceptsCharSequence(fileName)
    }
  }

  /**
   * Finds an appropriate icon for the given virtual file.
   *
   * @param virtualFile the [VirtualFile] associated with the element.
   * @return the icon for the file, or a default icon if no specific icon is found.
   */
  private fun findIcon(virtualFile: VirtualFile): Icon {
    val fileTypeName = virtualFile.fileType.name.lowercase()

    return iconOverrides[fileTypeName]
      ?: when {
        // Check if the name of the file is overridden by anything, if so return that icon.
        virtualFile.isDirectory ->
          icons.FOLDER_TO_ICONS[virtualFile.name.lowercase()] ?: icons._folder

        else -> findFileIcon(virtualFile) ?: icons._file
      }
  }

  /**
   * Finds an icon specifically for a file (not a directory).
   *
   * @param virtualFile the [VirtualFile] to find an icon for.
   * @return the icon for the file, or null if no specific icon is found.
   */
  private fun findFileIcon(virtualFile: VirtualFile): Icon? {
    val fileName = virtualFile.name.lowercase()

    // Files
    return icons.FILE_TO_ICONS[fileName]
      ?: findExtensionIcon(fileName)
      ?: if (virtualFile.fileType.isBinary) icons.binary else null
  }

  /**
   * Finds an icon based on the file extension.
   *
   * @param fileName the name of the file to find an icon for.
   * @return the icon for the file extension, or null if no matching icon is found.
   */
  private fun findExtensionIcon(fileName: String): Icon? {
    // Extensions
    // if the file is abc.test.tsx, try abc.test.tsx, then test.tsx, then tsx
    val parts = fileName.split(".")
    for (i in parts.indices) {
      val path = parts.subList(i, parts.size).joinToString(".")
      // Return the first matching icon we find, starting from the longest possible extension
      icons.EXT_TO_ICONS[path]?.let {
        return it
      }
    }
    // No matching extension was found, so return null, falling back to default icon
    return null
  }
}
