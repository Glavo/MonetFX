# Changelog

## 0.4.0

(In development)

### New Features

- New API `ReadOnlyColorSchemeWrapper`. It just like `ReadOnlyXxxWrapper` classes in JavaFX.

### Theme Builder

- Improved support for the 2023 version of the Material 3 color system specification and the watch platform in MonetFX Theme Builder.

  Now, when switching the target platform to watch, the 2025 specification will be automatically selected and dark mode enabled;

  When switching the color style to one that does not support the 2025 specification, it will automatically switch to the 2021 specification.

## 0.3.0

Release Date: 2025-11-17

### New Features

- More Javadoc. 
  
  In this release, we've essentially added documentation for all API.

- New API `ColorScheme#getContrast()`, which returns a `Contrast` instance representing the contrast of the current `ColorScheme`.

  It replaces the old `ColorScheme#getContrastLevel()` method, which returned a `double`.

### Breaking changes

- In this version, we have removed `ColorScheme#getContrastLevel()`. Users should use `ColorScheme#getContrast()` to obtain the contrast.

## 0.2.0

Release Date: 2025-11-15

### New Features

- More Javadoc.

- Support for the 2025 color specification.

  We have added a new enum `ColorSpecVersion` to distinguish between specification versions. This enum contains two
  values:

    - `SPEC_2021`: Represents the old color specification. We still use this as the default value for now (this may
      change in the future).

    - `SPEC_2025`: Represents the new color specification.

  You can switch between different color specifications by calling the new API
  `ColorSchemeBuilder#setSpecVersion(ColorSpecVersion)`.

- Support for watch targets.

  We have added a new enum `TargetPlatform` to distinguish between target platforms. This enum contains two values:

    * `PHONE`: Represents regular platforms (such as PC, phone, tablet, etc.). This is the default option.
    * `WATCH`: Represents the watch platform.

  You can switch between different target platforms by calling the new API
  `ColorSchemeBuilder#setPlatform(TargetPlatform)`.

### Breaking changes

* In this version, we have made the constructor of `Contrast` private. If you need a custom contrast, you should use the
  new factory method `Contrast.of(double)`.

## 0.1.0

Release Date: 2025-03-23

Initial version.