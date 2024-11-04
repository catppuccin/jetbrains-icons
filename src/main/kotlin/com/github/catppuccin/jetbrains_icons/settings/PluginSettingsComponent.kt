package com.github.catppuccin.jetbrains_icons.settings

import com.github.catppuccin.jetbrains_icons.settings.views.SettingsAboutView
import com.github.catppuccin.jetbrains_icons.settings.views.SettingsAdditionalSupportView
import com.github.catppuccin.jetbrains_icons.settings.views.SettingsHeaderView
import com.github.catppuccin.jetbrains_icons.settings.views.SettingsIconPackView
import com.intellij.ui.TitledSeparator
import com.intellij.util.ui.FormBuilder
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

class PluginSettingsComponent(currentVariant: String) {
  var view = JPanel()
    private set

  var iconPack = SettingsIconPackView(currentVariant)
    private set

  var additionalSupport = SettingsAdditionalSupportView()
    private set

  init {
    view =
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
}
