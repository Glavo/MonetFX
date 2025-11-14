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
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import org.glavo.monetfx.internal.dynamiccolor.ColorSpec;
import org.glavo.monetfx.internal.dynamiccolor.ColorSpecs;
import org.glavo.monetfx.internal.dynamiccolor.DynamicScheme;
import org.glavo.monetfx.internal.hct.Hct;
import org.glavo.monetfx.internal.quantize.QuantizerCelebi;
import org.glavo.monetfx.internal.score.Score;
import org.glavo.monetfx.internal.utils.ColorUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
/// The main accent color groups in the scheme are [ColorRole#PRIMARY], [ColorRole#SECONDARY],
/// and [ColorRole#TERTIARY].
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
/// '-Dim' color roles, such as [ColorRole#PRIMARY_FIXED] and [ColorRole#PRIMARY_FIXED_DIM]. Fixed roles
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
/// top of surfaces based on their elevation. These colors include: [ColorRole#SURFACE_BRIGHT],
/// [ColorRole#SURFACE_DIM], [ColorRole#SURFACE_CONTAINER_LOWEST], [ColorRole#SURFACE_CONTAINER_LOW], [ColorRole#SURFACE_CONTAINER],
/// [ColorRole#SURFACE_CONTAINER_HIGH], and [ColorRole#SURFACE_CONTAINER_HIGHEST].
///
/// Many of the colors have matching 'on' colors, which are used for drawing
/// content on top of the matching color. For example, if something is using
/// [ColorRole#PRIMARY] for a background color, [ColorRole#ON_PRIMARY] would be used to paint text
/// and icons on top of it. For this reason, the 'on' colors should have a
/// contrast ratio with their matching colors of at least 4.5:1 in order to
/// be readable. On '-FixedVariant' roles, such as [ColorRole#ON_PRIMARY_FIXED_VARIANT],
/// also have the same color between light and dark themes, but compared
/// with on '-Fixed' roles, such as [ColorRole#ON_PRIMARY_FIXED], they provide a
/// lower-emphasis option for text and icons.
public final class ColorScheme {

    static final Color FALLBACK_COLOR = Color.web("#4285f4");
    static final int MAX_DIMENSION = 112;

    // Scale image size down to reduce computation time of color extraction.
    static int[] imageToScaled(Image image) {
        if (image.isBackgroundLoading()) {
            throw new IllegalArgumentException("The image data is not ready yet");
        }

        if (image.isError()) {
            throw new IllegalArgumentException("The image was not loaded successfully", image.getException());
        }

        int sourceWidth = (int) image.getWidth();
        int sourceHeight = (int) image.getHeight();

        if (sourceWidth <= 0 || sourceHeight <= 0) {
            throw new IllegalArgumentException("Image dimensions must be greater than zero.");
        }

        PixelReader pixelReader = image.getPixelReader();
        if (pixelReader == null) {
            throw new IllegalArgumentException("Unable to read pixels of image");
        }

        if (sourceWidth <= MAX_DIMENSION && sourceHeight <= MAX_DIMENSION) {
            int[] result = new int[sourceWidth * sourceHeight];
            pixelReader.getPixels(0, 0,
                    sourceWidth, sourceHeight,
                    PixelFormat.getIntArgbInstance(),
                    result,
                    0, sourceWidth);
            return result;
        }

        int targetWidth = Math.min(sourceWidth, MAX_DIMENSION);
        int targetHeight = Math.min(sourceHeight, MAX_DIMENSION);

        double xScale = (double) sourceWidth / (double) targetWidth;
        double yScale = (double) sourceHeight / (double) targetHeight;

        int[] result = new int[targetWidth * targetHeight];

        for (int y = 0; y < targetHeight; y++) {
            int resultOffset = y * targetWidth;

            for (int x = 0; x < targetWidth; x++) {
                result[resultOffset + x] = pixelReader.getArgb((int) (x * xScale), (int) (y * yScale));
            }
        }

        return result;
    }

    static Color extractColor(Image image, Color fallbackColor) {
        int[] imageData = imageToScaled(image);
        Map<Integer, Integer> quantizeResult = QuantizerCelebi.quantize(imageData, 128);

        // Score colors for color scheme suitability.
        final List<Integer> scoredResults = Score.score(quantizeResult, 1, ColorUtils.argbFromFx(fallbackColor));
        return ColorUtils.fxFromArgb(scoredResults.get(0));
    }

