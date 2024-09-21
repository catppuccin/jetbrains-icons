package com.github.catppuccin.jetbrains_icons.settings

import com.github.catppuccin.jetbrains_icons.bundles.PluginSettingsBundle
import com.intellij.ide.GeneralSettings
import com.intellij.ide.IdeBundle
import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ex.ApplicationEx
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.ui.Messages
import javax.swing.JComponent


class PluginSettings : Configurable {
    private val component = PluginSettingsComponent(PluginSettingsState.instance.variant)

    override fun createComponent(): JComponent {
        return component.view
    }

    private fun packChanged(): Boolean {
        val state = PluginSettingsState.instance
        return component.iconPack.variant != state.variant
    }

    override fun isModified(): Boolean {
        val state = PluginSettingsState.instance
        return packChanged() ||
            component.additionalSupport.python.isSelected != state.pythonSupport ||
            component.additionalSupport.go.isSelected != state.goSupport ||
            component.additionalSupport.java.isSelected != state.javaSupport
    }

    override fun apply() {
        val state = PluginSettingsState.instance

        state.pythonSupport = component.additionalSupport.python.isSelected
        state.javaSupport = component.additionalSupport.java.isSelected
        state.goSupport = component.additionalSupport.go.isSelected

        if (packChanged()) {
            state.variant = component.iconPack.variant
            restart()
        }

        val projects = ProjectManager.getInstance().openProjects
        for (project in projects) {
            val projectView = ProjectView.getInstance(project)
            projectView?.refresh()
            projectView?.currentProjectViewPane?.updateFromRoot(true)
        }
    }

    override fun getDisplayName(): String {
        return "Catppuccin Icons"
    }

    private fun restart() {
        val result = if (GeneralSettings.getInstance().isConfirmExit) {
            Messages.showYesNoDialog(
                PluginSettingsBundle.message("dialog.message.restart.ide"),
                PluginSettingsBundle.message("dialog.title.restart.ide"),
                PluginSettingsBundle.message("dialog.action.restart.yes"),
                PluginSettingsBundle.message("dialog.action.restart.cancel"),
                Messages.getWarningIcon()
            ) == Messages.YES
        } else {
            true
        }

        if (result) {
            val app = ApplicationManager.getApplication() as ApplicationEx
            app.restart(true)
        }
    }
}
