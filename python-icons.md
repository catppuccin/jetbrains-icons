### Findings on Custom Icon Implementation in PyCharm

#### Project View Tree Icons
- **Working Solution**: Using a `ProjectViewNodeDecorator` successfully changes the icons in the Project View Tree.
- **Implementation**: The `ProjectViewNodeDecorator` interface allows for custom decoration of nodes in the Project View Tree, including setting custom icons.

#### Breadcrumbs Icons
- **Issue**: Custom icons for breadcrumbs at the bottom of the IDE are not being applied.
- **Attempted Solutions**:
  - **`IconProvider`**: Changed the icons in the tabs on the top of opened files.
  - **`FileIconProvider`**: Did not achieve the desired effect.
  - **`FileIconPatcher`**: Did not successfully patch the icons for breadcrumbs.

#### Next Steps
- **To Try**: Implementing a `LineMarkerProvider` to change the icons for Python files.

### Implementation of `LineMarkerProvider`

#### Step 1: Implement `PythonLineMarkerProvider`

```kotlin
package com.github.catppuccin.jetbrains_icons.pycharm.providers

import com.github.catppuccin.jetbrains_icons.commons.IconPack.icons
import com.github.catppuccin.jetbrains_icons.commons.settings.PluginSettingsState
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiUtilCore
import javax.swing.Icon

class PythonLineMarkerProvider : LineMarkerProvider {
  override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
    if (!PluginSettingsState.instance.pythonSupport) return null
    val virtualFile = PsiUtilCore.getVirtualFile(element) ?: return null
    if (virtualFile.extension != "py") return null

    return LineMarkerInfo(
      element,
      element.textRange,
      icons.python,
      { "Python file" },
      null,
      LineMarkerInfo.LineMarkerGutterIconRenderer.Alignment.LEFT
    )
  }
}
```

#### Step 2: Register `PythonLineMarkerProvider` in `plugin.xml`

```xml
<idea-plugin>
  <extensions defaultExtensionNs="com.intellij">
    <lineMarkerProvider
      implementation="com.github.catppuccin.jetbrains_icons.pycharm.providers.PythonLineMarkerProvider"
      id="PythonLineMarkerProvider"
    />
  </extensions>
</idea-plugin>
```

This should ensure that your custom icons are used for Python files in the Project View Tree and breadcrumbs.
