package com.github.catppuccin.jetbrains_icons.util

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiClassType
import com.intellij.psi.PsiModifier

/**
 * Utilities for determining properties about a [PsiClass] in order to correctly display an icon.
 */
object PsiClassUtils {
  fun isAbstract(psiClass: PsiClass): Boolean =
    psiClass.isValid && psiClass.hasModifierProperty(PsiModifier.ABSTRACT)

  fun isSealed(psiClass: PsiClass): Boolean =
    psiClass.isValid && psiClass.hasModifierProperty(PsiModifier.SEALED)

  fun isFinal(psiClass: PsiClass): Boolean =
    psiClass.isValid && psiClass.hasModifierProperty(PsiModifier.FINAL)

  fun isStatic(psiClass: PsiClass): Boolean =
    psiClass.isValid && psiClass.hasModifierProperty(PsiModifier.STATIC)

  /** Returns true if the [psiClass] is an exception (inherits from an exception). */
  fun isException(psiClass: PsiClass): Boolean {
    if (psiClass.name.isNullOrEmpty()) return false
    return psiClass.isValid && extendsException(psiClass)
  }

  /** Returns true if the [psiClass] has package-private visibility. */
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