    public static @NotNull ColorScheme fromImage(@NotNull Image image) {
        return fromSeed(extractColor(image, ColorScheme.FALLBACK_COLOR));
    }

    public static ColorScheme fromSeed(@NotNull Color seedColor) {
        return new ColorScheme(
                ColorStyle.TONAL_SPOT,
                false,
                Contrast.DEFAULT.getValue(),
                ColorPlatform.DEFAULT,
                ColorSpecVersion.DEFAULT,
                Objects.requireNonNull(seedColor), null, null, null, null, null
        );
    }

    public static ColorSchemeBuilder newBuilder() {
        return new ColorSchemeBuilder();
    }

    public static ColorSchemeBuilder newBuilder(ColorScheme oldScheme) {
        return newBuilder()
                .setColorStyle(oldScheme.getColorStyle())
                .setBrightness(oldScheme.getBrightness())
                .setContrast(Contrast.of(oldScheme.getContrastLevel()))
                .setPlatform(oldScheme.getPlatform())
                .setSpecVersion(oldScheme.getSpecVersion())
                .setPrimaryColorSeed(oldScheme.getPrimaryColorSeed())
                .setSecondaryColorSeed(oldScheme.getSecondaryColorSeed())
                .setTertiaryColorSeed(oldScheme.getTertiaryColorSeed())
                .setNeutralColorSeed(oldScheme.getNeutralColorSeed())
                .setNeutralVariantColorSeed(oldScheme.getNeutralVariantColorSeed())
                .setErrorColorSeed(oldScheme.getErrorColorSeed());
    }

    private final @NotNull DynamicScheme scheme;

    private final @NotNull Color primaryColorSeed;
    private final @Nullable Color secondaryColorSeed;
    private final @Nullable Color tertiaryColorSeed;
    private final @Nullable Color neutralColorSeed;
    private final @Nullable Color neutralVariantColorSeed;
    private final @Nullable Color errorColorSeed;

    private final Color[] colors = new Color[ColorRole.ALL.size()];

    ColorScheme(
            @NotNull ColorStyle variant,
            boolean isDark,
            double contrastLevel,
            @NotNull ColorPlatform platform,
            @NotNull ColorSpecVersion specVersion,
            @Nullable Color primaryColorSeed,
            @Nullable Color secondaryColorSeed,
            @Nullable Color tertiaryColorSeed,
            @Nullable Color neutralColorSeed,
            @Nullable Color neutralVariantColorSeed,
            @Nullable Color errorColorSeed) {
        if (primaryColorSeed == null)
            primaryColorSeed = FALLBACK_COLOR;
        Hct primaryColorHct = Hct.fromFx(primaryColorSeed);
        @Nullable Hct errorColorHct = errorColorSeed != null ? Hct.fromFx(errorColorSeed) : null;

        specVersion = DynamicScheme.maybeFallbackSpecVersion(specVersion, variant);

        ColorSpec colorSpec = ColorSpecs.get(specVersion);
        this.scheme = new DynamicScheme(
                primaryColorHct,
                variant,
                isDark,
                contrastLevel,
                platform,
                specVersion,
                colorSpec.getPrimaryPalette(variant, primaryColorHct, isDark, platform, contrastLevel),
                secondaryColorSeed == null
                        ? colorSpec.getSecondaryPalette(variant, primaryColorHct, isDark, platform, contrastLevel)
                        : colorSpec.getPrimaryPalette(variant, Hct.fromFx(secondaryColorSeed), isDark, platform, contrastLevel),
                tertiaryColorSeed == null
                        ? colorSpec.getTertiaryPalette(variant, primaryColorHct, isDark, platform, contrastLevel)
                        : colorSpec.getPrimaryPalette(variant, Hct.fromFx(tertiaryColorSeed), isDark, platform, contrastLevel),
                neutralColorSeed == null
                        ? colorSpec.getNeutralPalette(variant, primaryColorHct, isDark, platform, contrastLevel)
                        : colorSpec.getNeutralPalette(variant, Hct.fromFx(neutralColorSeed), isDark, platform, contrastLevel),
                neutralVariantColorSeed == null
                        ? colorSpec.getNeutralVariantPalette(variant, primaryColorHct, isDark, platform, contrastLevel)
                        : colorSpec.getNeutralVariantPalette(variant, Hct.fromFx(neutralVariantColorSeed), isDark, platform, contrastLevel),
                errorColorSeed == null
                        ? Optional.empty()
                        : Optional.of(colorSpec.getErrorPalette(variant, errorColorHct, isDark, platform, contrastLevel)
                        .orElseGet(() -> colorSpec.getPrimaryPalette(variant, errorColorHct, isDark, platform, contrastLevel)))
        );
        this.primaryColorSeed = primaryColorSeed;
        this.secondaryColorSeed = secondaryColorSeed;
        this.tertiaryColorSeed = tertiaryColorSeed;
        this.neutralColorSeed = neutralColorSeed;
        this.neutralVariantColorSeed = neutralVariantColorSeed;
        this.errorColorSeed = errorColorSeed;
    }

