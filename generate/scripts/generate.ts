import {mkdirSync, readdirSync, readFileSync, writeFileSync} from 'fs';
import {fileIcons} from "@/defaults/fileIcons";
import {folderIcons} from "@/defaults/folderIcons";

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

function generateIconsKt() {
  let data = `package com.github.catppuccin.jetbrains_icons

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
  Object.entries(folderIcons).forEach(([key, value]: [string, object]) => {
    value["folderNames"].forEach((folder: string) => {
      data += `        "${folder}" to folder_${key.replaceAll('-', '_')},\n`
    })
  })
  data += `    )\n`

  // File name to Icons
  data += `\n    val FILE_TO_ICONS = mapOf(\n`
  Object.entries(fileIcons).forEach(([key, value]: [string, object]) => {
    value["fileNames"]?.forEach((filename: string) => {
      data += `        "${filename}" to ${key.replaceAll('-', '_')},\n`
    })
  })
  data += `    )\n`

  // Extensions to Icons
  data += `\n    val EXT_TO_ICONS = mapOf(\n`
  Object.entries(fileIcons).forEach(([key, value]: [string, object]) => {
    value["fileExtensions"]?.forEach((ext: string) => {
      data += `        "${ext}" to ${key.replaceAll('-', '_')},\n`
    })
  })
  data += `    )\n`

  data += `\n}\n`

  writeFileSync(`src/main/kotlin/com/github/catppuccin/jetbrains_icons/Icons.kt`, data, {encoding: 'utf-8'})
}

['latte', 'frappe', 'macchiato', 'mocha'].forEach(generateIcons)
generateIconsKt()
