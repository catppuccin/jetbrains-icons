package com.github.catppuccin.jetbrains_icons.settings.views

import java.awt.Dimension
import java.awt.Image
import javax.swing.Box
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JPanel

class SettingsHeaderView : JPanel() {
  init {
    drawLogo()
    add(Box.createRigidArea(Dimension(4, 0)))
    drawTitle()
  }

  private fun drawLogo() {
    val url = javaClass.getResource("/pluginIcon.png")
    var image = ImageIcon(url)
    image = ImageIcon(image.image.getScaledInstance(60, 60, Image.SCALE_SMOOTH))

    val field = JLabel(image)
    add(field)
  }

  private fun drawTitle() {
    val label = JLabel("Catppuccin Icons")
    label.font = label.font.deriveFont(24.0f)
    add(label)
  }
}
