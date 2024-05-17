package com.github.catppuccin.jetbrains_icons.util

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiModifier

object PsiClassUtils {
    fun isAbstract(psiClass: PsiClass): Boolean {
        return psiClass.hasModifierProperty(PsiModifier.ABSTRACT)
    }
}
