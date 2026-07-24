package com.github.catppuccin.jetbrains_icons.listeners

import com.github.catppuccin.jetbrains_icons.settings.ThemeSyncService
import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.LafManagerListener

class ThemeSyncListener : LafManagerListener {
  override fun lookAndFeelChanged(source: LafManager) {
    ThemeSyncService.instance.syncVariantFromTheme()
  }
}
