package com.github.catppuccin.jetbrains_icons.model

import kotlinx.serialization.Serializable

@Serializable data class PatchConfig(val targetIcon: String, val patchIcon: String)

@Serializable
data class IconPatcherConfig(
  val name: String,
  val enabledSetting: String,
  val overrides: List<PatchConfig>,
)

@Serializable data class IconPatcherConfigList(val patcherConfigs: List<IconPatcherConfig>)
