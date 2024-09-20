package com.github.catppuccin.jetbrains_icons.util

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiModifier

object PsiClassUtils {
    fun isAbstract(psiClass: PsiClass): Boolean {
        return psiClass.isValid && psiClass.hasModifierProperty(PsiModifier.ABSTRACT)
    }

    fun isSealed(psiClass: PsiClass): Boolean {
        return psiClass.isValid && psiClass.hasModifierProperty(PsiModifier.SEALED)
    }

    fun isFinal(psiClass: PsiClass): Boolean {
        return psiClass.isValid && psiClass.hasModifierProperty(PsiModifier.FINAL)
    }

    fun isStatic(psiClass: PsiClass): Boolean {
        return psiClass.isValid && psiClass.hasModifierProperty(PsiModifier.STATIC)
    }

    fun isException(psiClass: PsiClass): Boolean {
        val className = psiClass.name
        if (className.isNullOrEmpty()) return false

        if (!psiClass.isValid) return false

        return psiClass.superTypes.any { it.className == "Throwable" || it.className == "Exception" }
            || className.endsWith("Exception")
    }

    fun isPackagePrivate(psiClass: PsiClass): Boolean {
        val modifierList = psiClass.modifierList
        return modifierList != null && !modifierList.hasModifierProperty(PsiModifier.PUBLIC) &&
            !modifierList.hasModifierProperty(PsiModifier.PROTECTED) &&
            !modifierList.hasModifierProperty(PsiModifier.PRIVATE)
    }

}
