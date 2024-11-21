package com.github.catppuccin.jetbrains_icons.patcher

import com.github.catppuccin.jetbrains_icons.settings.PluginSettingsState
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.util.IconPathPatcher

class PythonIconPatcher : IconPathPatcher() {

  private val settingsInstance = PluginSettingsState.instance

  // Only patch the path if Python support is enabled
  override fun patchPath(path: String, classLoader: ClassLoader?): String? {
    if (!settingsInstance.pythonSupport || !overrideMap.containsKey(path)) return null
    return "/jetbrains_icons/icons/${settingsInstance.variant}${overrideMap[path]}"
  }

  override fun getContextClassLoader(
    path: String,
    originalClassLoader: ClassLoader?,
  ): ClassLoader? = javaClass.classLoader

  companion object {
    fun install() = patcherInstance

    private val patcherInstance: Unit by lazy { IconLoader.installPathPatcher(PythonIconPatcher()) }

    val overrideMap: Map<String, String> =
      mapOf(
        // Icon in New UI
        "/icons/com/jetbrains/python/parser/pythonFile.svg" to "_python.svg",
        // Unknown, but reported by UI Inspector when inspecting icon before replacement in new UI
        "/icons/com/jetbrains/python/parser/expui/python.svg" to "}_python.svg",
        // Icon in Old UI
        "/icons/com/jetbrains/python/pythonFile.svg" to "_python.svg",
      )
  }
}
