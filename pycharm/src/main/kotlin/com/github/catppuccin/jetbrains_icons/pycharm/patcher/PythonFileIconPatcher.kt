package com.github.catppuccin.jetbrains_icons.pycharm.patcher

import com.github.catppuccin.jetbrains_icons.commons.IconPack.icons
import com.github.catppuccin.jetbrains_icons.commons.settings.PluginSettingsState
import com.intellij.ide.FileIconPatcher
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import javax.swing.Icon

class PythonFileIconPatcher : FileIconPatcher {
  override fun patchIcon(
    baseIcon: Icon?,
    file: VirtualFile?,
    flags: Int,
    project: Project?,
  ): Icon? {
    println("PythonFileIconPatcher: patchIcon")
    if (!PluginSettingsState.instance.pythonSupport || file?.extension != "py") {
      println("PythonFileIconPatcher: returning baseIcon")
      return baseIcon
    }
    println("PythonFileIconPatcher: returning icons.python")
    return icons.python
  }

}
