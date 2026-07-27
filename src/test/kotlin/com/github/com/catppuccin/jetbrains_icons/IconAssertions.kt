package com.github.com.catppuccin.jetbrains_icons

import com.intellij.ui.icons.IconPathProvider
import javax.swing.Icon
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull

/**
 * Asserts that [actual] is the icon loaded from the same resource as [expected], comparing the
 * paths they resolve to rather than the icon instances.
 *
 * Instance comparison is not reliable here. The tests build their own [Icons] instance while the
 * providers use the one held by `IconPack`, so an assertion always compares two separately loaded
 * icons, and `CachedImageIcon` does not treat two icons loaded from the same path as equal. The
 * path is what the assertions actually care about: which SVG the provider picked.
 */
fun assertSameIcon(expected: Icon, actual: Icon?) {
  val expectedPath = (expected as? IconPathProvider)?.originalPath
  assertNotNull(expectedPath, "Expected icon does not expose a path: $expected")

  val actualPath = (actual as? IconPathProvider)?.originalPath
  assertEquals(expectedPath, actualPath, "Provider returned the wrong icon (actual: $actual)")
}
