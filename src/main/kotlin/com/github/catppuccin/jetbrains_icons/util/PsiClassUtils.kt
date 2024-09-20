package com.github.catppuccin.jetbrains_icons.util

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiModifier

object PsiClassUtils {
    fun isAbstract(psiClass: PsiClass): Boolean {
        return psiClass.hasModifierProperty(PsiModifier.ABSTRACT)
    }

    fun isSealed(psiClass: PsiClass): Boolean {
        return psiClass.hasModifierProperty(PsiModifier.SEALED)
    }

    fun isFinal(psiClass: PsiClass): Boolean {
        return psiClass.hasModifierProperty(PsiModifier.FINAL)
    }

    fun isStatic(psiClass: PsiClass): Boolean {
        return psiClass.hasModifierProperty(PsiModifier.STATIC)
    }

    fun isException(psiClass: PsiClass): Boolean {
        return psiClass.name!!.endsWith("Exception")
    }
}
