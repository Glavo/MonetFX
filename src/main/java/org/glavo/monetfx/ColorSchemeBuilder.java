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
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/// A builder of [ColorScheme].
///
/// Builders are created by invoking [ColorScheme#newBuilder()].
/// Each of the setter methods modifies the state of the builder
/// and returns the same instance.
///
/// Builders are not thread-safe and should not be
/// used concurrently from multiple threads without external synchronization.
///
/// @see ColorScheme#newBuilder()
public final class ColorSchemeBuilder {

    private Color primaryColorSeed;
    private Color secondaryColorSeed;
    private Color tertiaryColorSeed;
    private Color neutralColorSeed;
    private Color neutralVariantColorSeed;
    private Color errorColorSeed;

    private Brightness brightness = Brightness.DEFAULT;
    private ColorStyle colorStyle = ColorStyle.DEFAULT;
    private Contrast contrast = Contrast.DEFAULT;
    private ColorSpecVersion specVersion = ColorSpecVersion.DEFAULT;
    private TargetPlatform platform = TargetPlatform.DEFAULT;

    ColorSchemeBuilder() {
    }

    /// Sets the primary color seed for this scheme.
    ///
    /// If the seed is not set or the seed is `null`,
    /// the newly built scheme will use `#4285f4` (Google Blue) as the primary color seed.
    ///
    /// @see #setWallpaper(Image)
    @Contract(value = "_ -> this")
    public ColorSchemeBuilder setPrimaryColorSeed(@Nullable Color primaryColorSeed) {
        this.primaryColorSeed = primaryColorSeed;
        return this;
    }

    /// Sets the primary color seed for this scheme by given image.
    ///
    /// This method analyzes the image and extracts a color from it as the primary color seed.
    ///
    /// If the seed is not set or the seed is `null`,
    /// the newly built scheme will use `#4285f4` (Google Blue) as the primary color seed.
    ///
    /// @throws NullPointerException     if the `image` is `null`.
    /// @throws IllegalArgumentException If the `image` has not yet been loaded or has failed to load.
    /// @see #setPrimaryColorSeed(Color)
    /// @see <a href="https://m3.material.io/styles/color/dynamic/choosing-a-source">Dynamic color schemes</a>
    @Contract(value = "_ -> this")
    public ColorSchemeBuilder setWallpaper(@NotNull Image image) {
        this.primaryColorSeed = ColorScheme.extractColor(image, ColorScheme.FALLBACK_COLOR);
        return this;
    }

    /// Sets the primary color seed for this scheme by given image.
    ///
    /// This method analyzes the image and extracts a color from it as the primary color seed.
    ///
    /// If the seed is not set or the seed is `null`,
    /// the newly built scheme will use `#4285f4` (Google Blue) as the primary color seed.
    ///
    /// @throws NullPointerException     if the `image` is `null`.
    /// @throws IllegalArgumentException If the `image` has not yet been loaded or has failed to load.
    /// @see #setPrimaryColorSeed(Color)
    /// @see <a href="https://m3.material.io/styles/color/dynamic/choosing-a-source">Dynamic color schemes</a>
    @Contract(value = "_, _ -> this")
    public ColorSchemeBuilder setWallpaper(@NotNull Image image, Color fallbackColor) {
        this.primaryColorSeed = ColorScheme.extractColor(image, fallbackColor);
        return this;
    }

    /// Sets the secondary color seed for this scheme.
    ///
    /// If the seed is not set or the seed is `null`,
    /// the secondary palette will be derived from [the primary color seed][#setPrimaryColorSeed(Color)].
    @Contract(value = "_ -> this")
    public ColorSchemeBuilder setSecondaryColorSeed(@Nullable Color secondaryColorSeed) {
        this.secondaryColorSeed = secondaryColorSeed;
        return this;
    }

    /// Sets the tertiary color seed for this scheme.
    ///
    /// If the seed is not set or the seed is `null`,
    /// the tertiary palette will be derived from [the primary color seed][#setPrimaryColorSeed(Color)].
    @Contract(value = "_ -> this")
    public ColorSchemeBuilder setTertiaryColorSeed(@Nullable Color tertiaryColorSeed) {
        this.tertiaryColorSeed = tertiaryColorSeed;
        return this;
    }

