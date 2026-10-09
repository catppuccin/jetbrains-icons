package com.github.catppuccin.jetbrains_icons.util

import com.github.catppuccin.jetbrains_icons.IconPack
import com.github.catppuccin.jetbrains_icons.settings.PluginSettingsState
import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.wm.WindowManager

object IconPackApplier {
  fun apply(variant: String, force: Boolean = false): Boolean {
    val state = PluginSettingsState.instance
    if (!force && state.variant == variant) return false

    state.variant = variant
    reloadPackAndCaches(variant)
    refreshUi()
    return true
  }

  fun reloadCurrent() {
    reloadPackAndCaches(PluginSettingsState.instance.variant)
    refreshUi()
  }

  private fun reloadPackAndCaches(variant: String) {
    IconPack.reload(variant)
    IconLoader.clearCache()
  }

  fun refreshUi() {
    val application = ApplicationManager.getApplication()
    if (application.isUnitTestMode) return

    val refresh = Runnable {
      for (project in ProjectManager.getInstance().openProjects) {
        if (project.isDisposed) continue
        val projectView = ProjectView.getInstance(project)
        projectView?.refresh()
        projectView?.currentProjectViewPane?.updateFromRoot(true)
      }

      WindowManager.getInstance().allProjectFrames.forEach { frame -> frame.component?.repaint() }
    }

    if (application.isDispatchThread) {
      refresh.run()
    } else {
      application.invokeLater(refresh)
    }
  }
}
