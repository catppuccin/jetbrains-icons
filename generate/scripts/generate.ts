import {mkdirSync, readdirSync, readFileSync, writeFileSync} from 'fs';
import {extensions, files, folders} from "../vscode-icons/src/associations";
import {catppuccinizeSvg} from "../vscode-icons/scripts/catppuccinize";
import {optimizeSvg} from "../vscode-icons/scripts/optimize";

function generateIcons() {
  mkdirSync('src/main/resources/icons', {recursive: true});
  readdirSync('generate/vscode-icons/src/icons').forEach((file) => {
    if (!file.endsWith('.svg')) {
      return
    }

    let data = readFileSync(`generate/vscode-icons/src/icons/${file}`, {encoding: 'utf-8'})
    data = data.replace("width=\"100.0px\"", "width=\"16.0px\"")
    data = data.replace("height=\"100.0px\"", "height=\"16.0px\"")
    data = catppuccinizeSvg(data)
    data = optimizeSvg(data)
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
    data += `\n`
  })

  // Folders to Icons
  data += `\n    val FOLDER_TO_ICONS = mapOf(\n`
  Object.entries(folders).forEach(([key, value]: [string, string[]]) => {
    value.forEach((folder) => {
      data += `        "${folder}" to ${key.toUpperCase()},\n`
    })
  })
  data += `    )\n`

  // File name to Icons
  data += `\n    val FILE_TO_ICONS = mapOf(\n`
  Object.entries(files).forEach(([key, value]: [string, string[]]) => {
    value.forEach((filename) => {
      data += `        "${filename}" to ${key.toUpperCase()},\n`
    })
  })
  data += `    )\n`

  // Extensions to Icons
  data += `\n    val EXT_TO_ICONS = mapOf(\n`
  Object.entries(extensions).forEach(([key, value]: [string, string[]]) => {
    value.forEach((ext) => {
      data += `        "${ext}" to ${key.toUpperCase()},\n`
    })
  })
  data += `    )\n`

  data += `\n}\n`

  writeFileSync(`src/main/kotlin/com/github/catppuccin/icons/Icons.kt`, data, {encoding: 'utf-8'})
}

generateIcons()
generateIconsKt()
