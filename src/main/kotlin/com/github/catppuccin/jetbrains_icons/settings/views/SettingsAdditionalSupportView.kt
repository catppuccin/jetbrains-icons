package com.github.catppuccin.jetbrains_icons.settings.views

import com.github.catppuccin.jetbrains_icons.settings.PluginSettingsState
import com.intellij.ui.components.JBCheckBox
import com.intellij.util.ui.FormBuilder
import java.awt.FlowLayout
import javax.swing.JPanel

class SettingsAdditionalSupportView : JPanel() {
    val python = JBCheckBox("Python", PluginSettingsState.instance.pythonSupport)

    init {
        val form = FormBuilder.createFormBuilder()
            .addComponent(python)
            .addTooltip("Override the Python plugin icons")
            .panel

        add(form)

        layout = FlowLayout(FlowLayout.LEADING)
    }
}
