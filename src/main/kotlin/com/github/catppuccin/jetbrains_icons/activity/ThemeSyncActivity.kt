package com.github.catppuccin.jetbrains_icons.activity

import com.github.catppuccin.jetbrains_icons.settings.ThemeSyncService
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class ThemeSyncActivity : ProjectActivity {
  override suspend fun execute(project: Project) {
    ThemeSyncService.instance.syncVariantFromTheme()
  }
}
