package com.github.catppuccin.jetbrains_icons.util

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiClassType
import com.intellij.psi.PsiModifier

object PsiClassUtils {
  fun isAbstract(psiClass: PsiClass): Boolean =
    psiClass.isValid && psiClass.hasModifierProperty(PsiModifier.ABSTRACT)

  fun isSealed(psiClass: PsiClass): Boolean =
    psiClass.isValid && psiClass.hasModifierProperty(PsiModifier.SEALED)

  fun isFinal(psiClass: PsiClass): Boolean =
    psiClass.isValid && psiClass.hasModifierProperty(PsiModifier.FINAL)

  fun isStatic(psiClass: PsiClass): Boolean =
    psiClass.isValid && psiClass.hasModifierProperty(PsiModifier.STATIC)

  fun isException(psiClass: PsiClass): Boolean {
    val className = psiClass.name
    if (className.isNullOrEmpty()) return false
    if (!psiClass.isValid) return false
    return extendsException(psiClass)
  }

  fun isPackagePrivate(psiClass: PsiClass): Boolean {
    val modifierList = psiClass.modifierList
    return modifierList != null &&
      !modifierList.hasModifierProperty(PsiModifier.PUBLIC) &&
      !modifierList.hasModifierProperty(PsiModifier.PROTECTED) &&
      !modifierList.hasModifierProperty(PsiModifier.PRIVATE)
  }

  private fun extendsException(psiClass: PsiClass): Boolean =
    generateSequence(psiClass) { it.superClass }
      .flatMap { it.superTypes.asSequence() }
      .filterIsInstance<PsiClassType>()
      .any { it.name in setOf("Exception", "Throwable") }
}
