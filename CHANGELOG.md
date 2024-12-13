# Changelog

## [Unreleased]

### Added

### Changed

### Deprecated

### Removed

### Fixed

### Security

## 1.10.1 - 2024-12-13

### Changed

- Add code formatting with ktfmt
- Add static analysis with detekt
- Add tests
- Updated Gradle to v8.11.2
- Updated pnpm to v9.14.2
- Updated @types/node to v22.0.1
- Updated typescript to v5.7.2
- Updated tsx to v4.19.2

### Fixed

- Fixed K2 compatibility warning
- Moved to supported Gradle Action
- Fixed Python icons when python Core plugin is installed
- Fixed Go icons when Go Core plugin is installed

## 1.10.0 - 2024-10-13

### Added

- Add icon for `.razor` files.
- Upgrade submodule `vscode-icons` from v1.16.0 to v1.17.0 (
  See [vscode-icons CHANGELOG.md](https://github.com/catppuccin/vscode-icons/blob/main/CHANGELOG.md#v1170) for added
  icons and associations)

### Changed

- Update IntelliJ Platform plugin to 2.1.0 ([#125](https://github.com/catppuccin/jetbrains-icons/pull/125))

### Fixed

- Fix deprecation warnings for 2024.3 EAP ([#121](https://github.com/catppuccin/jetbrains-icons/pull/121))

## 1.9.0 - 2024-09-21

### Added

- Support 2024.3 EAP

### Fixed

- Fix plugin defect warnings by optionally loading Java
  icons  ([#116](https://github.com/catppuccin/jetbrains-icons/pull/116))
- Visibility Icons for Java Files are not hidden
  anymore ([#116](https://github.com/catppuccin/jetbrains-icons/pull/116))
- Static Classes for Java files now have the appropriate Static
  Mark ([#116](https://github.com/catppuccin/jetbrains-icons/pull/116))
- Accurately apply `Exception` icon on all classes extending `Exception` and
  `Throwable` ([#116](https://github.com/catppuccin/jetbrains-icons/pull/116))

## 1.8.0 - 2024-09-13

### Added

- Differentiate Java files (Annotation, Class, Enum, etc.) by colour and shape in the file tree. If you know how to
  implement these icons in the rest of the user interface, please reach out to us via the issue tracker or the
  Catppuccin discord!
- Upgrade submodule `vscode-icons` from v1.15.0 to v1.16.0 (
  See [vscode-icons CHANGELOG.md](https://github.com/catppuccin/vscode-icons/blob/main/CHANGELOG.md#v1160) for added
  icons and associations)

## 1.7.0 - 2024-09-08

### Added

- Upgrade submodule `vscode-icons` from v1.13.0 to v1.15.0 (
  See [vscode-icons CHANGELOG.md](https://github.com/catppuccin/vscode-icons/blob/main/CHANGELOG.md#v1150) for added
  icons and associations)

## 1.6.2 - 2024-08-13

### Fixed

- Make `org.jetbrains.kotlin` and `com.intellij.java` modules optional. This reintroduces support for IDEs outside
  IntelliJ, Android Studio & Aqua. ([#86](https://github.com/catppuccin/jetbrains-icons/pull/86))

## 1.6.1 - 2024-08-09

### Added

- Support 2024.2

### Fixed

- Lazily initialize PluginSettingsState in IconPack ([#81](https://github.com/catppuccin/jetbrains-icons/pull/81))
- Display correct coloured Java icons in editor tabs ([#68](https://github.com/catppuccin/jetbrains-icons/pull/68))

## 1.6.0 - 2024-06-22

### Added

- `Vital` icons
- `Apple` icons
- `Amber` icons
- `Fastlane` folder icons
- `Stata` icons
- `Caddy` folder icons

### Changed

- Updated Gradle Wrapper to 8.7
- Updated Kotlin Version to 2.0.0
- Update IntelliJ Platform plugin to 2.0.0-beta7
- Update vscode-icons submodule to v1.13.0

### Security

- Added Github Action to verify Gradle wrapper checksum

## 1.5.0 - 2024-05-19

### Added

- Different colors for Java filetypes (e.g. `Class`: Red, `Interface`: Green, `Record`: Mauve, `Enum`:
  Yellow and `Annotation`: Green) ([#35](https://github.com/catppuccin/jetbrains-icons/pull/35))
- Ability to disable different colors for Java filetypes in settings
  panel. ([#35](https://github.com/catppuccin/jetbrains-icons/pull/35))
- Add Docker icon to all files with string `Dockerfile` (e.g.
  `dev.Dockerfile`) ([#32](https://github.com/catppuccin/jetbrains-icons/pull/32))
- Add Dependency on the IntelliJ Java Module ([#40](https://github.com/catppuccin/jetbrains-icons/pull/40))

## 1.4.0 - 2024-02-27

### Added

- Brand new settings panel ([#27](https://github.com/catppuccin/jetbrains-icons/pull/27))
- Ability to disable Python support ([#27](https://github.com/catppuccin/jetbrains-icons/pull/27))

### Fixed

- Mapping for `html` association
- Mapping for directories named `py`

## 1.3.0 - 2024-02-25

### Added

- Python decorator support ([#26](https://github.com/catppuccin/jetbrains-icons/pull/26))

## 1.2.0 - 2024-02-11

### Added

- Php icon ([#73](https://github.com/catppuccin/vscode-icons/pull/73))
- Org icon ([#74](https://github.com/catppuccin/vscode-icons/pull/74))
- Evelenty icon ([#82](https://github.com/catppuccin/vscode-icons/pull/82))
- Astro icon ([#82](https://github.com/catppuccin/vscode-icons/pull/82))
- Hooks folder icon ([350fae5](https://github.com/catppuccin/vscode-icons/commit/350fae5))
- Intellij folder icon ([f099e5b](https://github.com/catppuccin/vscode-icons/commit/f099e5b))
- Devcontainer file/folder icons ([377704a](https://github.com/catppuccin/vscode-icons/commit/377704a))
- Adobe (ae, ai, id, ps, xd) file icons ([dc1a22c](https://github.com/catppuccin/vscode-icons/commit/dc1a22c))
- Puppeteer icon ([7212fcf](https://github.com/catppuccin/vscode-icons/commit/7212fcf))

### Changed

- Rework all existing icons for lower resolutions

## 1.1.1 - 2024-01-11

### Fixed

- `flake.lock` icon.
- `yarn` icon.
- `yarn.lock` icon.

## 1.1.0 - 2024-01-01

### Added

- `Dhall` icon.
- `Haml` icon.

### Changed

- Rework `yarn` icons for v4.

### Fixed

- Mapping for `vue` icon.

## 1.0.0 - 2023-10-29

🚀 Initial Release 🚀

## 0.0.0 - 2023-10-21
