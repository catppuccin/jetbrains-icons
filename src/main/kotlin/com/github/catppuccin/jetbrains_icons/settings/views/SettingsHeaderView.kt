package com.github.catppuccin.jetbrains_icons.settings.views

import java.awt.Dimension
import java.awt.Image
import javax.swing.Box
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JPanel

class SettingsHeaderView : JPanel() {

  companion object {
    private const val SPACER_WIDTH = 4
    private const val SPACER_HEIGHT = 8
    private const val LOGO_SIZE = 60
    private const val FONT_SIZE = 24.0f
  }

  init {
    drawLogo()

    // Draw spacer between Logo and Title
    add(Box.createRigidArea(Dimension(SPACER_WIDTH, SPACER_HEIGHT)))

    drawTitle()
  }

  private fun drawLogo() {
    val url = javaClass.getResource("/pluginIcon.png")
    var image = ImageIcon(url)
    image = ImageIcon(image.image.getScaledInstance(LOGO_SIZE, LOGO_SIZE, Image.SCALE_SMOOTH))

    val field = JLabel(image)
    add(field)
  }

  private fun drawTitle() {
    val label = JLabel("Catppuccin Icons")
    label.font = label.font.deriveFont(FONT_SIZE)
    add(label)
  }
}
