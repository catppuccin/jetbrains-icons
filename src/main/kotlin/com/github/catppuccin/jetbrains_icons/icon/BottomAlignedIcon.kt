package com.github.catppuccin.jetbrains_icons.icon

import com.intellij.util.ui.JBUI
import java.awt.Component
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import javax.swing.Icon

class BottomAlignedIcon(private val parentIcon: Icon, private val childIcon: Icon,private val scaleFactor: Double = 1.0) : Icon {

    override fun getIconWidth(): Int = parentIcon.iconWidth

    override fun getIconHeight(): Int = parentIcon.iconHeight

    override fun paintIcon(c: Component?, g: Graphics, x: Int, y: Int) {
        // Paint the parent icon at the given (x, y)
        parentIcon.paintIcon(c, g, x, y)

        // Create a Graphics2D object from the original Graphics
        val g2d = g.create() as Graphics2D

        // Apply rendering hints for better image quality during scaling
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC)
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        // Calculate the new size of the child icon based on the scale factor
        val scaledWidth = (childIcon.iconWidth * scaleFactor).toInt()

        val dpiScaledOffset = JBUI.scale(5)

        // Apply the fixed vertical offset: y + parentIcon.iconHeight - 4
        val yOffset = parentIcon.iconHeight - dpiScaledOffset

        // Calculate the x-offset to center the scaled child icon
        // This compensates for the horizontal shift by adjusting by half the width difference
        val xOffset = (parentIcon.iconWidth - scaledWidth) / 2

        // Apply scaling and translate to the correct position
        g2d.translate(x + xOffset.toDouble(), (y + yOffset).toDouble())
        g2d.scale(scaleFactor, scaleFactor)

        // Paint the child icon with scaling and positioning applied
        childIcon.paintIcon(c, g2d, 0, 0)

        // Dispose of the Graphics2D context to free resources
        g2d.dispose()
    }
}
