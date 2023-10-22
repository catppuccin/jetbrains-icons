import {mkdirSync, readdirSync, readFileSync, writeFileSync} from 'fs';
import {extensions, files, folders} from "@/associations";
import {catppuccinizeSvg} from "../vscode-icons/scripts/catppuccinize";
import {optimizeSvg} from "../vscode-icons/scripts/optimize";
import {CattppucinVariant, cssVarStyleTags, hexToVar, varToHex} from "@/palettes";

function generateIcons(variant: CattppucinVariant) {
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
    data = injectPalette(data, variant)
    writeFileSync(`src/main/resources/icons/${variant}_${file}`, data, {encoding: 'utf-8'})
  });
}

function generateIconsKt() {
  let data = `package com.github.catppuccin.icons`
  data += `\n\n`
  data += `import com.intellij.openapi.util.IconLoader`
  data += `\n\n`
  data += `class Icons(private val variant: String) {`

  readdirSync('generate/vscode-icons/src/icons').forEach((file) => {
    if (!file.endsWith('.svg')) {
      return
    }

    data += `\n    @JvmField`
    data += `\n    val ${file.replace('.svg', '')} = IconLoader.getIcon("/icons/" + variant + "_${file}", javaClass)`
    data += `\n`
  })

  // Folders to Icons
  data += `\n    val FOLDER_TO_ICONS = mapOf(\n`
  Object.entries(folders).forEach(([key, value]: [string, string[]]) => {
    value.forEach((folder) => {
      data += `        "${folder}" to ${key},\n`
    })
  })
  data += `    )\n`

  // File name to Icons
  data += `\n    val FILE_TO_ICONS = mapOf(\n`
  Object.entries(files).forEach(([key, value]: [string, string[]]) => {
    value.forEach((filename) => {
      data += `        "${filename}" to ${key},\n`
    })
  })
  data += `    )\n`

  // Extensions to Icons
  data += `\n    val EXT_TO_ICONS = mapOf(\n`
  Object.entries(extensions).forEach(([key, value]: [string, string[]]) => {
    value.forEach((ext) => {
      data += `        "${ext}" to ${key},\n`
    })
  })
  data += `    )\n`

  data += `\n}\n`

  writeFileSync(`src/main/kotlin/com/github/catppuccin/icons/Icons.kt`, data, {encoding: 'utf-8'})
}

// Modified version of injectPalette from github.com/catppuccin/vscode-icons/scripts/catppuccinize.ts:
export function injectPalette(svg: string, variant: CattppucinVariant) {
  const replaced = svg.replaceAll(/#([a-dA-F0-9]{6})/gi, m => {
    const v = hexToVar.mocha[m]
    return varToHex[variant][v]
  })
  return replaced.replace(/\n/, cssVarStyleTags.mocha)
}

['latte', 'frappe', 'macchiato', 'mocha'].forEach((variant: CattppucinVariant) => {
  generateIcons(variant)
})
generateIconsKt()
