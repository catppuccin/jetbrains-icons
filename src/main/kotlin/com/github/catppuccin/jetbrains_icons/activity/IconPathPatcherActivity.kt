package com.github.catppuccin.jetbrains_icons.activity

import com.github.catppuccin.jetbrains_icons.patcher.GeneralIconPatcher
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.util.IconPathPatcher

/** Activity responsible for loading and managing [IconPathPatcher] instances */
class IconPathPatcherActivity : ProjectActivity {
  override suspend fun execute(project: Project) {
    GeneralIconPatcher.installAll(GeneralIconPatcher.loadConfigsFromJson())
  }
}
