package com.github.catppuccin.jetbrains_icons.activity

import com.github.catppuccin.jetbrains_icons.patcher.PythonIconPatcher
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

/** Activity responsible for loading and managing IconPathPatchers */
class IconPathPatcherActivity : ProjectActivity {
  override suspend fun execute(project: Project) {
    PythonIconPatcher.install()
  }
}
