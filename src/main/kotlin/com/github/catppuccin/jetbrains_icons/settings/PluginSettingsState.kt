package com.github.catppuccin.jetbrains_icons.settings

import com.intellij.ide.plugins.PluginManager.isPluginInstalled
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.extensions.PluginId.getId
import com.intellij.openapi.fileTypes.FileNameMatcher
import com.intellij.openapi.fileTypes.WildcardFileNameMatcher
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
  name = "com.github.catppuccin.jetbrains_icons.settings.PluginSettingsState",
  storages = [Storage("CatppuccinIcons.xml")],
)
class PluginSettingsState : PersistentStateComponent<PluginSettingsState> {
  var variant = Variant.MOCHA.id

  var pythonSupport = true
  var javaSupport = isPluginInstalled(getId("com.intellij.java"))
  var goSupport = true
  var ignoredFiles = ""
    set(value) {
      if (value == field) return

      field = value
      ignoredFileMatchers =
        value.split(",").map(String::trim).filter(String::isNotEmpty).map(::WildcardFileNameMatcher)
    }

  @Transient var ignoredFileMatchers: List<FileNameMatcher> = emptyList()

  override fun getState(): PluginSettingsState = this

  override fun loadState(state: PluginSettingsState) = XmlSerializerUtil.copyBean(state, this)

  companion object {
    val instance: PluginSettingsState
      get() = ApplicationManager.getApplication().getService(PluginSettingsState::class.java)
  }
}
