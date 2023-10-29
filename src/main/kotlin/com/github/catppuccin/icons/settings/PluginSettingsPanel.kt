package com.github.catppuccin.icons.settings

import com.intellij.openapi.ui.ComboBox
import java.awt.Component
import java.awt.Dimension
import javax.swing.*

enum class Variant(val id: String, val label: String) {
    LATTE("latte", "Catppuccin Latte"),
    FRAPPE("frappe", "Catppuccin Frappé"),
    MACCHIATO("macchiato", "Catppuccin Macchiato"),
    MOCHA("mocha", "Catppuccin Mocha");

    override fun toString(): String {
        return label
    }
}

class PluginSettingsPanel(private val currentVariant: String) : JPanel() {
    private val dropdown = ComboBox<Variant>()

    val variant: String
        get() = (dropdown.selectedItem as Variant).id

    init {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        drawLogo(panel)
        panel.add(Box.createRigidArea(Dimension(0, 10)))
        drawTitle(panel)
        panel.add(Box.createRigidArea(Dimension(0, 20)))
        drawDropdown(panel)
        panel.add(Box.createRigidArea(Dimension(0, 20)))
        drawHint(panel)

        add(panel)
    }

    private fun drawLogo(panel: JPanel) {
        val url = javaClass.getResource("/pluginIcon.png")
        var image = ImageIcon(url)
        image = ImageIcon(image.image.getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH))

        val field = JLabel(image)
        field.alignmentX = Component.CENTER_ALIGNMENT
        panel.add(field)
    }

    private fun drawTitle(panel: JPanel) {
        val label = JLabel("Catppuccin Icons")
        label.font = label.font.deriveFont(24.0f)
        label.alignmentX = Component.CENTER_ALIGNMENT
        panel.add(label)
    }

    private fun drawDropdown(panel: JPanel) {
        val horizontalPanel = JPanel()

        val label = JLabel("Icon Pack")
        label.alignmentY = Component.CENTER_ALIGNMENT
        horizontalPanel.add(label)

        for (variant in Variant.values()) {
            dropdown.addItem(variant)
        }

        dropdown.selectedItem = Variant.values().find { it.id == currentVariant }
        dropdown.alignmentY = Component.CENTER_ALIGNMENT
        horizontalPanel.add(dropdown)

        panel.add(horizontalPanel)
    }

    private fun drawHint(panel: JPanel) {
        val label = JLabel("Restart IDE to apply changes")
        label.alignmentX = Component.CENTER_ALIGNMENT
        label.font = label.font.deriveFont(11.0f)
        panel.add(label)
    }
}
