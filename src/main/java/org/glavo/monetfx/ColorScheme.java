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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// See https://github.com/flutter/flutter/blob/5491c8c146441d3126aff91beaa3fb5df6d710d0/packages/flutter/lib/src/material/color_scheme.dart

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

    public static ColorScheme fromImage(Image image, Brightness brightness, DynamicSchemeVariant dynamicSchemeVariant, double contrastLevel) {
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

        final DynamicScheme scheme = buildDynamicScheme(
                brightness,
                baseColor,
                dynamicSchemeVariant,
                contrastLevel
        );

        return new ColorScheme(scheme);
    }

    private final Color primary;
    private final Color onPrimary;
    private final Color primaryContainer;
    private final Color onPrimaryContainer;
    private final Color inversePrimary;
    private final Color secondary;
    private final Color onSecondary;
    private final Color secondaryContainer;
    private final Color onSecondaryContainer;
    private final Color tertiary;
    private final Color onTertiary;
    private final Color tertiaryContainer;
    private final Color onTertiaryContainer;
    private final Color background;
    private final Color onBackground;
    private final Color surface;
    private final Color onSurface;
    private final Color surfaceVariant;
    private final Color onSurfaceVariant;
    private final Color surfaceTint;
    private final Color inverseSurface;
    private final Color inverseOnSurface;
    private final Color error;
    private final Color onError;
    private final Color errorContainer;
    private final Color onErrorContainer;
    private final Color outline;
    private final Color outlineVariant;
    private final Color scrim;
    private final Color surfaceBright;
    private final Color surfaceDim;
    private final Color surfaceContainer;
    private final Color surfaceContainerHigh;
    private final Color surfaceContainerHighest;
    private final Color surfaceContainerLow;
    private final Color surfaceContainerLowest;

    private ColorScheme(Color primary, Color onPrimary, Color primaryContainer, Color onPrimaryContainer, Color inversePrimary, Color secondary, Color onSecondary, Color secondaryContainer, Color onSecondaryContainer, Color tertiary, Color onTertiary, Color tertiaryContainer, Color onTertiaryContainer, Color background, Color onBackground, Color surface, Color onSurface, Color surfaceVariant, Color onSurfaceVariant, Color surfaceTint, Color inverseSurface, Color inverseOnSurface, Color error, Color onError, Color errorContainer, Color onErrorContainer, Color outline, Color outlineVariant, Color scrim, Color surfaceBright, Color surfaceDim, Color surfaceContainer, Color surfaceContainerHigh, Color surfaceContainerHighest, Color surfaceContainerLow, Color surfaceContainerLowest) {
        this.primary = primary;
        this.onPrimary = onPrimary;
        this.primaryContainer = primaryContainer;
        this.onPrimaryContainer = onPrimaryContainer;
        this.inversePrimary = inversePrimary;
        this.secondary = secondary;
        this.onSecondary = onSecondary;
        this.secondaryContainer = secondaryContainer;
        this.onSecondaryContainer = onSecondaryContainer;
        this.tertiary = tertiary;
        this.onTertiary = onTertiary;
        this.tertiaryContainer = tertiaryContainer;
        this.onTertiaryContainer = onTertiaryContainer;
        this.background = background;
        this.onBackground = onBackground;
        this.surface = surface;
        this.onSurface = onSurface;
        this.surfaceVariant = surfaceVariant;
        this.onSurfaceVariant = onSurfaceVariant;
        this.surfaceTint = surfaceTint;
        this.inverseSurface = inverseSurface;
        this.inverseOnSurface = inverseOnSurface;
        this.error = error;
        this.onError = onError;
        this.errorContainer = errorContainer;
        this.onErrorContainer = onErrorContainer;
        this.outline = outline;
        this.outlineVariant = outlineVariant;
        this.scrim = scrim;
        this.surfaceBright = surfaceBright;
        this.surfaceDim = surfaceDim;
        this.surfaceContainer = surfaceContainer;
        this.surfaceContainerHigh = surfaceContainerHigh;
        this.surfaceContainerHighest = surfaceContainerHighest;
        this.surfaceContainerLow = surfaceContainerLow;
        this.surfaceContainerLowest = surfaceContainerLowest;
    }

    private ColorScheme(DynamicScheme scheme) {
        this.primary = ColorUtils.fxFromArgb(scheme.getPrimary());
        this.onPrimary = ColorUtils.fxFromArgb(scheme.getOnPrimary());
        this.primaryContainer = ColorUtils.fxFromArgb(scheme.getPrimaryContainer());
        this.onPrimaryContainer = ColorUtils.fxFromArgb(scheme.getOnPrimaryContainer());
        this.inversePrimary = ColorUtils.fxFromArgb(scheme.getInversePrimary());
        this.secondary = ColorUtils.fxFromArgb(scheme.getSecondary());
        this.onSecondary = ColorUtils.fxFromArgb(scheme.getOnSecondary());
        this.secondaryContainer = ColorUtils.fxFromArgb(scheme.getSecondaryContainer());
        this.onSecondaryContainer = ColorUtils.fxFromArgb(scheme.getOnSecondaryContainer());
        this.tertiary = ColorUtils.fxFromArgb(scheme.getTertiary());
        this.onTertiary = ColorUtils.fxFromArgb(scheme.getOnTertiary());
        this.tertiaryContainer = ColorUtils.fxFromArgb(scheme.getTertiaryContainer());
        this.onTertiaryContainer = ColorUtils.fxFromArgb(scheme.getTertiaryContainer());
        this.background = ColorUtils.fxFromArgb(scheme.getBackground());
        this.onBackground = ColorUtils.fxFromArgb(scheme.getOnBackground());
        this.surface = ColorUtils.fxFromArgb(scheme.getSurface());
        this.onSurface = ColorUtils.fxFromArgb(scheme.getOnSurface());
        this.surfaceVariant = ColorUtils.fxFromArgb(scheme.getSurfaceVariant());
        this.onSurfaceVariant = ColorUtils.fxFromArgb(scheme.getOnSurfaceVariant());
        this.surfaceTint = ColorUtils.fxFromArgb(scheme.getSurfaceTint());
        this.inverseSurface = ColorUtils.fxFromArgb(scheme.getInverseSurface());
        this.inverseOnSurface = ColorUtils.fxFromArgb(scheme.getInverseOnSurface());
        this.error = ColorUtils.fxFromArgb(scheme.getError());
        this.onError = ColorUtils.fxFromArgb(scheme.getOnError());
        this.errorContainer = ColorUtils.fxFromArgb(scheme.getErrorContainer());
        this.onErrorContainer = ColorUtils.fxFromArgb(scheme.getOnErrorContainer());
        this.outline = ColorUtils.fxFromArgb(scheme.getOutline());
        this.outlineVariant = ColorUtils.fxFromArgb(scheme.getOutlineVariant());
        this.scrim = ColorUtils.fxFromArgb(scheme.getScrim());
        this.surfaceBright = ColorUtils.fxFromArgb(scheme.getSurfaceBright());
        this.surfaceDim = ColorUtils.fxFromArgb(scheme.getSurfaceDim());
        this.surfaceContainer = ColorUtils.fxFromArgb(scheme.getSurfaceContainer());
        this.surfaceContainerHigh = ColorUtils.fxFromArgb(scheme.getSurfaceContainerHigh());
        this.surfaceContainerHighest = ColorUtils.fxFromArgb(scheme.getSurfaceContainerHighest());
        this.surfaceContainerLow = ColorUtils.fxFromArgb(scheme.getSurfaceContainerLow());
        this.surfaceContainerLowest = ColorUtils.fxFromArgb(scheme.getSurfaceContainerLowest());
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

    public Color getInversePrimary() {
        return inversePrimary;
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

    public Color getBackground() {
        return background;
    }

    public Color getOnBackground() {
        return onBackground;
    }

    public Color getSurface() {
        return surface;
    }

    public Color getOnSurface() {
        return onSurface;
    }

    public Color getSurfaceVariant() {
        return surfaceVariant;
    }

    public Color getOnSurfaceVariant() {
        return onSurfaceVariant;
    }

    public Color getSurfaceTint() {
        return surfaceTint;
    }

    public Color getInverseSurface() {
        return inverseSurface;
    }

    public Color getInverseOnSurface() {
        return inverseOnSurface;
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

    public Color getOutline() {
        return outline;
    }

    public Color getOutlineVariant() {
        return outlineVariant;
    }

    public Color getScrim() {
        return scrim;
    }

    public Color getSurfaceBright() {
        return surfaceBright;
    }

    public Color getSurfaceDim() {
        return surfaceDim;
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

    public Color getSurfaceContainerLow() {
        return surfaceContainerLow;
    }

    public Color getSurfaceContainerLowest() {
        return surfaceContainerLowest;
    }
}
