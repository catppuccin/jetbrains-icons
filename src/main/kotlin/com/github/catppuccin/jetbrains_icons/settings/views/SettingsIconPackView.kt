package com.github.catppuccin.jetbrains_icons.settings.views

import com.github.catppuccin.jetbrains_icons.settings.Variant
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import java.awt.Component
import java.awt.FlowLayout
import javax.swing.JPanel

class SettingsIconPackView(private val currentVariant: String) : JPanel() {
    private val dropdown = ComboBox<Variant>()

    val variant: String
        get() = (dropdown.selectedItem as Variant).id

    init {
        drawDropdown()

        val form = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Variant:"), dropdown, 1, false)
            .addTooltip("Restart IDE to apply changes")
            .panel

        add(form)

        layout = FlowLayout(FlowLayout.LEADING)
    }

    private fun drawDropdown() {
        for (variant in Variant.values()) {
            dropdown.addItem(variant)
        }
        dropdown.selectedItem = Variant.values().find { it.id == currentVariant }
        dropdown.alignmentY = Component.CENTER_ALIGNMENT
    }
}
