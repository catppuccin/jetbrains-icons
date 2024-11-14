package com.github.catppuccin.jetbrains_icons.pycharm.providers

import com.github.catppuccin.jetbrains_icons.commons.IconPack.icons
import com.github.catppuccin.jetbrains_icons.commons.settings.PluginSettingsState
import com.intellij.ide.FileIconProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import javax.swing.Icon

class PythonFileIconProvider : FileIconProvider {
  override fun getIcon(
    virtualFile: VirtualFile,
    flags: Int,
    project: Project?,
  ): Icon? {
    println("PythonFileIconProvider: getIcon")
    val psiFile = PsiManager.getInstance(project!!).findFile(virtualFile) ?: return null
    return when {
      !PluginSettingsState.instance.pythonSupport -> {
        println("PythonFileIconProvider: disabled, returning null")
        null
      }

      virtualFile.extension != "py" -> {
        println("PythonFileIconProvider: not a Python file, returning null")
        null
      }

      else -> {
        println("PythonFileIconProvider: returning icons.python")
        icons.python
      }
    }
  }


}
