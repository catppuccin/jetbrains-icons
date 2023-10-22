package com.github.catppuccin.icons.settings

import com.intellij.openapi.ui.ComboBox
import java.awt.Component
import java.awt.Dimension
import javax.swing.*

class PluginSettingsPanel() : JPanel() {
    init {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        drawLogo(panel)
        panel.add(Box.createRigidArea(Dimension(0, 10)))
        drawTitle(panel)
        panel.add(Box.createRigidArea(Dimension(0, 20)))
        drawDropdown(panel)

        add(panel)
    }

    private fun drawLogo(panel: JPanel) {
        val url = javaClass.getResource("/catppuccin.png")
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

        horizontalPanel.add(Box.createRigidArea(Dimension(5, 0)))

        val dropdown = ComboBox<String>()
        dropdown.addItem("Catppuccin Latte")
        dropdown.addItem("Catppuccin Frappé")
        dropdown.addItem("Catppuccin Macchiato")
        dropdown.addItem("Catppuccin Mocha")

        dropdown.alignmentY = Component.CENTER_ALIGNMENT
        horizontalPanel.add(dropdown)

        panel.add(horizontalPanel)
    }
}
