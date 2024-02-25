package com.github.catppuccin.jetbrains_icons

import com.github.catppuccin.jetbrains_icons.settings.PluginSettingsState
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNodeDecorator
import com.intellij.packageDependencies.ui.PackageDependenciesNode
import com.intellij.ui.ColoredTreeCellRenderer

// hack: pycharm python plugin overrides .py icons in files tree
// by using ProjectViewNodeDecorator we can override it with our icon
class ProjectViewNodeDecorator: ProjectViewNodeDecorator {
    private var icons = Icons(PluginSettingsState.instance.variant)

    override fun decorate(node: ProjectViewNode<*>, data: PresentationData) {
        val file_type = node.getVirtualFile()?.name?.split(".")?.last()

        if (file_type.equals("py")) {
            data.setIcon(icons.EXT_TO_ICONS["py"])
        }
    }

    // deprecated
    override fun decorate(node: PackageDependenciesNode, cellRender: ColoredTreeCellRenderer) {
    }
}
