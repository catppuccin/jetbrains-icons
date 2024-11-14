package com.github.catppuccin.jetbrains_icons.commons.settings.views

import com.intellij.ide.BrowserUtil
import com.intellij.ui.components.ActionLink
import com.intellij.util.ui.FormBuilder
import java.awt.FlowLayout
import javax.swing.JPanel

class SettingsAboutView : JPanel() {
  init {
    val panel =
      FormBuilder.createFormBuilder()
        .addComponent(drawChangelogLink())
        .addComponent(drawIssuesLink())
        .panel

    add(panel)

    layout = FlowLayout(FlowLayout.LEADING)
  }

  private fun drawChangelogLink(): ActionLink {
    val link =
      ActionLink("Changelog") {
        BrowserUtil.browse("https://github.com/catppuccin/jetbrains-icons/releases")
      }
    link.setExternalLinkIcon()
    return link
  }

  private fun drawIssuesLink(): ActionLink {
    val link =
      ActionLink("Report an issue") {
        BrowserUtil.browse("https://github.com/catppuccin/jetbrains-icons/issues")
      }
    link.setExternalLinkIcon()
    return link
  }
}
