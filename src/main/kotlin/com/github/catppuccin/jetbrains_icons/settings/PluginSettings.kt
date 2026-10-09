package com.github.catppuccin.jetbrains_icons.settings

import com.github.catppuccin.jetbrains_icons.util.IconPackApplier
import com.github.catppuccin.jetbrains_icons.util.IdeTheme
import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class PluginSettings : Configurable {
  private var component: PluginSettingsComponent? = null

  private fun settings(): PluginSettingsComponent =
    component ?: PluginSettingsComponent(PluginSettingsState.instance).also { component = it }

  override fun createComponent(): JComponent = settings().view

  override fun isModified(): Boolean {
    val component = component ?: return false
    val state = PluginSettingsState.instance
    return packChanged(component, state) ||
      syncSettingsChanged(component, state) ||
      component.additionalSupport.python.isSelected != state.pythonSupport ||
      component.additionalSupport.java.isSelected != state.javaSupport ||
      component.additionalSupport.go.isSelected != state.goSupport
  }

  override fun apply() {
    val component = settings()
    val state = PluginSettingsState.instance

    val supportChanged =
      component.additionalSupport.python.isSelected != state.pythonSupport ||
        component.additionalSupport.java.isSelected != state.javaSupport ||
        component.additionalSupport.go.isSelected != state.goSupport

    state.pythonSupport = component.additionalSupport.python.isSelected
    state.javaSupport = component.additionalSupport.java.isSelected
    state.goSupport = component.additionalSupport.go.isSelected

    state.syncWithOs = component.iconPack.syncWithOs
    state.darkVariant = component.iconPack.darkVariant
    state.lightVariant = component.iconPack.lightVariant

    val targetVariant =
      if (state.syncWithOs) {
        if (IdeTheme.isDark()) state.darkVariant else state.lightVariant
      } else {
        component.iconPack.variant
      }

    val variantChanged = targetVariant != state.variant
    if (variantChanged) {
      IconPackApplier.apply(targetVariant)
    } else if (supportChanged) {
      IconPackApplier.reloadCurrent()
    } else {
      IconPackApplier.refreshUi()
    }
  }

  override fun getDisplayName(): String = "Catppuccin Icons"

  override fun disposeUIResources() {
    component = null
  }

  private fun packChanged(component: PluginSettingsComponent, state: PluginSettingsState): Boolean =
    component.iconPack.variant != state.variant

  private fun syncSettingsChanged(
    component: PluginSettingsComponent,
    state: PluginSettingsState,
  ): Boolean =
    component.iconPack.syncWithOs != state.syncWithOs ||
      component.iconPack.darkVariant != state.darkVariant ||
      component.iconPack.lightVariant != state.lightVariant
}
