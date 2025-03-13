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

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import org.glavo.monetfx.internal.dynamiccolor.DynamicScheme;
import org.glavo.monetfx.internal.hct.Hct;
import org.glavo.monetfx.internal.quantize.QuantizerCelebi;
import org.glavo.monetfx.internal.scheme.SchemeContent;
import org.glavo.monetfx.internal.scheme.SchemeExpressive;
import org.glavo.monetfx.internal.scheme.SchemeFidelity;
import org.glavo.monetfx.internal.scheme.SchemeFruitSalad;
import org.glavo.monetfx.internal.scheme.SchemeMonochrome;
import org.glavo.monetfx.internal.scheme.SchemeNeutral;
import org.glavo.monetfx.internal.scheme.SchemeRainbow;
import org.glavo.monetfx.internal.scheme.SchemeTonalSpot;
import org.glavo.monetfx.internal.scheme.SchemeVibrant;
import org.glavo.monetfx.internal.score.Score;
import org.glavo.monetfx.internal.utils.ColorUtils;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// See https://github.com/flutter/flutter/blob/5491c8c146441d3126aff91beaa3fb5df6d710d0/packages/flutter/lib/src/material/color_scheme.dart

/// A set of 45 colors based on the
/// [Material spec](https://m3.material.io/styles/color/the-color-system/color-roles)
/// that can be used to configure the color properties of most components.
///
/// ### Colors in Material 3
///
/// In Material 3, colors are represented using color roles and
/// corresponding tokens. Each property in the [ColorScheme] class
/// represents one color role as defined in the spec above.
///
/// The main accent color groups in the scheme are [#primary], [#secondary],
/// and [#tertiary].
///
/// * Primary colors are used for key components across the UI, such as the FAB,
///   prominent buttons, and active states.
///
/// * Secondary colors are used for less prominent components in the UI, such as
///   filter chips, while expanding the opportunity for color expression.
///
/// * Tertiary colors are used for contrasting accents that can be used to
///   balance primary and secondary colors or bring heightened attention to
///   an element, such as an input field. The tertiary colors are left
///   for makers to use at their discretion and are intended to support
///   broader color expression in products.
///
/// Each accent color group (primary, secondary and tertiary) includes '-Fixed'
/// '-Dim' color roles, such as [#primaryFixed] and [#primaryFixedDim]. Fixed roles
/// are appropriate to use in places where Container roles are normally used,
/// but they stay the same color between light and dark themes. The '-Dim' roles
/// provide a stronger, more emphasized color with the same fixed behavior.
///
/// The remaining colors of the scheme are composed of neutral colors used for
/// backgrounds and surfaces, as well as specific colors for errors, dividers
/// and shadows. Surface colors are used for backgrounds and large, low-emphasis
/// areas of the screen.
///
/// Material 3 also introduces tone-based surfaces and surface containers.
/// They replace the old opacity-based model which applied a tinted overlay on
/// top of surfaces based on their elevation. These colors include: [#surfaceBright],
/// [#surfaceDim], [#surfaceContainerLowest], [#surfaceContainerLow], [#surfaceContainer],
/// [#surfaceContainerHigh], and [#surfaceContainerHighest].
///
/// Many of the colors have matching 'on' colors, which are used for drawing
/// content on top of the matching color. For example, if something is using
/// [#primary] for a background color, [#onPrimary] would be used to paint text
/// and icons on top of it. For this reason, the 'on' colors should have a
/// contrast ratio with their matching colors of at least 4.5:1 in order to
/// be readable. On '-FixedVariant' roles, such as [#onPrimaryFixedVariant],
/// also have the same color between light and dark themes, but compared
/// with on '-Fixed' roles, such as [#onPrimaryFixed], they provide a
/// lower-emphasis option for text and icons.
public final class ColorScheme {

    private static int getArgbFromAbgr(int abgr) {
        final int exceptRMask = 0xFF00FFFF;
        final int onlyRMask = ~exceptRMask;
        final int exceptBMask = 0xFFFFFF00;
        final int onlyBMask = ~exceptBMask;
        final int r = (abgr & onlyRMask) >> 16;
        final int b = abgr & onlyBMask;
        return (abgr & exceptRMask & exceptBMask) | (b << 16) | r;
    }

