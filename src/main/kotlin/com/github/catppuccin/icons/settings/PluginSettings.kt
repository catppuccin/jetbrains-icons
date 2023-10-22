package com.github.catppuccin.icons.settings

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class PluginSettings : Configurable {
    override fun createComponent(): JComponent {
        return PluginSettingsPanel()
    }

    override fun isModified(): Boolean {
        return false
    }

    override fun apply() {
        return
    }

    override fun getDisplayName(): String {
        return "Catppuccin Icons"
    }
}
