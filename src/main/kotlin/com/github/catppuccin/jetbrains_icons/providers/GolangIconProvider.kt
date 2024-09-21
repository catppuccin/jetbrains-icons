package com.github.catppuccin.jetbrains_icons.providers

import com.github.catppuccin.jetbrains_icons.IconPack
import com.github.catppuccin.jetbrains_icons.icon.BottomAlignedIcon
import com.github.catppuccin.jetbrains_icons.settings.PluginSettingsState
import com.intellij.icons.AllIcons
import com.intellij.ide.IconProvider
import com.intellij.openapi.util.Iconable
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiUtilCore
import javax.swing.Icon

class GolangIconProvider : IconProvider() {

    private val icons = IconPack.instance.icons

    override fun getIcon(element: PsiElement, @Iconable.IconFlags flags: Int): Icon? {
        if (!PluginSettingsState.instance.goSupport) return icons.go

        val virtualFile = PsiUtilCore.getVirtualFile(element) ?: return null
        if (!virtualFile.name.endsWith(".go")) return null

        val baseIcon: Icon = icons.go
        val testIcon: Icon = AllIcons.Nodes.JunitTestMark

        if (virtualFile.name.endsWith("_test.go")) {
            return BottomAlignedIcon(baseIcon, testIcon, 1.2)
        }

        return baseIcon
    }

}