    private static DynamicScheme buildDynamicScheme(
            Brightness brightness,
            Color seedColor,
            DynamicSchemeVariant schemeVariant,
            double contrastLevel
    ) {
        assert contrastLevel >= -1.0 && contrastLevel <= 1.0 : "contrastLevel must be between -1.0 and 1.0 inclusive.";

        final boolean isDark = brightness == Brightness.DARK;
        final Hct sourceColor = Hct.fromInt(ColorUtils.argbFromFx(seedColor));
        switch (schemeVariant) {
            case TONAL_SPOT:
                return new SchemeTonalSpot(sourceColor, isDark, contrastLevel);
            case FIDELITY:
                return new SchemeFidelity(sourceColor, isDark, contrastLevel);
            case CONTENT:
                return new SchemeContent(sourceColor, isDark, contrastLevel);
            case MONOCHROME:
                return new SchemeMonochrome(sourceColor, isDark, contrastLevel);
            case NEUTRAL:
                return new SchemeNeutral(sourceColor, isDark, contrastLevel);
            case VIBRANT:
                return new SchemeVibrant(sourceColor, isDark, contrastLevel);
            case EXPRESSIVE:
                return new SchemeExpressive(sourceColor, isDark, contrastLevel);
            case RAINBOW:
                return new SchemeRainbow(sourceColor, isDark, contrastLevel);
            case FRUIT_SALAD:
                return new SchemeFruitSalad(sourceColor, isDark, contrastLevel);
            default:
                throw new AssertionError("Unknown scheme variant " + schemeVariant);
        }
    }

    public static @NotNull ColorScheme fromImage(@NotNull Image image) {
        return fromImage(image, Brightness.LIGHT, DynamicSchemeVariant.TONAL_SPOT, 0.0);
    }

    public static ColorScheme fromImage(@NotNull Image image,
                                        @NotNull Brightness brightness,
                                        @NotNull DynamicSchemeVariant dynamicSchemeVariant,
                                        double contrastLevel) {
        PixelReader pixelReader = image.getPixelReader();
        if (pixelReader == null) {
            throw new IllegalArgumentException("Unable to read pixels of image");
        }

        Map<Integer, Integer> quantizeResult = QuantizerCelebi.quantize(
                pixelReader, (int) image.getWidth(), (int) image.getHeight(), 128);

        LinkedHashMap<Integer, Integer> colorToCount = new LinkedHashMap<>(quantizeResult.size());
        for (Map.Entry<Integer, Integer> entry : quantizeResult.entrySet()) {
            colorToCount.put(getArgbFromAbgr(entry.getKey()), entry.getValue());
        }

        // Score colors for color scheme suitability.
        final List<Integer> scoredResults = Score.score(colorToCount, 1);
        final Color baseColor = ColorUtils.fxFromArgb(scoredResults.get(0));

        return fromSeed(baseColor, brightness, dynamicSchemeVariant, contrastLevel);
    }

    /// Generate a [ColorScheme] derived from the given `seedColor`.
    ///
    /// Using the `seedColor` as a starting point, a set of tonal palettes are
    /// constructed. By default, the tonal palettes are based on the Material 3
    /// Color system and provide all of the [ColorScheme] colors. These colors are
    /// designed to work well together and meet contrast requirements for
    /// accessibility.
    ///
    /// If any of the optional color parameters are non-null they will be
    /// used in place of the generated colors for that field in the resulting
    /// color scheme. This allows apps to override specific colors for their
    /// needs.
    ///
    /// Given the nature of the algorithm, the `seedColor` may not wind up as
    /// one of the ColorScheme colors.
    ///
    /// The `dynamicSchemeVariant` parameter creates different types of
    /// [DynamicScheme]s, which are used to generate different styles of [ColorScheme]s.
    /// By default, `dynamicSchemeVariant` is set to `tonalSpot`. A [ColorScheme]
    /// constructed by `dynamicSchemeVariant.tonalSpot` has pastel palettes and
    /// won't be too "colorful" even if the `seedColor` has a high chroma value.
    /// If the resulting color scheme is too dark, consider setting `dynamicSchemeVariant`
    /// to [DynamicSchemeVariant#FIDELITY], whose palettes match the seed color.
    ///
    /// The `contrastLevel` parameter indicates the contrast level between color
    /// pairs, such as [#primary] and [#onPrimary]. 0.0 is the default (normal);
    /// -1.0 is the lowest; 1.0 is the highest. From Material Design guideline, the
    /// medium and high contrast correspond to 0.5 and 1.0 respectively.
    public static ColorScheme fromSeed(@NotNull Color seedColor) {
        return fromSeed(seedColor, Brightness.LIGHT, DynamicSchemeVariant.TONAL_SPOT, 0.0);
    }

