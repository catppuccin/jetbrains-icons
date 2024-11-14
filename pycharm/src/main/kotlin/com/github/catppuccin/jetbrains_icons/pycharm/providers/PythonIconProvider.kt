package com.github.catppuccin.jetbrains_icons.pycharm.providers

import com.github.catppuccin.jetbrains_icons.commons.IconPack.icons
import com.github.catppuccin.jetbrains_icons.commons.settings.PluginSettingsState
import com.intellij.ide.IconProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiUtilCore
import javax.swing.Icon

/** Provides icons for Python files. If the IDE has the PythonCore plugin installed, our symbol gets override
 *  by the PythonCore plugin's icon. Therefore we provide an override here
 */
class PythonIconProvider : IconProvider() {

  /**
   * Returns an icon for the given [PsiElement] if it's a Python file.
   *
   * @param element The [PsiElement] to get an icon for.
   * @param flags Additional flags for icon retrieval. We don't use this, but it's required by the interface.
   * @return The icon for the element, or null if no suitable icon is found.
   */
  override fun getIcon(element: PsiElement, flags: Int): Icon? {
    println("PythonIconProvider: getIcon")
    return when {
      !PluginSettingsState.instance.pythonSupport -> null
      PsiUtilCore.getVirtualFile(element)?.name?.endsWith(".py") != true -> null
      else -> icons.python
    }
  }
}
