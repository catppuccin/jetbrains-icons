package com.github.catppuccin.jetbrains_icons.patcher

import com.github.catppuccin.jetbrains_icons.settings.PluginSettingsState
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.util.IconPathPatcher
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class GoIconPatcher : IconPathPatcher() {

  private val settingsInstance = PluginSettingsState.instance

  // Only patch the path if Go support is enabled
  override fun patchPath(path: String, classLoader: ClassLoader?): String? =
    if (settingsInstance.goSupport && overrideMap.containsKey(path)) {
      "/jetbrains_icons/icons/${settingsInstance.variant}${overrideMap[path]}"
    } else {
      null
    }

  override fun getContextClassLoader(
    path: String,
    originalClassLoader: ClassLoader?,
  ): ClassLoader? = javaClass.classLoader

  companion object {
    private val isInstalledMutex = Mutex()
    private var isInstalled = false

    suspend fun install() =
      isInstalledMutex.withLock {
        if (!isInstalled) {
          isInstalled = true
          IconLoader.installPathPatcher(GoIconPatcher())
        }
      }

    private val overrideMap: Map<String, String> =
      mapOf("/icons/expui/runTest@20x20.svg" to "_go.svg", "/icons/runTest.svg" to "_go.svg")
  }
}