    /// Generate a [ColorScheme] derived from the given `seedColor`.
    ///
    /// Using the `seedColor` as a starting point, a set of tonal palettes are
    /// constructed. By default, the tonal palettes are based on the Material 3
    /// Color system and provide all of the [ColorScheme] colors. These colors are
    /// designed to work well together and meet contrast requirements for
    /// accessibility.
    ///
    /// If any of the optional color parameters are non-null they will be
    /// used in place of the generated colors for that field in the resulting
    /// color scheme. This allows apps to override specific colors for their
    /// needs.
    ///
    /// Given the nature of the algorithm, the `seedColor` may not wind up as
    /// one of the ColorScheme colors.
    ///
    /// The `dynamicSchemeVariant` parameter creates different types of
    /// [DynamicScheme]s, which are used to generate different styles of [ColorScheme]s.
    /// By default, `dynamicSchemeVariant` is set to `tonalSpot`. A [ColorScheme]
    /// constructed by `dynamicSchemeVariant.tonalSpot` has pastel palettes and
    /// won't be too "colorful" even if the `seedColor` has a high chroma value.
    /// If the resulting color scheme is too dark, consider setting `dynamicSchemeVariant`
    /// to [DynamicSchemeVariant#FIDELITY], whose palettes match the seed color.
    ///
    /// The `contrastLevel` parameter indicates the contrast level between color
    /// pairs, such as [#primary] and [#onPrimary]. 0.0 is the default (normal);
    /// -1.0 is the lowest; 1.0 is the highest. From Material Design guideline, the
    /// medium and high contrast correspond to 0.5 and 1.0 respectively.
    public static ColorScheme fromSeed(@NotNull Color seedColor,
                                       @NotNull Brightness brightness,
                                       @NotNull DynamicSchemeVariant dynamicSchemeVariant,
                                       double contrastLevel) {
        final DynamicScheme scheme = buildDynamicScheme(
                brightness,
                seedColor,
                dynamicSchemeVariant,
                contrastLevel
        );

        return new ColorScheme(scheme);
    }

    private final Brightness brightness;
    private final double contrastLevel;

    private final Color primary;
    private final Color onPrimary;
    private final Color primaryContainer;
    private final Color onPrimaryContainer;
    private final Color primaryFixed;
    private final Color primaryFixedDim;
    private final Color onPrimaryFixed;
    private final Color onPrimaryFixedVariant;

    private final Color secondary;
    private final Color onSecondary;
    private final Color secondaryContainer;
    private final Color onSecondaryContainer;
    private final Color secondaryFixed;
    private final Color secondaryFixedDim;
    private final Color onSecondaryFixed;
    private final Color onSecondaryFixedVariant;

    private final Color tertiary;
    private final Color onTertiary;
    private final Color tertiaryContainer;
    private final Color onTertiaryContainer;
    private final Color tertiaryFixed;
    private final Color tertiaryFixedDim;
    private final Color onTertiaryFixed;
    private final Color onTertiaryFixedVariant;

    private final Color error;
    private final Color onError;
    private final Color errorContainer;
    private final Color onErrorContainer;

    private final Color surface;
    private final Color onSurface;
    private final Color surfaceDim;
    private final Color surfaceBright;
    private final Color surfaceContainerLowest;
    private final Color surfaceContainerLow;
    private final Color surfaceContainer;
    private final Color surfaceContainerHigh;
    private final Color surfaceContainerHighest;
    private final Color surfaceVariant;
    private final Color onSurfaceVariant;

    private final Color background;
    private final Color onBackground;

    private final Color outline;
    private final Color outlineVariant;

    private final Color shadow;
    private final Color scrim;
    private final Color inverseSurface;
    private final Color inverseOnSurface;
    private final Color inversePrimary;
    private final Color surfaceTint;

