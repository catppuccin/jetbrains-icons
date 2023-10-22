package com.github.catppuccin.icons.settings

import com.intellij.openapi.options.Configurable
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
    }

    override fun getDisplayName(): String {
        return "Catppuccin Icons"
    }
}
