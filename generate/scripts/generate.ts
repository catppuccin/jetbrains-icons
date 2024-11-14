import {mkdirSync, readdirSync, readFileSync, writeFileSync} from 'fs';
import {fileNames, fileExtensions} from "@/defaults/fileIcons";
import {folderNames} from "@/defaults/folderIcons";

function generateIcons(variant: string) {
  mkdirSync('src/main/resources/icons', {recursive: true});
  readdirSync(`generate/vscode-icons/icons/${variant}`).forEach((file) => {
    if (!file.endsWith('.svg')) {
      return
    }
    let data = readFileSync(`generate/vscode-icons/icons/${variant}/${file}`, {encoding: 'utf-8'})
    writeFileSync(`src/main/resources/icons/${variant}_${file}`, data, {encoding: 'utf-8'})
  });
}

// VSCode sometimes doesn't need the `fileExtension` block to be populated but that affects JetBrains so we can add our
// own extensions/overrides here if need be.
const customFileExtensions = {razor: "razor"}
const extendedFileExtensions = {...fileExtensions, ...customFileExtensions}

function generateIconsKt() {
  let data = `package com.github.catppuccin.jetbrains_icons.commons

import com.intellij.openapi.util.IconLoader

class Icons(private val variant: String) {`

  readdirSync('generate/vscode-icons/icons/latte').forEach((file) => {
    if (!file.endsWith('.svg')) {
      return
    }

    data += `\n    @JvmField`
    data += `\n    val ${file.replace('.svg', '').replaceAll('-', '_')} = IconLoader.getIcon("/icons/" + variant + "_${file}", javaClass)`
    data += `\n`
  })

  // Folders to Icons
  data += `\n    val FOLDER_TO_ICONS = mapOf(\n`
  Object.entries(folderNames).forEach(([key, value]: [string, string]) => {
    data += `        "${key}" to ${value.replaceAll('-', '_')},\n`
  })
  data += `    )\n`

  // File name to Icons
  data += `\n    val FILE_TO_ICONS = mapOf(\n`
  Object.entries(fileNames).forEach(([key, value]: [string, string]) => {
    data += `        "${key}" to ${value.replaceAll('-', '_')},\n`
  })
  data += `    )\n`

  // Extensions to Icons
  data += `\n    val EXT_TO_ICONS = mapOf(\n`
  Object.entries(extendedFileExtensions).forEach(([key, value]: [string, string]) => {
    data += `        "${key}" to ${value.replaceAll('-', '_')},\n`
  })
  data += `    )\n`

  data += `\n}\n`

  // writeFileSync(`src/main/kotlin/com/github/catppuccin/jetbrains_icons/Icons.kt`, data, {encoding: 'utf-8'})
  writeFileSync(`commons/src/main/kotlin/com/github/catppuccin/jetbrains_icons/commons/Icons.kt`, data, {encoding: 'utf-8'})
}

['latte', 'frappe', 'macchiato', 'mocha'].forEach(generateIcons)
generateIconsKt()
