package com.github.catppuccin.jetbrains_icons.settings

import com.intellij.ide.plugins.PluginManager.*
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.extensions.PluginId.findId
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "com.github.catppuccin.jetbrains_icons.settings.PluginSettingsState",
    storages = [Storage("CatppuccinIcons.xml")]
)
class PluginSettingsState : PersistentStateComponent<PluginSettingsState> {
    var variant = Variant.MOCHA.id

    var pythonSupport = true
    var javaSupport = isPluginInstalled(findId("com.intellij.java"))
    var goSupport = true

    companion object {
        val instance: PluginSettingsState
            get() = ApplicationManager.getApplication().getService(PluginSettingsState::class.java)
    }

    override fun getState(): PluginSettingsState {
        return this
    }

    override fun loadState(state: PluginSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }
}
