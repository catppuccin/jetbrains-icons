package com.github.catppuccin.icons

import com.intellij.ide.IconProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiUtilCore
import javax.swing.Icon

class IconProvider : IconProvider() {
    override fun getIcon(element: PsiElement, flags: Int): Icon? {
        val file = PsiUtilCore.getVirtualFile(element)

        // switch cases
        return when {
            file?.name?.startsWith(".example") == true -> Icons.EXAMPLE
            else -> null
        }
    }
}
