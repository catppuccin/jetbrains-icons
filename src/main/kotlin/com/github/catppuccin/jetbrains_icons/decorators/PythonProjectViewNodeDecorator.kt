package com.github.catppuccin.jetbrains_icons.decorators

import com.github.catppuccin.jetbrains_icons.Icons
import com.github.catppuccin.jetbrains_icons.settings.PluginSettingsState
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNodeDecorator

class PythonProjectViewNodeDecorator : ProjectViewNodeDecorator {
    private var icons = Icons(PluginSettingsState.instance.variant)

    override fun decorate(node: ProjectViewNode<*>, data: PresentationData) {
        val fileType = node.virtualFile?.name?.split(".")?.last()

        if (fileType.equals("py")) {
            data.setIcon(icons.EXT_TO_ICONS["py"])
        }
    }
}
