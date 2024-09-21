package com.github.catppuccin.jetbrains_icons.settings.views

import com.github.catppuccin.jetbrains_icons.settings.PluginSettingsState
import com.intellij.ide.plugins.PluginManager.isPluginInstalled
import com.intellij.openapi.extensions.PluginId.*
import com.intellij.ui.components.JBCheckBox
import com.intellij.util.ui.FormBuilder
import java.awt.FlowLayout
import javax.swing.JPanel

class SettingsAdditionalSupportView : JPanel() {
    val python = JBCheckBox("Python", PluginSettingsState.instance.pythonSupport)
    val java = JBCheckBox("Java Filetypes", PluginSettingsState.instance.javaSupport).apply {
        isEnabled = isPluginInstalled(findId("com.intellij.java"))
    }

    init {
        val form = FormBuilder.createFormBuilder()
            .addComponent(python)
            .addTooltip("Override the Python plugin icons")
            .addComponent(java)
            .addTooltip("Use different shapes and colors for Java filetypes (e.g. Class, Interface, Record, etc.)")
            .panel

        add(form)

        layout = FlowLayout(FlowLayout.LEADING)
    }
}
