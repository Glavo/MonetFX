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

import org.glavo.monetfx.internal.dislike.DislikeAnalyzer;
import org.glavo.monetfx.internal.dynamiccolor.DynamicScheme;
import org.glavo.monetfx.internal.hct.Hct;
import org.glavo.monetfx.internal.palettes.TonalPalette;
import org.glavo.monetfx.internal.temperature.TemperatureCache;
import org.glavo.monetfx.internal.utils.MathUtils;

import java.util.Locale;

import static java.lang.Math.max;

/// The algorithm used to construct a [ColorScheme] in [ColorScheme#fromSeed].
///
/// The [#TONAL_SPOT] style builds default Material scheme colors. These colors are
/// mapped to light or dark tones to achieve visually accessible color
/// pairings with sufficient contrast between foreground and background elements.
///
/// In some cases, the tones can prevent colors from appearing as intended,
/// such as when a color is too light to offer enough contrast for accessibility.
/// Color fidelity (`ColorStyle.FIDELITY`) is a feature that adjusts
/// tones in these cases to produce the intended visual results without harming
/// visual contrast.
public enum ColorStyle {
    /// Default for Material theme colors. Builds pastel palettes with a low chroma.
    TONAL_SPOT {
        @Override
        TonalPalette getPrimaryPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 36.0);
        }

        @Override
        TonalPalette getSecondaryPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 16.0);
        }

        @Override
        TonalPalette getTertiaryPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(MathUtils.sanitizeDegreesDouble(sourceColorHct.getHue() + 60.0), 24.0);
        }

        @Override
        TonalPalette getNeutralPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 6.0);
        }

        @Override
        TonalPalette getNeutralVariantPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 8.0);
        }
    },

    /// The resulting color palettes match seed color, even if the seed color
    /// is very bright (high chroma).
    FIDELITY {
        @Override
        TonalPalette getPrimaryPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), sourceColorHct.getChroma());
        }

        @Override
        TonalPalette getSecondaryPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), Math.max(sourceColorHct.getChroma() - 32.0, sourceColorHct.getChroma() * 0.5));
        }

        @Override
        TonalPalette getTertiaryPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHct(DislikeAnalyzer.fixIfDisliked(new TemperatureCache(sourceColorHct).getComplement()));
        }

        @Override
        TonalPalette getNeutralPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), sourceColorHct.getChroma() / 8.0);
        }

        @Override
        TonalPalette getNeutralVariantPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), (sourceColorHct.getChroma() / 8.0) + 4.0);
        }
    },

    /// All colors are grayscale, no chroma.
    MONOCHROME {
        @Override
        TonalPalette getPrimaryPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 0.0);
        }

        @Override
        TonalPalette getSecondaryPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 0.0);
        }

        @Override
        TonalPalette getTertiaryPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 0.0);
        }

        @Override
        TonalPalette getNeutralPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 0.0);
        }

        @Override
        TonalPalette getNeutralVariantPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 0.0);
        }
    },

    /// Close to grayscale, a hint of chroma.
    NEUTRAL {
        @Override
        TonalPalette getPrimaryPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 12.0);
        }

        @Override
        TonalPalette getSecondaryPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 8.0);
        }

        @Override
        TonalPalette getTertiaryPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 16.0);
        }

        @Override
        TonalPalette getNeutralPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 2.0);
        }

        @Override
        TonalPalette getNeutralVariantPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 2.0);
        }
    },

    /// Pastel colors, high chroma palettes. The primary palette's chroma is at
    /// maximum. Use `fidelity` instead if tokens should alter their tone to match
    /// the palette vibrancy.
    VIBRANT {
        private final double[] HUES = {0, 41, 61, 101, 131, 181, 251, 301, 360};
        private final double[] SECONDARY_ROTATIONS = {18, 15, 10, 12, 15, 18, 15, 12, 12};
        private final double[] TERTIARY_ROTATIONS = {35, 30, 20, 25, 30, 35, 30, 25, 25};

        @Override
        TonalPalette getPrimaryPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 200.0);
        }

        @Override
        TonalPalette getSecondaryPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(DynamicScheme.getRotatedHue(sourceColorHct, HUES, SECONDARY_ROTATIONS), 24.0);
        }

        @Override
        TonalPalette getTertiaryPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(DynamicScheme.getRotatedHue(sourceColorHct, HUES, TERTIARY_ROTATIONS), 32.0);
        }

        @Override
        TonalPalette getNeutralPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 10.0);
        }

        @Override
        TonalPalette getNeutralVariantPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 12.0);
        }
    },

    /// Pastel colors, medium chroma palettes. The primary palette's hue is
    /// different from the seed color, for variety.
    EXPRESSIVE {
        private final double[] HUES = {0, 21, 51, 121, 151, 191, 271, 321, 360};
        private final double[] SECONDARY_ROTATIONS = {45, 95, 45, 20, 45, 90, 45, 45, 45};
        private final double[] TERTIARY_ROTATIONS = {120, 120, 20, 45, 20, 15, 20, 120, 120};

        @Override
        TonalPalette getPrimaryPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(MathUtils.sanitizeDegreesDouble(sourceColorHct.getHue() + 240.0), 40.0);
        }

        @Override
        TonalPalette getSecondaryPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(DynamicScheme.getRotatedHue(sourceColorHct, HUES, SECONDARY_ROTATIONS), 24.0);
        }

        @Override
        TonalPalette getTertiaryPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(DynamicScheme.getRotatedHue(sourceColorHct, HUES, TERTIARY_ROTATIONS), 32.0);
        }

        @Override
        TonalPalette getNeutralPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(MathUtils.sanitizeDegreesDouble(sourceColorHct.getHue() + 15.0), 8.0);
        }

        @Override
        TonalPalette getNeutralVariantPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(MathUtils.sanitizeDegreesDouble(sourceColorHct.getHue() + 15.0), 12.0);
        }
    },

    /// Almost identical to `fidelity`. Tokens and palettes match the seed color.
    /// [ColorScheme#getPrimaryContainer] is the seed color, adjusted to ensure
    /// contrast with surfaces. The tertiary palette is analogue of the seed color.
    CONTENT {
        @Override
        TonalPalette getPrimaryPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), sourceColorHct.getChroma());
        }

        @Override
        TonalPalette getSecondaryPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), max(sourceColorHct.getChroma() - 32.0, sourceColorHct.getChroma() * 0.5));
        }

        @Override
        TonalPalette getTertiaryPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHct(DislikeAnalyzer.fixIfDisliked(new TemperatureCache(sourceColorHct).getAnalogousColors(3, 6).get(2)));
        }

        @Override
        TonalPalette getNeutralPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), sourceColorHct.getChroma() / 8.0);
        }

        @Override
        TonalPalette getNeutralVariantPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), (sourceColorHct.getChroma() / 8.0) + 4.0);
        }
    },

    /// A playful theme - the seed color's hue does not appear in the theme.
    RAINBOW {
        @Override
        TonalPalette getPrimaryPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 48.0);
        }

        @Override
        TonalPalette getSecondaryPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 16.0);
        }

        @Override
        TonalPalette getTertiaryPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(
                    MathUtils.sanitizeDegreesDouble(sourceColorHct.getHue() + 60.0), 24.0);
        }

        @Override
        TonalPalette getNeutralPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 0.0);
        }

        @Override
        TonalPalette getNeutralVariantPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 0.0);
        }
    },

    /// A playful theme - the seed color's hue does not appear in the theme.
    FRUIT_SALAD {
        @Override
        TonalPalette getPrimaryPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(
                    MathUtils.sanitizeDegreesDouble(sourceColorHct.getHue() - 50.0), 48.0);
        }

        @Override
        TonalPalette getSecondaryPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(
                    MathUtils.sanitizeDegreesDouble(sourceColorHct.getHue() - 50.0), 36.0);
        }

        @Override
        TonalPalette getTertiaryPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 36.0);
        }

        @Override
        TonalPalette getNeutralPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 10.0);
        }

        @Override
        TonalPalette getNeutralVariantPalette(Hct sourceColorHct, boolean isDark, double contrastLevel) {
            return TonalPalette.fromHueAndChroma(sourceColorHct.getHue(), 16.0);
        }
    };

    private final String displayName;

    ColorStyle() {
        String[] parts = this.name().split("_");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].charAt(0) + parts[i].substring(1).toLowerCase(Locale.ROOT);
        }
        this.displayName = String.join(" ", parts);
    }

    abstract TonalPalette getPrimaryPalette(Hct sourceColorHct, boolean isDark, double contrastLevel);

    abstract TonalPalette getSecondaryPalette(Hct sourceColorHct, boolean isDark, double contrastLevel);

    abstract TonalPalette getTertiaryPalette(Hct sourceColorHct, boolean isDark, double contrastLevel);

    abstract TonalPalette getNeutralPalette(Hct sourceColorHct, boolean isDark, double contrastLevel);

    abstract TonalPalette getNeutralVariantPalette(Hct sourceColorHct, boolean isDark, double contrastLevel);

    @Override
    public String toString() {
        return displayName;
    }
}
