/*
 * Copyright 2025 Glavo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.glavo.monetfx;

/// The algorithm used to construct a [ColorScheme] in [ColorScheme#fromSeed].
///
/// The [#TONAL_SPOT] variant builds default Material scheme colors. These colors are
/// mapped to light or dark tones to achieve visually accessible color
/// pairings with sufficient contrast between foreground and background elements.
///
/// In some cases, the tones can prevent colors from appearing as intended,
/// such as when a color is too light to offer enough contrast for accessibility.
/// Color fidelity (`DynamicSchemeVariant.fidelity`) is a feature that adjusts
/// tones in these cases to produce the intended visual results without harming
/// visual contrast.
public enum DynamicSchemeVariant {
    /// Default for Material theme colors. Builds pastel palettes with a low chroma.
    TONAL_SPOT,

    /// The resulting color palettes match seed color, even if the seed color
    /// is very bright (high chroma).
    FIDELITY,

    /// All colors are grayscale, no chroma.
    MONOCHROME,

    /// Close to grayscale, a hint of chroma.
    NEUTRAL,

    /// Pastel colors, high chroma palettes. The primary palette's chroma is at
    /// maximum. Use `fidelity` instead if tokens should alter their tone to match
    /// the palette vibrancy.
    VIBRANT,

    /// Pastel colors, medium chroma palettes. The primary palette's hue is
    /// different from the seed color, for variety.
    EXPRESSIVE,

    /// Almost identical to `fidelity`. Tokens and palettes match the seed color.
    /// [ColorScheme#getPrimaryContainer] is the seed color, adjusted to ensure
    /// contrast with surfaces. The tertiary palette is analogue of the seed color.
    CONTENT,

    /// A playful theme - the seed color's hue does not appear in the theme.
    RAINBOW,

    /// A playful theme - the seed color's hue does not appear in the theme.
    FRUIT_SALAD,
}