    public @NotNull Brightness getBrightness() {
        return scheme.isDark ? Brightness.DARK : Brightness.LIGHT;
    }

    public double getContrastLevel() {
        return scheme.contrastLevel;
    }

    public @NotNull ColorStyle getColorStyle() {
        return scheme.variant;
    }

    public @NotNull ColorPlatform getPlatform() {
        return scheme.platform;
    }

    public @NotNull ColorSpecVersion getSpecVersion() {
        return scheme.specVersion;
    }

    public @NotNull Color getPrimaryColorSeed() {
        return primaryColorSeed;
    }

    public @Nullable Color getSecondaryColorSeed() {
        return secondaryColorSeed;
    }

    public @Nullable Color getTertiaryColorSeed() {
        return tertiaryColorSeed;
    }

    public @Nullable Color getNeutralColorSeed() {
        return neutralColorSeed;
    }

    public @Nullable Color getNeutralVariantColorSeed() {
        return neutralVariantColorSeed;
    }

    public @Nullable Color getErrorColorSeed() {
        return errorColorSeed;
    }

    public Color getColor(@NotNull ColorRole role) {
        Color color = colors[role.ordinal()];
        if (color == null) {
            color = ColorUtils.fxFromArgb(scheme.getArgb(role.accessor.apply(ColorSpecs.get(getSpecVersion()))));
            colors[role.ordinal()] = color;
        }
        return color;
    }

    /// @see ColorRole#PRIMARY
    public Color getPrimary() {
        return getColor(ColorRole.PRIMARY);
    }

    /// @see ColorRole#ON_PRIMARY
    public Color getOnPrimary() {
        return getColor(ColorRole.ON_PRIMARY);
    }

    /// @see ColorRole#PRIMARY_CONTAINER
    public Color getPrimaryContainer() {
        return getColor(ColorRole.PRIMARY_CONTAINER);
    }

    /// @see ColorRole#ON_PRIMARY_CONTAINER
    public Color getOnPrimaryContainer() {
        return getColor(ColorRole.ON_PRIMARY_CONTAINER);
    }

    /// @see ColorRole#PRIMARY_FIXED
    public Color getPrimaryFixed() {
        return getColor(ColorRole.PRIMARY_FIXED);
    }

    /// @see ColorRole#PRIMARY_FIXED_DIM
    public Color getPrimaryFixedDim() {
        return getColor(ColorRole.PRIMARY_FIXED_DIM);
    }

    /// @see ColorRole#ON_PRIMARY_FIXED
    public Color getOnPrimaryFixed() {
        return getColor(ColorRole.ON_PRIMARY_FIXED);
    }

    /// @see ColorRole#ON_PRIMARY_FIXED_VARIANT
    public Color getOnPrimaryFixedVariant() {
        return getColor(ColorRole.ON_PRIMARY_FIXED_VARIANT);
    }

    /// @see ColorRole#SECONDARY
    public Color getSecondary() {
        return getColor(ColorRole.SECONDARY);
    }

    /// @see ColorRole#ON_SECONDARY
    public Color getOnSecondary() {
        return getColor(ColorRole.ON_SECONDARY);
    }

    /// @see ColorRole#SECONDARY_CONTAINER
    public Color getSecondaryContainer() {
        return getColor(ColorRole.SECONDARY_CONTAINER);
    }

    /// @see ColorRole#ON_SECONDARY_CONTAINER
    public Color getOnSecondaryContainer() {
        return getColor(ColorRole.ON_SECONDARY_CONTAINER);
    }