    private ColorScheme(DynamicScheme scheme) {
        this.brightness = scheme.isDark ? Brightness.DARK : Brightness.LIGHT;
        this.contrastLevel = scheme.contrastLevel;

        this.primary = ColorUtils.fxFromArgb(scheme.getPrimary());
        this.onPrimary = ColorUtils.fxFromArgb(scheme.getOnPrimary());
        this.primaryContainer = ColorUtils.fxFromArgb(scheme.getPrimaryContainer());
        this.onPrimaryContainer = ColorUtils.fxFromArgb(scheme.getOnPrimaryContainer());
        this.primaryFixed = ColorUtils.fxFromArgb(scheme.getPrimaryFixed());
        this.primaryFixedDim = ColorUtils.fxFromArgb(scheme.getPrimaryFixedDim());
        this.onPrimaryFixed = ColorUtils.fxFromArgb(scheme.getOnPrimaryFixed());
        this.onPrimaryFixedVariant = ColorUtils.fxFromArgb(scheme.getOnPrimaryFixedVariant());

        this.secondary = ColorUtils.fxFromArgb(scheme.getSecondary());
        this.onSecondary = ColorUtils.fxFromArgb(scheme.getOnSecondary());
        this.secondaryContainer = ColorUtils.fxFromArgb(scheme.getSecondaryContainer());
        this.onSecondaryContainer = ColorUtils.fxFromArgb(scheme.getOnSecondaryContainer());
        this.secondaryFixed = ColorUtils.fxFromArgb(scheme.getSecondaryFixed());
        this.secondaryFixedDim = ColorUtils.fxFromArgb(scheme.getSecondaryFixedDim());
        this.onSecondaryFixed = ColorUtils.fxFromArgb(scheme.getOnSecondaryFixed());
        this.onSecondaryFixedVariant = ColorUtils.fxFromArgb(scheme.getOnSecondaryFixedVariant());

        this.tertiary = ColorUtils.fxFromArgb(scheme.getTertiary());
        this.onTertiary = ColorUtils.fxFromArgb(scheme.getOnTertiary());
        this.tertiaryContainer = ColorUtils.fxFromArgb(scheme.getTertiaryContainer());
        this.onTertiaryContainer = ColorUtils.fxFromArgb(scheme.getTertiaryContainer());
        this.tertiaryFixed = ColorUtils.fxFromArgb(scheme.getTertiaryFixed());
        this.tertiaryFixedDim = ColorUtils.fxFromArgb(scheme.getTertiaryFixedDim());
        this.onTertiaryFixed = ColorUtils.fxFromArgb(scheme.getOnTertiaryFixed());
        this.onTertiaryFixedVariant = ColorUtils.fxFromArgb(scheme.getOnTertiaryFixedVariant());

        this.error = ColorUtils.fxFromArgb(scheme.getError());
        this.onError = ColorUtils.fxFromArgb(scheme.getOnError());
        this.errorContainer = ColorUtils.fxFromArgb(scheme.getErrorContainer());
        this.onErrorContainer = ColorUtils.fxFromArgb(scheme.getOnErrorContainer());

        this.surface = ColorUtils.fxFromArgb(scheme.getSurface());
        this.onSurface = ColorUtils.fxFromArgb(scheme.getOnSurface());
        this.surfaceDim = ColorUtils.fxFromArgb(scheme.getSurfaceDim());
        this.surfaceBright = ColorUtils.fxFromArgb(scheme.getSurfaceBright());
        this.surfaceContainerLowest = ColorUtils.fxFromArgb(scheme.getSurfaceContainerLowest());
        this.surfaceContainerLow = ColorUtils.fxFromArgb(scheme.getSurfaceContainerLow());
        this.surfaceContainer = ColorUtils.fxFromArgb(scheme.getSurfaceContainer());
        this.surfaceContainerHigh = ColorUtils.fxFromArgb(scheme.getSurfaceContainerHigh());
        this.surfaceContainerHighest = ColorUtils.fxFromArgb(scheme.getSurfaceContainerHighest());
        this.surfaceVariant = ColorUtils.fxFromArgb(scheme.getSurfaceVariant());
        this.onSurfaceVariant = ColorUtils.fxFromArgb(scheme.getOnSurfaceVariant());

        this.background = ColorUtils.fxFromArgb(scheme.getBackground());
        this.onBackground = ColorUtils.fxFromArgb(scheme.getOnBackground());

        this.outline = ColorUtils.fxFromArgb(scheme.getOutline());
        this.outlineVariant = ColorUtils.fxFromArgb(scheme.getOutlineVariant());

        this.shadow = ColorUtils.fxFromArgb(scheme.getShadow());
        this.scrim = ColorUtils.fxFromArgb(scheme.getScrim());
        this.inverseSurface = ColorUtils.fxFromArgb(scheme.getInverseSurface());
        this.inverseOnSurface = ColorUtils.fxFromArgb(scheme.getInverseOnSurface());
        this.inversePrimary = ColorUtils.fxFromArgb(scheme.getInversePrimary());
        this.surfaceTint = ColorUtils.fxFromArgb(scheme.getSurfaceTint());
    }

