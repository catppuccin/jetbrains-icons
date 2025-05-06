package com.github.catppuccin.jetbrains_icons.patcher

import com.github.catppuccin.jetbrains_icons.model.IconPatcherConfigList
import com.github.catppuccin.jetbrains_icons.settings.PluginSettingsState
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.util.IconPathPatcher
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * An [IconPathPatcher] implementation that dynamically patches icon paths based on user settings
 * and variant selection.
 *
 * This patcher allows for conditional icon replacement in JetBrains IDEs, supporting multiple
 * variants and user-configurable overrides. It is typically installed via [installAll] using
 * configurations loaded from a JSON resource.
 *
 * @property isEnabled A function that determines if the patcher is currently enabled.
 * @property overrideMap A map of original icon paths to their replacement paths.
 * @property variantProvider A function that provides the current icon variant (e.g., "latte",
 *   "frappe").
 */
class GeneralIconPatcher(
  private val isEnabled: () -> Boolean,
  private val overrideMap: Map<String, String>,
  private val variantProvider: () -> String = { PluginSettingsState.instance.variant },
) : IconPathPatcher() {
  /**
   * Patches the given icon path if the patcher is enabled, returning the patched path or null
   * otherwise.
   *
   * @param path The original icon path.
   * @param classLoader The class loader to use (may be null).
   * @return The patched icon path if enabled, or null if not.
   */
  override fun patchPath(path: String, classLoader: ClassLoader?): String? =
    if (isEnabled()) {
      overrideMap[path]?.let { "/jetbrains_icons/icons/${variantProvider()}$it" }
    } else null

  /**
   * Returns the class loader to use for loading icons.
   *
   * @param path The icon path.
   * @param originalClassLoader The original class loader (may be null).
   * @return The class loader to use for loading icons.
   */
  override fun getContextClassLoader(
    path: String,
    originalClassLoader: ClassLoader?,
  ): ClassLoader? = javaClass.classLoader

  companion object {
    private val isInstalledMutex = Mutex()
    private var isInstalled = false

    /**
     * Installs all provided icon patcher configs, ensuring installation only occurs once.
     *
     * @param configs The list of [Config]s to install.
     */
    suspend fun installAll(configs: List<Config>) =
      isInstalledMutex.withLock {
        if (!isInstalled) {
          isInstalled = true
          configs.forEach { config ->
            IconLoader.installPathPatcher(
              GeneralIconPatcher(config.isEnabled, config.overrideMap, config.variantProvider)
            )
          }
        }
      }

    /**
     * Configuration for a [GeneralIconPatcher] instance.
     *
     * @property isEnabled A function that determines if this patcher config is enabled.
     * @property overrideMap A map of original icon paths to their replacement paths for this
     *   config.
     * @property variantProvider A function that provides the current icon variant for this config.
     */
    data class Config(
      val isEnabled: () -> Boolean,
      val overrideMap: Map<String, String>,
      val variantProvider: () -> String = { PluginSettingsState.instance.variant },
    )

    /**
     * Loads icon patcher configs from a JSON resource file.
     *
     * @param resourcePath The path to the JSON resource file. Defaults to "icon_patchers.json".
     * @return A list of [Config] objects parsed from the JSON.
     * @throws IllegalArgumentException if the resource is not found.
     */
    fun loadConfigsFromJson(resourcePath: String = "icon_patchers.json"): List<Config> {
      val resourceStream =
        requireNotNull(
          GeneralIconPatcher::class.java.classLoader.getResourceAsStream(resourcePath)
        ) {
          "Resource $resourcePath not found"
        }
      val jsonString = resourceStream.bufferedReader().use { it.readText() }
      val json = Json { ignoreUnknownKeys = true }
      val configList = json.decodeFromString<IconPatcherConfigList>(jsonString)
      val settings = PluginSettingsState.instance
      return configList.patcherConfigs.map { iconPatcherConfig ->
        val overrideMap = iconPatcherConfig.overrides.associate { it.targetIcon to it.patchIcon }
        val isEnabled: () -> Boolean = {
          runCatching {
              val prop =
                settings::class.members.first { it.name == iconPatcherConfig.enabledSetting }
              prop.call(settings) as Boolean
            }
            .getOrDefault(false)
        }
        Config(isEnabled, overrideMap)
      }
    }
  }
}
