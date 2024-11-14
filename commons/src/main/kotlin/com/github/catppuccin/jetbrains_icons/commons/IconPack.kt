package com.github.catppuccin.jetbrains_icons.commons

import com.github.catppuccin.jetbrains_icons.commons.settings.PluginSettingsState

object IconPack {
  val icons: Icons by lazy { Icons(PluginSettingsState.instance.variant) }
}
