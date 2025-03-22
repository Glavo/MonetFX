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
import javafx.scene.paint.Color;
import org.glavo.monetfx.internal.dynamiccolor.DynamicScheme;
import org.glavo.monetfx.internal.hct.Hct;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class ColorSchemeBuilder {

    private Color primaryColorSeed;
    private Color secondaryColorSeed;
    private Color tertiaryColorSeed;
    private Color neutralColorSeed;
    private Color neutralVariantColorSeed;
    private Color errorColorSeed;

    private Brightness brightness = Brightness.LIGHT;
    private ColorStyle colorStyle = ColorStyle.TONAL_SPOT;
    private Contrast contrast = Contrast.STANDARD;

    ColorSchemeBuilder() {
    }

    @Contract(value = "_ -> this", pure = true)
    public ColorSchemeBuilder setPrimaryColorSeed(Color primaryColorSeed) {
        this.primaryColorSeed = primaryColorSeed;
        return this;
    }

    @Contract(value = "_ -> this", pure = true)
    public ColorSchemeBuilder setWallpaper(@NotNull Image image) {
        this.primaryColorSeed = ColorScheme.extractColor(image, ColorScheme.FALLBACK_COLOR);
        return this;
    }

    @Contract(value = "_, _ -> this", pure = true)
    public ColorSchemeBuilder setWallpaper(@NotNull Image image, Color fallbackColor) {
        this.primaryColorSeed = ColorScheme.extractColor(image, fallbackColor);
        return this;
    }

    @Contract(value = "_ -> this", pure = true)
    public ColorSchemeBuilder setSecondaryColorSeed(Color secondaryColorSeed) {
        this.secondaryColorSeed = secondaryColorSeed;
        return this;
    }

    @Contract(value = "_ -> this", pure = true)
    public ColorSchemeBuilder setTertiaryColorSeed(Color tertiaryColorSeed) {
        this.tertiaryColorSeed = tertiaryColorSeed;
        return this;
    }

    @Contract(value = "_ -> this", pure = true)
    public ColorSchemeBuilder setNeutralColorSeed(Color neutralColorSeed) {
        this.neutralColorSeed = neutralColorSeed;
        return this;
    }

    @Contract(value = "_ -> this", pure = true)
    public ColorSchemeBuilder setNeutralVariantColorSeed(Color neutralVariantColorSeed) {
        this.neutralVariantColorSeed = neutralVariantColorSeed;
        return this;
    }

    @Contract(value = "_ -> this", pure = true)
    public ColorSchemeBuilder setErrorColorSeed(Color errorColorSeed) {
        this.errorColorSeed = errorColorSeed;
        return this;
    }

    @Contract(value = "_ -> this", pure = true)
    public ColorSchemeBuilder setBrightness(@NotNull Brightness brightness) {
        this.brightness = Objects.requireNonNull(brightness);
        return this;
    }

    @Contract(value = "_ -> this", pure = true)
    public ColorSchemeBuilder setColorStyle(@NotNull ColorStyle colorStyle) {
        this.colorStyle = Objects.requireNonNull(colorStyle);
        return this;
    }

    @Contract(value = "_ -> this", pure = true)
    public ColorSchemeBuilder setContrast(@NotNull Contrast contrast) {
        this.contrast = Objects.requireNonNull(contrast);
        return this;
    }

    public ColorScheme build() {
        Color primaryColor = this.primaryColorSeed;
        if (primaryColor == null) {
            primaryColor = ColorScheme.FALLBACK_COLOR;
        }

        boolean isDark = brightness == Brightness.DARK;
        double contrastLevel = contrast.getValue();

        Hct primaryColorHct = Hct.fromFx(primaryColor);

        return new ColorScheme(new DynamicScheme(
                primaryColorHct,
                colorStyle,
                isDark,
                contrastLevel,
                colorStyle.getPrimaryPalette(primaryColorHct, isDark, contrastLevel),

                this.secondaryColorSeed == null
                        ? colorStyle.getSecondaryPalette(primaryColorHct, isDark, contrastLevel)
                        : colorStyle.getPrimaryPalette(Hct.fromFx(this.secondaryColorSeed), isDark, contrastLevel),

                this.tertiaryColorSeed == null
                        ? colorStyle.getTertiaryPalette(primaryColorHct, isDark, contrastLevel)
                        : colorStyle.getPrimaryPalette(Hct.fromFx(this.tertiaryColorSeed), isDark, contrastLevel),

                this.neutralColorSeed == null
                        ? colorStyle.getNeutralPalette(primaryColorHct, isDark, contrastLevel)
                        : colorStyle.getNeutralPalette(Hct.fromFx(this.neutralColorSeed), isDark, contrastLevel),

                this.neutralVariantColorSeed == null
                        ? colorStyle.getNeutralVariantPalette(primaryColorHct, isDark, contrastLevel)
                        : colorStyle.getNeutralVariantPalette(Hct.fromFx(this.neutralVariantColorSeed), isDark, contrastLevel),

                this.errorColorSeed == null
                        ? DynamicScheme.DEFAULT_ERROR_PALETTE
                        : colorStyle.getPrimaryPalette(Hct.fromFx(this.errorColorSeed), isDark, contrastLevel)
        ), primaryColor, secondaryColorSeed, tertiaryColorSeed, neutralColorSeed, neutralVariantColorSeed, errorColorSeed);
    }
}
