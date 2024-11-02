package com.github.catppuccin.jetbrains_icons

import com.github.catppuccin.jetbrains_icons.settings.PluginSettingsState

object IconPack {
  val icons: Icons by lazy { Icons(PluginSettingsState.instance.variant) }
}