    public Brightness getBrightness() {
        return brightness;
    }

    public double getContrastLevel() {
        return contrastLevel;
    }

    public Color getPrimary() {
        return primary;
    }

    public Color getOnPrimary() {
        return onPrimary;
    }

    public Color getPrimaryContainer() {
        return primaryContainer;
    }

    public Color getOnPrimaryContainer() {
        return onPrimaryContainer;
    }

    public Color getPrimaryFixed() {
        return primaryFixed;
    }

    public Color getPrimaryFixedDim() {
        return primaryFixedDim;
    }

    public Color getOnPrimaryFixed() {
        return onPrimaryFixed;
    }

    public Color getOnPrimaryFixedVariant() {
        return onPrimaryFixedVariant;
    }

    public Color getSecondary() {
        return secondary;
    }

    public Color getOnSecondary() {
        return onSecondary;
    }

    public Color getSecondaryContainer() {
        return secondaryContainer;
    }

    public Color getOnSecondaryContainer() {
        return onSecondaryContainer;
    }

    public Color getSecondaryFixed() {
        return secondaryFixed;
    }

    public Color getSecondaryFixedDim() {
        return secondaryFixedDim;
    }

    public Color getOnSecondaryFixed() {
        return onSecondaryFixed;
    }

    public Color getOnSecondaryFixedVariant() {
        return onSecondaryFixedVariant;
    }

    public Color getTertiary() {
        return tertiary;
    }

    public Color getOnTertiary() {
        return onTertiary;
    }

    public Color getTertiaryContainer() {
        return tertiaryContainer;
    }

    public Color getOnTertiaryContainer() {
        return onTertiaryContainer;
    }

    public Color getTertiaryFixed() {
        return tertiaryFixed;
    }

    public Color getTertiaryFixedDim() {
        return tertiaryFixedDim;
    }

    public Color getOnTertiaryFixed() {
        return onTertiaryFixed;
    }

    public Color getOnTertiaryFixedVariant() {
        return onTertiaryFixedVariant;
    }

    public Color getError() {
        return error;
    }

    public Color getOnError() {
        return onError;
    }

    public Color getErrorContainer() {
        return errorContainer;
    }

    public Color getOnErrorContainer() {
        return onErrorContainer;
    }

    public Color getSurface() {
        return surface;
    }

    public Color getOnSurface() {
        return onSurface;
    }

    public Color getSurfaceDim() {
        return surfaceDim;
    }

    public Color getSurfaceBright() {
        return surfaceBright;
    }

    public Color getSurfaceContainerLowest() {
        return surfaceContainerLowest;
    }

    public Color getSurfaceContainerLow() {
        return surfaceContainerLow;
    }

    public Color getSurfaceContainer() {
        return surfaceContainer;
    }

    public Color getSurfaceContainerHigh() {
        return surfaceContainerHigh;
    }

    public Color getSurfaceContainerHighest() {
        return surfaceContainerHighest;
    }

    public Color getSurfaceVariant() {
        return surfaceVariant;
    }

    public Color getOnSurfaceVariant() {
        return onSurfaceVariant;
    }

    public Color getBackground() {
        return background;
    }

    public Color getOnBackground() {
        return onBackground;
    }

    public Color getOutline() {
        return outline;
    }

    public Color getOutlineVariant() {
        return outlineVariant;
    }

    public Color getShadow() {
        return shadow;
    }

    public Color getScrim() {
        return scrim;
    }

    public Color getInverseSurface() {
        return inverseSurface;
    }

    public Color getInverseOnSurface() {
        return inverseOnSurface;
    }

    public Color getInversePrimary() {
        return inversePrimary;
    }

    public Color getSurfaceTint() {
        return surfaceTint;
    }
}
