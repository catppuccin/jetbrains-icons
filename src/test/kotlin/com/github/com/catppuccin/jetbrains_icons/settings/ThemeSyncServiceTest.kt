package com.github.com.catppuccin.jetbrains_icons.settings

import com.github.catppuccin.jetbrains_icons.settings.PluginSettingsState
import com.github.catppuccin.jetbrains_icons.settings.ThemeSyncService
import com.github.catppuccin.jetbrains_icons.settings.Variant
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ThemeSyncServiceTest {
  @Test
  fun resolveVariant_usesDarkPackWhenDark() {
    val state =
      PluginSettingsState().apply {
        darkVariant = Variant.MOCHA.id
        lightVariant = Variant.LATTE.id
      }

    assertEquals(Variant.MOCHA.id, ThemeSyncService.resolveVariant(isDark = true, state = state))
  }

  @Test
  fun resolveVariant_usesLightPackWhenLight() {
    val state =
      PluginSettingsState().apply {
        darkVariant = Variant.MACCHIATO.id
        lightVariant = Variant.LATTE.id
      }

    assertEquals(Variant.LATTE.id, ThemeSyncService.resolveVariant(isDark = false, state = state))
  }

  @Test
  fun resolveVariant_supportsAllFlavors() {
    val state =
      PluginSettingsState().apply {
        darkVariant = Variant.FRAPPE.id
        lightVariant = Variant.MACCHIATO.id
      }

    assertEquals(Variant.FRAPPE.id, ThemeSyncService.resolveVariant(isDark = true, state = state))
    assertEquals(
      Variant.MACCHIATO.id,
      ThemeSyncService.resolveVariant(isDark = false, state = state),
    )
  }
}