    /// @see ColorRole#SECONDARY_FIXED
    public Color getSecondaryFixed() {
        return getColor(ColorRole.SECONDARY_FIXED);
    }

    /// @see ColorRole#SECONDARY_FIXED_DIM
    public Color getSecondaryFixedDim() {
        return getColor(ColorRole.SECONDARY_FIXED_DIM);
    }

    /// @see ColorRole#ON_SECONDARY_FIXED
    public Color getOnSecondaryFixed() {
        return getColor(ColorRole.ON_SECONDARY_FIXED);
    }

    /// @see ColorRole#ON_SECONDARY_FIXED_VARIANT
    public Color getOnSecondaryFixedVariant() {
        return getColor(ColorRole.ON_SECONDARY_FIXED_VARIANT);
    }

    /// @see ColorRole#TERTIARY
    public Color getTertiary() {
        return getColor(ColorRole.TERTIARY);
    }

    /// @see ColorRole#ON_TERTIARY
    public Color getOnTertiary() {
        return getColor(ColorRole.ON_TERTIARY);
    }

    /// @see ColorRole#TERTIARY_CONTAINER
    public Color getTertiaryContainer() {
        return getColor(ColorRole.TERTIARY_CONTAINER);
    }

    /// @see ColorRole#ON_TERTIARY_CONTAINER
    public Color getOnTertiaryContainer() {
        return getColor(ColorRole.ON_TERTIARY_CONTAINER);
    }

    /// @see ColorRole#TERTIARY_FIXED
    public Color getTertiaryFixed() {
        return getColor(ColorRole.TERTIARY_FIXED);
    }

    /// @see ColorRole#TERTIARY_FIXED_DIM
    public Color getTertiaryFixedDim() {
        return getColor(ColorRole.TERTIARY_FIXED_DIM);
    }

    /// @see ColorRole#ON_TERTIARY_FIXED
    public Color getOnTertiaryFixed() {
        return getColor(ColorRole.ON_TERTIARY_FIXED);
    }

    /// @see ColorRole#ON_TERTIARY_FIXED_VARIANT
    public Color getOnTertiaryFixedVariant() {
        return getColor(ColorRole.ON_TERTIARY_FIXED_VARIANT);
    }

    /// @see ColorRole#ERROR
    public Color getError() {
        return getColor(ColorRole.ERROR);
    }

    /// @see ColorRole#ON_ERROR
    public Color getOnError() {
        return getColor(ColorRole.ON_ERROR);
    }

    /// @see ColorRole#ERROR_CONTAINER
    public Color getErrorContainer() {
        return getColor(ColorRole.ERROR_CONTAINER);
    }

    /// @see ColorRole#ON_ERROR_CONTAINER
    public Color getOnErrorContainer() {
        return getColor(ColorRole.ON_ERROR_CONTAINER);
    }

    /// @see ColorRole#SURFACE
    public Color getSurface() {
        return getColor(ColorRole.SURFACE);
    }

    /// @see ColorRole#ON_SURFACE
    public Color getOnSurface() {
        return getColor(ColorRole.ON_SURFACE);
    }

    /// @see ColorRole#SURFACE_DIM
    public Color getSurfaceDim() {
        return getColor(ColorRole.SURFACE_DIM);
    }

    /// @see ColorRole#SURFACE_BRIGHT
    public Color getSurfaceBright() {
        return getColor(ColorRole.SURFACE_BRIGHT);
    }

    /// @see ColorRole#SURFACE_CONTAINER_LOWEST
    public Color getSurfaceContainerLowest() {
        return getColor(ColorRole.SURFACE_CONTAINER_LOWEST);
    }

    /// @see ColorRole#SURFACE_CONTAINER_LOW
    public Color getSurfaceContainerLow() {
        return getColor(ColorRole.SURFACE_CONTAINER_LOW);
    }

    /// @see ColorRole#SURFACE_CONTAINER
    public Color getSurfaceContainer() {
        return getColor(ColorRole.SURFACE_CONTAINER);
    }

    /// @see ColorRole#SURFACE_CONTAINER_HIGH
    public Color getSurfaceContainerHigh() {
        return getColor(ColorRole.SURFACE_CONTAINER_HIGH);
    }

    /// @see ColorRole#SURFACE_CONTAINER_HIGHEST
    public Color getSurfaceContainerHighest() {
        return getColor(ColorRole.SURFACE_CONTAINER_HIGHEST);
    }