    /// Sets the neutral color seed for this scheme.
    ///
    /// If the seed is not set or the seed is `null`,
    /// the neutral palette will be derived from [the primary color seed][#setPrimaryColorSeed(Color)].
    @Contract(value = "_ -> this")
    public ColorSchemeBuilder setNeutralColorSeed(@Nullable Color neutralColorSeed) {
        this.neutralColorSeed = neutralColorSeed;
        return this;
    }

    /// Sets the neutral variant color seed for this scheme.
    ///
    /// If the seed is not set or the seed is `null`,
    /// the neutral variant palette will be derived from [the primary color seed][#setPrimaryColorSeed(Color)].
    @Contract(value = "_ -> this")
    public ColorSchemeBuilder setNeutralVariantColorSeed(@Nullable Color neutralVariantColorSeed) {
        this.neutralVariantColorSeed = neutralVariantColorSeed;
        return this;
    }

    /// Sets the error color seed for this scheme.
    ///
    /// If the seed is not set or the seed is `null`, the default error palette will be used.
    @Contract(value = "_ -> this")
    public ColorSchemeBuilder setErrorColorSeed(@Nullable Color errorColorSeed) {
        this.errorColorSeed = errorColorSeed;
        return this;
    }

    /// Sets the [brightness][Brightness] for this scheme.
    ///
    /// If not set, [the default brightness][Brightness#DEFAULT] will be used.
    @Contract(value = "_ -> this")
    public ColorSchemeBuilder setBrightness(@NotNull Brightness brightness) {
        this.brightness = Objects.requireNonNull(brightness);
        return this;
    }

    /// Sets the [color style][ColorStyle] for this scheme.
    ///
    /// If not set, [the default color style][ColorStyle#DEFAULT] will be used.
    @Contract(value = "_ -> this")
    public ColorSchemeBuilder setColorStyle(@NotNull ColorStyle colorStyle) {
        this.colorStyle = Objects.requireNonNull(colorStyle);
        return this;
    }

    /// Sets the [contrast][Contrast] for this scheme.
    ///
    /// If not set, [the default contrast][Contrast#DEFAULT] will be used.
    @Contract(value = "_ -> this")
    public ColorSchemeBuilder setContrast(@NotNull Contrast contrast) {
        this.contrast = Objects.requireNonNull(contrast);
        return this;
    }

    /// Sets the [target platform][TargetPlatform] for this scheme.
    ///
    /// If not set, [the target platform][TargetPlatform#DEFAULT] will be used.
    ///
    /// @since 0.2.0
    @Contract(value = "_ -> this")
    public ColorSchemeBuilder setPlatform(@NotNull TargetPlatform platform) {
        this.platform = Objects.requireNonNull(platform);
        return this;
    }

    /// Sets the color scheme specification version for this scheme.
    ///
    /// If not set, [the default specification version][ColorSpecVersion#DEFAULT] will be used.
    ///
    /// @apiNote Only some [color scheme][ColorScheme] support [ColorSpecVersion#SPEC_2025].
    /// If [ColorSpecVersion#SPEC_2025] is set but the color scheme used does not support this version,
    /// it will fall back to [ColorSpecVersion#SPEC_2021].
    /// @since 0.2.0
    @Contract(value = "_ -> this")
    public ColorSchemeBuilder setSpecVersion(@NotNull ColorSpecVersion specVersion) {
        this.specVersion = Objects.requireNonNull(specVersion);
        return this;
    }

    /// Returns a new [ColorScheme] built from the current state of this builder.
    public ColorScheme build() {
        return new ColorScheme(
                colorStyle,
                brightness == Brightness.DARK,
                contrast.getValue(),
                this.platform,
                specVersion,
                primaryColorSeed, secondaryColorSeed, tertiaryColorSeed, neutralColorSeed, neutralVariantColorSeed, errorColorSeed);
    }
}
