package com.github.catppuccin.jetbrains_icons.settings

import com.github.catppuccin.jetbrains_icons.settings.views.SettingsAboutView
import com.github.catppuccin.jetbrains_icons.settings.views.SettingsAdditionalSupportView
import com.github.catppuccin.jetbrains_icons.settings.views.SettingsHeaderView
import com.github.catppuccin.jetbrains_icons.settings.views.SettingsIconPackView
import com.intellij.ui.TitledSeparator
import com.intellij.util.ui.FormBuilder
import javax.swing.JPanel

enum class Variant(val id: String, private val label: String) {
  LATTE("latte", "Catppuccin Latte"),
  FRAPPE("frappe", "Catppuccin Frappé"),
  MACCHIATO("macchiato", "Catppuccin Macchiato"),
  MOCHA("mocha", "Catppuccin Mocha");

  override fun toString(): String = label
}

class PluginSettingsComponent(currentVariant: String) {
  val additionalSupport = SettingsAdditionalSupportView()
  val iconPack = SettingsIconPackView(currentVariant)

  val view: JPanel =
    FormBuilder.createFormBuilder()
      .addComponent(SettingsHeaderView())
      .addComponent(TitledSeparator("General"))
      .addComponent(iconPack)
      .addComponent(TitledSeparator("Additional Support"))
      .addComponent(additionalSupport)
      .addComponent(TitledSeparator("About"))
      .addComponent(SettingsAboutView())
      .addComponentFillVertically(JPanel(), 0)
      .panel
}
