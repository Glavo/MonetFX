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

    private Image wallpaperImage;
    private Color primaryColor;
    private Color secondaryColor;
    private Color tertiaryColor;
    private Color neutralColor;
    private Color neutralVariantColor;
    private Color errorColor;

    private Color fallbackColor = ColorScheme.FALLBACK_COLOR;
    private Brightness brightness = Brightness.LIGHT;
    private DynamicSchemeVariant dynamicSchemeVariant = DynamicSchemeVariant.TONAL_SPOT;
    private Contrast contrast = Contrast.STANDARD;

    ColorSchemeBuilder() {
    }

    @Contract(value = "_ -> this", pure = true)
    public ColorSchemeBuilder setWallpaperImage(Image wallpaperImage) {
        this.wallpaperImage = wallpaperImage;
        this.primaryColor = null;
        return this;
    }

    @Contract(value = "_ -> this", pure = true)
    public ColorSchemeBuilder setPrimaryColor(Color primaryColor) {
        this.wallpaperImage = null;
        this.primaryColor = primaryColor;
        return this;
    }

    @Contract(value = "_ -> this", pure = true)
    public ColorSchemeBuilder setSecondaryColor(Color secondaryColor) {
        this.secondaryColor = secondaryColor;
        return this;
    }

    @Contract(value = "_ -> this", pure = true)
    public ColorSchemeBuilder setTertiaryColor(Color tertiaryColor) {
        this.tertiaryColor = tertiaryColor;
        return this;
    }

    @Contract(value = "_ -> this", pure = true)
    public ColorSchemeBuilder setNeutralColor(Color neutralColor) {
        this.neutralColor = neutralColor;
        return this;
    }

    @Contract(value = "_ -> this", pure = true)
    public ColorSchemeBuilder setNeutralVariantColor(Color neutralVariantColor) {
        this.neutralVariantColor = neutralVariantColor;
        return this;
    }

    @Contract(value = "_ -> this", pure = true)
    public ColorSchemeBuilder setErrorColor(Color errorColor) {
        this.errorColor = errorColor;
        return this;
    }

    @Contract(value = "_ -> this", pure = true)
    public ColorSchemeBuilder setFallbackColor(@NotNull Color fallbackColor) {
        this.fallbackColor = Objects.requireNonNull(fallbackColor);
        return this;
    }

    @Contract(value = "_ -> this", pure = true)
    public ColorSchemeBuilder setBrightness(@NotNull Brightness brightness) {
        this.brightness = Objects.requireNonNull(brightness);
        return this;
    }

    @Contract(value = "_ -> this", pure = true)
    public ColorSchemeBuilder setDynamicSchemeVariant(DynamicSchemeVariant dynamicSchemeVariant) {
        this.dynamicSchemeVariant = dynamicSchemeVariant;
        return this;
    }

    @Contract(value = "_ -> this", pure = true)
    public ColorSchemeBuilder setContrast(@NotNull Contrast contrast) {
        this.contrast = Objects.requireNonNull(contrast);
        return this;
    }

    public ColorScheme build() {
        Color primaryColor = this.primaryColor;
        if (primaryColor == null) {
            if (wallpaperImage == null) {
                throw new IllegalStateException("Primary color has not been set");
            }

            primaryColor = ColorScheme.extractColor(wallpaperImage, fallbackColor);
        }

        boolean isDark = brightness == Brightness.DARK;
        double contrastLevel = contrast.getValue();

        Hct primaryColorHct = Hct.fromFx(primaryColor);

        return new ColorScheme(new DynamicScheme(
                primaryColorHct,
                dynamicSchemeVariant,
                isDark,
                contrastLevel,
                dynamicSchemeVariant.getPrimaryPalette(primaryColorHct, isDark, contrastLevel),

                this.secondaryColor == null
                        ? dynamicSchemeVariant.getSecondaryPalette(primaryColorHct, isDark, contrastLevel)
                        : dynamicSchemeVariant.getPrimaryPalette(Hct.fromFx(this.secondaryColor), isDark, contrastLevel),

                this.tertiaryColor == null
                        ? dynamicSchemeVariant.getTertiaryPalette(primaryColorHct, isDark, contrastLevel)
                        : dynamicSchemeVariant.getPrimaryPalette(Hct.fromFx(this.tertiaryColor), isDark, contrastLevel),

                this.neutralColor == null
                        ? dynamicSchemeVariant.getNeutralPalette(primaryColorHct, isDark, contrastLevel)
                        : dynamicSchemeVariant.getNeutralPalette(Hct.fromFx(this.neutralColor), isDark, contrastLevel),

                this.neutralVariantColor == null
                        ? dynamicSchemeVariant.getNeutralVariantPalette(primaryColorHct, isDark, contrastLevel)
                        : dynamicSchemeVariant.getNeutralVariantPalette(Hct.fromFx(this.neutralVariantColor), isDark, contrastLevel),

                this.errorColor == null
                        ? DynamicScheme.DEFAULT_ERROR_PALETTE
                        : dynamicSchemeVariant.getPrimaryPalette(Hct.fromFx(this.errorColor), isDark, contrastLevel)
        ), primaryColor, secondaryColor, tertiaryColor, neutralColor, neutralVariantColor, errorColor);
    }
}
