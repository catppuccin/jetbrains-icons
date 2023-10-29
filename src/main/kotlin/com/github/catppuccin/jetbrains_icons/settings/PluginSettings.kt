package com.github.catppuccin.jetbrains_icons.settings

import com.intellij.ide.GeneralSettings
import com.intellij.ide.IdeBundle
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ex.ApplicationEx
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.Messages
import javax.swing.JComponent


class PluginSettings : Configurable {
    private val panel = PluginSettingsPanel(PluginSettingsState.instance.variant)

    override fun createComponent(): JComponent {
        return panel
    }

    override fun isModified(): Boolean {
        val state = PluginSettingsState.instance
        return panel.variant != state.variant
    }

    override fun apply() {
        val state = PluginSettingsState.instance
        state.variant = panel.variant
        restart()
    }

    override fun getDisplayName(): String {
        return "Catppuccin Icons"
    }

    private fun restart() {
        val result = if (GeneralSettings.getInstance().isConfirmExit) {
            Messages.showYesNoDialog(
                "The IDE needs to be restarted for the changes to take effect. Restart now?",
                IdeBundle.message("dialog.title.restart.ide"),
                IdeBundle.message("dialog.action.restart.yes"),
                IdeBundle.message("dialog.action.restart.cancel"),
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
