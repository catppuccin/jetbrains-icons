package com.github.catppuccin.jetbrains_icons.decorators

import com.github.catppuccin.jetbrains_icons.IconPack
import com.github.catppuccin.jetbrains_icons.settings.PluginSettingsState
import com.github.catppuccin.jetbrains_icons.util.PsiClassUtils
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ProjectViewNodeDecorator
import com.intellij.lang.jvm.JvmModifier
import com.intellij.psi.PsiClass
import org.jetbrains.kotlin.idea.codeInsight.SuperDeclaration
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.isAbstract
import org.jetbrains.kotlin.psi.psiUtil.isObjectLiteral

class JavaProjectViewNodeDecorator : ProjectViewNodeDecorator {
    override fun decorate(node: ProjectViewNode<*>, data: PresentationData) {
        if (!PluginSettingsState.instance.javaSupport) return
        if (node.virtualFile?.isDirectory == true) return

        val icons = IconPack.instance.icons

        when (val value = node.value) {

            is PsiClass -> when {
                value.isInterface -> data.setIcon(icons.java_alt_1)
                value.isAnnotationType -> data.setIcon(icons.java_alt_1)
                value.isEnum -> data.setIcon(icons.java_alt_3)
                value.isRecord -> data.setIcon(icons.java_alt_2)
                PsiClassUtils.isAbstract(value) -> data.setIcon(icons.java_alt_1)
            }
        }
    }
}
