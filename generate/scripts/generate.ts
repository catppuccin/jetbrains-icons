import {mkdirSync, readdirSync, readFileSync, writeFileSync} from 'fs';

function generateIcons() {
  mkdirSync('src/main/resources/icons', {recursive: true});
  readdirSync('generate/vscode-icons/src/icons').forEach((file) => {
    if (!file.endsWith('.svg')) {
      return
    }

    let data = readFileSync(`generate/vscode-icons/src/icons/${file}`, {encoding: 'utf-8'});
    data = data.replace("width=\"100.0px\"", "width=\"16.0px\"");
    data = data.replace("height=\"100.0px\"", "height=\"16.0px\"");
    writeFileSync(`src/main/resources/icons/${file}`, data, {encoding: 'utf-8'})
  });
}

function generateIconsKt() {
  let data = `package com.github.catppuccin.icons`
  data += `\n\n`
  data += `import com.intellij.openapi.util.IconLoader`
  data += `\n\n`
  data += `object Icons {`

  readdirSync('src/main/resources/icons').forEach((file) => {
    if (!file.endsWith('.svg')) {
      return
    }

    data += `\n    @JvmField`
    data += `\n    val ${file.replace('.svg', '').toUpperCase()} = IconLoader.getIcon("/icons/${file}", javaClass)`
  })

  data += `\n}\n`

  writeFileSync(`src/main/kotlin/com/github/catppuccin/icons/Icons.kt`, data, {encoding: 'utf-8'})
}

generateIcons();
generateIconsKt()
