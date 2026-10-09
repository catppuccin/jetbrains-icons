package com.github.catppuccin.jetbrains_icons.util

import com.intellij.ui.JBColor

object IdeTheme {
  fun isDark(): Boolean = !JBColor.isBright()
}
