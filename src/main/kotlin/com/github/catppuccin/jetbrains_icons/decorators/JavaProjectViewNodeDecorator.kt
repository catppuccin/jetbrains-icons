package com.github.catppuccin.jetbrains_icons.decorators

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ProjectViewNodeDecorator
import org.jetbrains.kotlin.psi.*

class JavaProjectViewNodeDecorator : ProjectViewNodeDecorator {
    override fun decorate(node: ProjectViewNode<*>, data: PresentationData) {
        if (node.virtualFile?.isDirectory == true) return

        val value = node.value
        when (value) {
            is KtClass -> when {
                // value.isInterface() ->
                // value.isEnum() ->
                // value.isAnnotation() ->
                // value.isSealed() ->
                // else ->
            }
        }
    }
}
