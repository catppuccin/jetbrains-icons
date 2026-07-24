package com.github.catppuccin.jetbrains_icons.settings

import com.github.catppuccin.jetbrains_icons.util.IconPackApplier
import com.github.catppuccin.jetbrains_icons.util.IdeTheme
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service

@Service
class ThemeSyncService {
  private val lock = Any()

  fun syncVariantFromTheme(): Boolean {
    val target =
      synchronized(lock) {
        val state = PluginSettingsState.instance
        if (!state.syncWithOs) return@synchronized null
        resolveVariant(isDark = IdeTheme.isDark(), state = state)
      } ?: return false

    return IconPackApplier.apply(target)
  }

  companion object {
    val instance: ThemeSyncService
      get() = ApplicationManager.getApplication().getService(ThemeSyncService::class.java)

    fun resolveVariant(isDark: Boolean, state: PluginSettingsState): String =
      if (isDark) state.darkVariant else state.lightVariant
  }
}
