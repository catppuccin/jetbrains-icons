package com.github.catppuccin.jetbrains_icons.bundles;

import com.intellij.AbstractBundle
import org.jetbrains.annotations.PropertyKey

object PluginSettingsBundle : AbstractBundle("messages.PluginSettingsBundle") {

  private const val BUNDLE = "messages.PluginSettingsBundle"

  fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any): String {
    return getMessage(key, *params)
  }
}
