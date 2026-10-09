package com.github.catppuccin.jetbrains_icons

import com.github.catppuccin.jetbrains_icons.settings.PluginSettingsState

object IconPack {
  @Volatile private var current: Icons? = null

  val icons: Icons
    get() = current ?: load(PluginSettingsState.instance.variant).also { current = it }

  fun reload(variant: String = PluginSettingsState.instance.variant) {
    current = load(variant)
  }

  private fun load(variant: String): Icons = Icons(variant)
}
