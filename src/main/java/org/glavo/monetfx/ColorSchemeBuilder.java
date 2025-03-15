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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class ColorSchemeBuilder {
    private final Color seed;
    private final Image image;

    private Color fallbackColor = ColorScheme.FALLBACK_COLOR;
    private Brightness brightness = Brightness.LIGHT;
    private DynamicSchemeVariant dynamicSchemeVariant = DynamicSchemeVariant.FIDELITY;
    private Contrast contrast = Contrast.STANDARD;

    ColorSchemeBuilder(Color seed) {
        this.seed = Objects.requireNonNull(seed);
        this.image = null;
    }

    ColorSchemeBuilder(Image image) {
        this.seed = null;
        this.image = Objects.requireNonNull(image);
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
        Color seedColor = seed;
        if (seedColor == null) {
            seedColor = ColorScheme.extractColor(image, fallbackColor);
        }
        return new ColorScheme(ColorScheme.buildDynamicScheme(seedColor, brightness, dynamicSchemeVariant, contrast));
    }
}
