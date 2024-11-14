package com.github.catppuccin.jetbrains_icons.decorators

import com.github.catppuccin.jetbrains_icons.commons.IconPack.icons
import com.github.catppuccin.jetbrains_icons.commons.settings.PluginSettingsState
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ProjectViewNodeDecorator

class PythonProjectViewNodeDecorator : ProjectViewNodeDecorator {
  override fun decorate(node: ProjectViewNode<*>, data: PresentationData) {
    if (!PluginSettingsState.instance.pythonSupport) return
    if (node.virtualFile?.isDirectory == true) return

    val extension = node.virtualFile?.name?.split(".")?.last()

    if (extension.equals("py")) {
      data.setIcon(icons.EXT_TO_ICONS[extension])
    }
  }
}