    /// @see ColorRole#SURFACE_VARIANT
    public Color getSurfaceVariant() {
        return getColor(ColorRole.SURFACE_VARIANT);
    }

    /// @see ColorRole#ON_SURFACE_VARIANT
    public Color getOnSurfaceVariant() {
        return getColor(ColorRole.ON_SURFACE_VARIANT);
    }

    /// @see ColorRole#BACKGROUND
    public Color getBackground() {
        return getColor(ColorRole.BACKGROUND);
    }

    /// @see ColorRole#ON_BACKGROUND
    public Color getOnBackground() {
        return getColor(ColorRole.ON_BACKGROUND);
    }

    /// @see ColorRole#OUTLINE
    public Color getOutline() {
        return getColor(ColorRole.OUTLINE);
    }

    /// @see ColorRole#OUTLINE_VARIANT
    public Color getOutlineVariant() {
        return getColor(ColorRole.OUTLINE_VARIANT);
    }

    /// @see ColorRole#SHADOW
    public Color getShadow() {
        return getColor(ColorRole.SHADOW);
    }

    /// @see ColorRole#SCRIM
    public Color getScrim() {
        return getColor(ColorRole.SCRIM);
    }

    /// @see ColorRole#INVERSE_SURFACE
    public Color getInverseSurface() {
        return getColor(ColorRole.INVERSE_SURFACE);
    }

    /// @see ColorRole#INVERSE_ON_SURFACE
    public Color getInverseOnSurface() {
        return getColor(ColorRole.INVERSE_ON_SURFACE);
    }

    /// @see ColorRole#INVERSE_PRIMARY
    public Color getInversePrimary() {
        return getColor(ColorRole.INVERSE_PRIMARY);
    }

    /// @see ColorRole#SURFACE_TINT
    public Color getSurfaceTint() {
        return getColor(ColorRole.SURFACE_TINT);
    }

    public String toStyleSheet() {
        return toStyleSheet(null, null, null);
    }

    public String toStyleSheet(@Nullable String cssClass, @Nullable String prefix, @Nullable Iterable<ColorRole> colorRoles) {
        StringBuilder builder = new StringBuilder();

        if (cssClass != null) {
            builder.append('.').append(cssClass).append(" {\n");
        } else {
            builder.append("* {\n");
        }

        if (prefix == null) {
            prefix = ColorRole.DEFAULT_VARIABLE_NAME_PREFIX;
        }

        if (colorRoles == null) {
            colorRoles = ColorRole.ALL;
        }

        for (ColorRole colorRole : colorRoles) {
            Color color = getColor(colorRole);
            int r = (int) Math.round(color.getRed() * 255.0);
            int g = (int) Math.round(color.getGreen() * 255.0);
            int b = (int) Math.round(color.getBlue() * 255.0);

            builder.append("  ").append(prefix).append('-').append(colorRole.variableNameBase)
                    .append(": rgb(").append(r).append(", ").append(g).append(", ").append(b).append(");\n");
        }

        builder.append("}\n");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.scheme.variant, this.scheme.isDark, this.scheme.platform, this.scheme.contrastLevel, this.scheme.specVersion,
                this.primaryColorSeed,
                this.secondaryColorSeed,
                this.tertiaryColorSeed,
                this.neutralColorSeed,
                this.neutralVariantColorSeed,
                this.errorColorSeed
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof ColorScheme)) {
            return false;
        }

        ColorScheme that = (ColorScheme) obj;
        return this.scheme.variant == that.scheme.variant
                && this.scheme.isDark == that.scheme.isDark
                && this.scheme.platform == that.scheme.platform
                && this.scheme.contrastLevel == that.scheme.contrastLevel
                && this.scheme.specVersion == that.scheme.specVersion
                && this.primaryColorSeed.equals(that.primaryColorSeed)
                && Objects.equals(this.secondaryColorSeed, that.secondaryColorSeed)
                && Objects.equals(this.tertiaryColorSeed, that.tertiaryColorSeed)
                && Objects.equals(this.neutralColorSeed, that.neutralColorSeed)
                && Objects.equals(this.neutralVariantColorSeed, that.neutralVariantColorSeed)
                && Objects.equals(this.errorColorSeed, that.errorColorSeed);
    }

    @Override
    public String toString() {
        return toStyleSheet();
    }
}
