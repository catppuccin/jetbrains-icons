package com.github.catppuccin.jetbrains_icons.settings.views

import com.github.catppuccin.jetbrains_icons.bundles.PluginSettingsBundle
import com.github.catppuccin.jetbrains_icons.settings.PluginSettingsState
import com.github.catppuccin.jetbrains_icons.settings.Variant
import com.github.catppuccin.jetbrains_icons.util.IdeTheme
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.Presentation
import com.intellij.openapi.actionSystem.Separator
import com.intellij.openapi.actionSystem.ToggleAction
import com.intellij.openapi.actionSystem.impl.ActionButton
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.util.IconUtil
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.JBUI
import java.awt.Component
import java.awt.FlowLayout
import javax.swing.Icon
import javax.swing.JPanel

class SettingsIconPackView(state: PluginSettingsState) : JPanel() {
  private val dropdown = variantCombo(state.variant)

  private var selectedDarkVariant: String =
    if (state.darkVariant == Variant.LATTE.id) Variant.MOCHA.id else state.darkVariant

  private var selectedLightVariant: String = Variant.LATTE.id

  val syncCheckbox =
    JBCheckBox(PluginSettingsBundle.message("settings.sync.with.os"), state.syncWithOs)

  private val gearIcon: Icon = themedActionIcon(AllIcons.General.Gear)

  private val preferredPacksGroup =
    object : DefaultActionGroup() {
      init {
        isPopup = true
        templatePresentation.icon = gearIcon
        templatePresentation.text = PluginSettingsBundle.message("settings.sync.gear.tooltip")
      }

      override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.EDT
    }

  private val gearButton: ActionButton

  val variant: String
    get() = (dropdown.selectedItem as Variant).id

  val syncWithOs: Boolean
    get() = syncCheckbox.isSelected

  val darkVariant: String
    get() = selectedDarkVariant

  val lightVariant: String
    get() = selectedLightVariant

  init {
    preferredPacksGroup.add(Separator.create(PluginSettingsBundle.message("settings.sync.dark")))
    for (pack in DARK_VARIANTS) {
      preferredPacksGroup.add(SelectVariantAction(pack, isDark = true))
    }
    preferredPacksGroup.add(Separator.create(PluginSettingsBundle.message("settings.sync.light")))
    for (pack in LIGHT_VARIANTS) {
      preferredPacksGroup.add(SelectVariantAction(pack, isDark = false))
    }

    val presentation =
      Presentation().apply {
        icon = gearIcon
        text = PluginSettingsBundle.message("settings.sync.gear.tooltip")
        isPopupGroup = true
        isEnabled = state.syncWithOs
      }

    gearButton =
      ActionButton(
          preferredPacksGroup,
          presentation,
          ACTION_PLACE,
          ActionToolbar.DEFAULT_MINIMUM_BUTTON_SIZE,
        )
        .also { it.update() }

    syncCheckbox.addItemListener {
      val enabled = syncCheckbox.isSelected
      presentation.isEnabled = enabled
      gearButton.isEnabled = enabled
      gearButton.update()
      dropdown.isEnabled = !enabled
      if (enabled) {
        refreshMainDropdownFromLightDark()
      }
    }
    dropdown.isEnabled = !state.syncWithOs

    val controls =
      JPanel(FlowLayout(FlowLayout.LEADING, JBUI.scale(CONTROLS_HGAP), 0)).apply {
        isOpaque = false
        add(dropdown)
        add(syncCheckbox)
        add(gearButton)
      }

    val form =
      FormBuilder.createFormBuilder()
        .addLabeledComponent(
          JBLabel(PluginSettingsBundle.message("settings.variant")),
          controls,
          1,
          false,
        )
        .panel

    add(form)
    layout = FlowLayout(FlowLayout.LEADING)
  }

  private fun refreshMainDropdownFromLightDark() {
    val id = if (IdeTheme.isDark()) selectedDarkVariant else selectedLightVariant
    dropdown.selectedItem = Variant.values().find { it.id == id }
  }

  private fun variantCombo(selectedId: String): ComboBox<Variant> =
    ComboBox(Variant.values()).apply {
      selectedItem = Variant.values().find { it.id == selectedId } ?: Variant.MOCHA
      alignmentY = Component.CENTER_ALIGNMENT
    }

  private inner class SelectVariantAction(private val pack: Variant, private val isDark: Boolean) :
    ToggleAction(pack.toString()) {
    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.EDT

    override fun isSelected(e: AnActionEvent): Boolean =
      if (isDark) selectedDarkVariant == pack.id else selectedLightVariant == pack.id

    override fun setSelected(e: AnActionEvent, state: Boolean) {
      if (!state) return
      if (isDark) {
        selectedDarkVariant = pack.id
      } else {
        selectedLightVariant = pack.id
      }
      if (syncCheckbox.isSelected) {
        refreshMainDropdownFromLightDark()
      }
    }
  }

  companion object {
    private const val ACTION_PLACE = "CatppuccinIcons.PreferredIconPack"
    private const val CONTROLS_HGAP = 8

    private val DARK_VARIANTS = listOf(Variant.FRAPPE, Variant.MACCHIATO, Variant.MOCHA)

    private val LIGHT_VARIANTS = listOf(Variant.LATTE)

    private fun themedActionIcon(source: Icon): Icon =
      runCatching { IconUtil.colorize(source, JBUI.CurrentTheme.Label.foreground()) }
        .getOrDefault(source)
  }
}
