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

import org.glavo.monetfx.internal.dynamiccolor.DynamicScheme;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/// Like the "numbers" on a paint-by-number canvas, color roles are assigned to specific UI elements.
/// They have semantic names like primary, on primary, and primary container, and matching color tokens.
///
/// @see <a href="https://m3.material.io/styles/color/system/how-the-system-works#b5f5d45a-280b-4184-b999-37609f84d165">Color roles - Material Design 3</a>
@SuppressWarnings("DeprecatedIsStillUsed")
public enum ColorRole {
    /// The color displayed most frequently across your app’s screens and components.
    PRIMARY,

    /// A color that's clearly legible when drawn on [PRIMARY][#PRIMARY].
    ///
    /// To ensure that an app is accessible, a contrast ratio between
    /// [PRIMARY][#PRIMARY] and [ON_PRIMARY][#ON_PRIMARY] of at least 4.5:1 is recommended.
    ///
    /// @see <a href="https://www.w3.org/TR/UNDERSTANDING-WCAG20/visual-audio-contrast-contrast.html">Understanding WCAG 2.0</a>
    ON_PRIMARY,

    /// A color used for elements needing less emphasis than [PRIMARY][#PRIMARY].
    PRIMARY_CONTAINER,

    /// A color that's clearly legible when drawn on [PRIMARY_CONTAINER][#PRIMARY_CONTAINER].
    ///
    /// To ensure that an app is accessible, a contrast ratio between
    /// [PRIMARY_CONTAINER][#PRIMARY_CONTAINER] and [ON_PRIMARY_CONTAINER][#ON_PRIMARY_CONTAINER] of at least 4.5:1
    /// is recommended.
    ///
    /// @see <a href="https://www.w3.org/TR/UNDERSTANDING-WCAG20/visual-audio-contrast-contrast.html">Understanding WCAG 2.0</a>
    ON_PRIMARY_CONTAINER,

    /// A substitute for [PRIMARY_CONTAINER][#PRIMARY_CONTAINER] that's the same color for the dark and light themes.
    PRIMARY_FIXED,

    /// A color used for elements needing more emphasis than [PRIMARY_FIXED][#PRIMARY_FIXED].
    PRIMARY_FIXED_DIM,

    /// A color that is used for text and icons that exist on top of elements having [PRIMARY_FIXED][#PRIMARY_FIXED] color.
    ON_PRIMARY_FIXED,

    /// A color that provides a lower-emphasis option for text and icons than [ON_PRIMARY_FIXED][#ON_PRIMARY_FIXED].
    ON_PRIMARY_FIXED_VARIANT,

    /// An accent color used for less prominent components in the UI, such as
    /// filter chips, while expanding the opportunity for color expression.
    SECONDARY,

    /// A color that's clearly legible when drawn on [SECONDARY][#SECONDARY].
    ///
    /// To ensure that an app is accessible, a contrast ratio between
    /// [SECONDARY][#SECONDARY] and [ON_SECONDARY][#ON_SECONDARY] of at least 4.5:1 is recommended.
    ///
    /// @see <a href="https://www.w3.org/TR/UNDERSTANDING-WCAG20/visual-audio-contrast-contrast.html">Understanding WCAG 2.0</a>
    ON_SECONDARY,

    /// A color used for elements needing less emphasis than [SECONDARY][#SECONDARY].
    SECONDARY_CONTAINER,

    /// A color that's clearly legible when drawn on [SECONDARY_CONTAINER][#SECONDARY_CONTAINER].
    ///
    /// To ensure that an app is accessible, a contrast ratio between
    /// [SECONDARY_CONTAINER][#SECONDARY_CONTAINER] and [ON_SECONDARY_CONTAINER][#ON_SECONDARY_CONTAINER] of at least 4.5:1 is
    /// recommended.
    ///
    /// @see <a href="https://www.w3.org/TR/UNDERSTANDING-WCAG20/visual-audio-contrast-contrast.html">Understanding WCAG 2.0</a>
    ON_SECONDARY_CONTAINER,

    /// A substitute for [SECONDARY_CONTAINER][#SECONDARY_CONTAINER] that's the same color for the dark and light themes.
    SECONDARY_FIXED,

    /// A color used for elements needing more emphasis than [SECONDARY_FIXED][#SECONDARY_FIXED].
    SECONDARY_FIXED_DIM,

    /// A color that is used for text and icons that exist on top of elements having [ON_SECONDARY_FIXED][#ON_SECONDARY_FIXED] color.
    ON_SECONDARY_FIXED,

    /// A color that provides a lower-emphasis option for text and icons than [ON_SECONDARY_FIXED][#ON_SECONDARY_FIXED].
    ON_SECONDARY_FIXED_VARIANT,

    /// A color used as a contrasting accent that can balance [PRIMARY][#PRIMARY]
    /// and [SECONDARY][#SECONDARY] colors or bring heightened attention to an element,
    /// such as an input field.
    TERTIARY,

    /// A color that's clearly legible when drawn on [TERTIARY][#TERTIARY].
    ///
    /// To ensure that an app is accessible, a contrast ratio between
    /// [TERTIARY][#TERTIARY] and [ON_TERTIARY][#ON_TERTIARY] of at least 4.5:1 is recommended.
    ON_TERTIARY,

    /// A color used for elements needing less emphasis than [TERTIARY][#TERTIARY].
    TERTIARY_CONTAINER,

    /// A color that's clearly legible when drawn on [TERTIARY_CONTAINER][#TERTIARY_CONTAINER].
    ///
    /// To ensure that an app is accessible, a contrast ratio between
    /// [TERTIARY_CONTAINER][#TERTIARY_CONTAINER] and [ON_TERTIARY_CONTAINER][#ON_TERTIARY_CONTAINER] of at least 4.5:1 is
    /// recommended.
    ///
    /// @see <a href="https://www.w3.org/TR/UNDERSTANDING-WCAG20/visual-audio-contrast-contrast.html">Understanding WCAG 2.0</a>
    ON_TERTIARY_CONTAINER,

    /// A substitute for [TERTIARY_CONTAINER][#TERTIARY_CONTAINER] that's the same color for dark and light themes.
    TERTIARY_FIXED,

    /// A color used for elements needing more emphasis than [TERTIARY_FIXED][#TERTIARY_FIXED].
    TERTIARY_FIXED_DIM,

    /// A color that is used for text and icons that exist on top of elements having [TERTIARY_FIXED][#TERTIARY_FIXED] color.
    ON_TERTIARY_FIXED,

    /// A color that provides a lower-emphasis option for text and icons than [ON_TERTIARY_FIXED][#ON_TERTIARY_FIXED].
    ON_TERTIARY_FIXED_VARIANT,

    /// The color to use for input validation errors.
    ERROR,

    /// A color that's clearly legible when drawn on [ERROR][#ERROR].
    ///
    /// To ensure that an app is accessible, a contrast ratio between
    /// [ERROR][#ERROR] and [ON_ERROR][#ON_ERROR] of at least 4.5:1 is recommended.
    ON_ERROR,

    /// A color used for error elements needing less emphasis than [ERROR][#ERROR].
    ERROR_CONTAINER,

    /// A color that's clearly legible when drawn on [ERROR_CONTAINER][#ERROR_CONTAINER].
    ///
    /// To ensure that an app is accessible, a contrast ratio between
    /// [ERROR_CONTAINER][#ERROR_CONTAINER] and [ON_ERROR_CONTAINER][#ON_ERROR_CONTAINER] of at least 4.5:1 is
    /// recommended.
    ON_ERROR_CONTAINER,

    /// The background color for widgets.
    SURFACE,

    /// A color that's clearly legible when drawn on [SURFACE][#SURFACE].
    ///
    /// To ensure that an app is accessible, a contrast ratio between
    /// [SURFACE][#SURFACE] and [ON_SURFACE][#ON_SURFACE] of at least 4.5:1 is recommended.
    ON_SURFACE,

    /// A color that's always darkest in the dark or light theme.
    SURFACE_DIM,

    /// A color that's always the lightest in the dark or light theme.
    SURFACE_BRIGHT,

    /// A surface container color with the lightest tone and the least emphasis relative to the surface.
    SURFACE_CONTAINER_LOWEST,

    /// A surface container color with a lighter tone that creates less emphasis
    /// than [SURFACE_CONTAINER][#SURFACE_CONTAINER] but more emphasis than [SURFACE_CONTAINER_LOWEST][#SURFACE_CONTAINER_LOWEST].
    SURFACE_CONTAINER_LOW,

    /// A recommended color role for a distinct area within the surface.
    ///
    /// Surface container color roles are independent of elevation. They replace the old
    /// opacity-based model which applied a tinted overlay on top of
    /// surfaces based on their elevation.
    ///
    /// Surface container colors include [SURFACE_CONTAINER_LOWEST][#SURFACE_CONTAINER_LOWEST],
    /// [SURFACE_CONTAINER_LOW][#SURFACE_CONTAINER_LOW], [SURFACE_CONTAINER][#SURFACE_CONTAINER],
    /// [SURFACE_CONTAINER_HIGH][#SURFACE_CONTAINER_HIGH] and [SURFACE_CONTAINER_HIGHEST][#SURFACE_CONTAINER_HIGHEST].
    SURFACE_CONTAINER,

    /// A surface container color with a darker tone. It is used to create more
    /// emphasis than [SURFACE_CONTAINER][#SURFACE_CONTAINER] but less emphasis than [SURFACE_CONTAINER_HIGHEST][#SURFACE_CONTAINER_HIGHEST].
    SURFACE_CONTAINER_HIGH,

    /// A surface container color with the darkest tone. It is used to create the
    /// most emphasis against the surface.
    SURFACE_CONTAINER_HIGHEST,

    /// A color variant of [SURFACE][#SURFACE] that can be used for differentiation against
    /// a component using [SURFACE][#SURFACE].
    ///
    /// @deprecated Use [SURFACE_CONTAINER_HIGHEST][#SURFACE_CONTAINER_HIGHEST] instead.
    SURFACE_VARIANT,

    /// A color that's clearly legible when drawn on [SURFACE_VARIANT][#SURFACE_VARIANT].
    ///
    /// To ensure that an app is accessible, a contrast ratio between
    /// [SURFACE_VARIANT][#SURFACE_VARIANT] and [ON_SURFACE_VARIANT][#ON_SURFACE_VARIANT] of at least 4.5:1 is
    /// recommended.
    ///
    /// @see <a href="https://www.w3.org/TR/UNDERSTANDING-WCAG20/visual-audio-contrast-contrast.html">Understanding WCAG 2.0</a>
    ON_SURFACE_VARIANT,

    /// A color that typically appears behind scrollable content.
    ///
    /// @deprecated Use [SURFACE][#SURFACE] instead.
    BACKGROUND,

    /// A color that's clearly legible when drawn on [BACKGROUND][#BACKGROUND].
    ///
    /// To ensure that an app is accessible, a contrast ratio between
    /// [BACKGROUND][#BACKGROUND] and [ON_BACKGROUND][#ON_BACKGROUND] of at least 4.5:1 is recommended.
    ///
    /// @see <a href="https://www.w3.org/TR/UNDERSTANDING-WCAG20/visual-audio-contrast-contrast.html">Understanding WCAG 2.0</a>
    /// @deprecated Use [ON_SURFACE][#ON_SURFACE] instead.
    ON_BACKGROUND,

    /// A utility color that creates boundaries and emphasis to improve usability.
    OUTLINE,

    /// A utility color that creates boundaries for decorative elements when a
    /// 3:1 contrast isn’t required, such as for dividers or decorative elements.
    OUTLINE_VARIANT,

    /// A color use to paint the drop shadows of elevated components.
    SHADOW,

    /// A color use to paint the scrim around of modal components.
    SCRIM,

    /// A surface color used for displaying the reverse of what’s seen in the
    /// surrounding UI, for example in a SnackBar to bring attention to
    /// an alert.
    INVERSE_SURFACE,

    /// A color that's clearly legible when drawn on [INVERSE_SURFACE][#INVERSE_SURFACE].
    ///
    /// To ensure that an app is accessible, a contrast ratio between
    /// [INVERSE_SURFACE][#INVERSE_SURFACE] and [INVERSE_ON_SURFACE][#INVERSE_ON_SURFACE] of at least 4.5:1 is
    /// recommended.
    INVERSE_ON_SURFACE,

    /// An accent color used for displaying a highlight color on [INVERSE_SURFACE][#INVERSE_SURFACE]
    /// backgrounds, like button text in a SnackBar.
    INVERSE_PRIMARY,

    /// A color used as an overlay on a surface color to indicate a component's elevation.
    SURFACE_TINT;

    static final String DEFAULT_VARIABLE_NAME_PREFIX = "-monet";

    public static final List<ColorRole> ALL = Collections.unmodifiableList(Arrays.asList(ColorRole.values()));

    private static String normalizeName(String name) {
        if (name.indexOf('_') >= 0) {
            name = name.replace("_", "");
        } else if (name.indexOf('-') >= 0) {
            name = name.replace("-", "");
        }

        return name.toLowerCase(Locale.ROOT);
    }

    private static final Map<String, ColorRole> searchTable = new HashMap<>();

    static {
        for (ColorRole role : ALL) {
            if (searchTable.put(normalizeName(role.name()), role) != null) {
                throw new AssertionError("Duplicate role: " + role);
            }
        }
    }

    public static @Nullable ColorRole of(@NotNull String role) {
        return searchTable.get(normalizeName(role));
    }

    final String displayName;

    {
        String[] parts = this.name().split("_");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].charAt(0) + parts[i].substring(1).toLowerCase(Locale.ROOT);
        }
        displayName = String.join(" ", parts);
    }

    final String variableNameBase = name().toLowerCase(Locale.ROOT).replace("_", "-");
    final String defaultVariableName = DEFAULT_VARIABLE_NAME_PREFIX + "-" + variableNameBase;

    int getArgb(DynamicScheme scheme) {
        switch (this) {
            case PRIMARY:
                return scheme.getPrimary();
            case ON_PRIMARY:
                return scheme.getOnPrimary();
            case PRIMARY_CONTAINER:
                return scheme.getPrimaryContainer();
            case ON_PRIMARY_CONTAINER:
                return scheme.getOnPrimaryContainer();
            case PRIMARY_FIXED:
                return scheme.getPrimaryFixed();
            case PRIMARY_FIXED_DIM:
                return scheme.getPrimaryFixedDim();
            case ON_PRIMARY_FIXED:
                return scheme.getOnPrimaryFixed();
            case ON_PRIMARY_FIXED_VARIANT:
                return scheme.getOnPrimaryFixedVariant();
            case SECONDARY:
                return scheme.getSecondary();
            case ON_SECONDARY:
                return scheme.getOnSecondary();
            case SECONDARY_CONTAINER:
                return scheme.getSecondaryContainer();
            case ON_SECONDARY_CONTAINER:
                return scheme.getOnSecondaryContainer();
            case SECONDARY_FIXED:
                return scheme.getSecondaryFixed();
            case SECONDARY_FIXED_DIM:
                return scheme.getSecondaryFixedDim();
            case ON_SECONDARY_FIXED:
                return scheme.getOnSecondaryFixed();
            case ON_SECONDARY_FIXED_VARIANT:
                return scheme.getOnSecondaryFixedVariant();
            case TERTIARY:
                return scheme.getTertiary();
            case ON_TERTIARY:
                return scheme.getOnTertiary();
            case TERTIARY_CONTAINER:
                return scheme.getTertiaryContainer();
            case ON_TERTIARY_CONTAINER:
                return scheme.getOnTertiaryContainer();
            case TERTIARY_FIXED:
                return scheme.getTertiaryFixed();
            case TERTIARY_FIXED_DIM:
                return scheme.getTertiaryFixedDim();
            case ON_TERTIARY_FIXED:
                return scheme.getOnTertiaryFixed();
            case ON_TERTIARY_FIXED_VARIANT:
                return scheme.getOnTertiaryFixedVariant();
            case ERROR:
                return scheme.getError();
            case ON_ERROR:
                return scheme.getOnError();
            case ERROR_CONTAINER:
                return scheme.getErrorContainer();
            case ON_ERROR_CONTAINER:
                return scheme.getOnErrorContainer();
            case SURFACE:
                return scheme.getSurface();
            case ON_SURFACE:
                return scheme.getOnSurface();
            case SURFACE_DIM:
                return scheme.getSurfaceDim();
            case SURFACE_BRIGHT:
                return scheme.getSurfaceBright();
            case SURFACE_CONTAINER_LOWEST:
                return scheme.getSurfaceContainerLowest();
            case SURFACE_CONTAINER_LOW:
                return scheme.getSurfaceContainerLow();
            case SURFACE_CONTAINER:
                return scheme.getSurfaceContainer();
            case SURFACE_CONTAINER_HIGH:
                return scheme.getSurfaceContainerHigh();
            case SURFACE_CONTAINER_HIGHEST:
                return scheme.getSurfaceContainerHighest();
            case SURFACE_VARIANT:
                return scheme.getSurfaceVariant();
            case ON_SURFACE_VARIANT:
                return scheme.getOnSurfaceVariant();
            case BACKGROUND:
                return scheme.getBackground();
            case ON_BACKGROUND:
                return scheme.getOnBackground();
            case OUTLINE:
                return scheme.getOutline();
            case OUTLINE_VARIANT:
                return scheme.getOutlineVariant();
            case SHADOW:
                return scheme.getShadow();
            case SCRIM:
                return scheme.getScrim();
            case INVERSE_SURFACE:
                return scheme.getInverseSurface();
            case INVERSE_ON_SURFACE:
                return scheme.getInverseOnSurface();
            case INVERSE_PRIMARY:
                return scheme.getInversePrimary();
            case SURFACE_TINT:
                return scheme.getSurfaceTint();
            default:
                throw new AssertionError("Unknown color role: " + this);
        }
    }

    public String getVariableName(String prefix) {
        return prefix + "-" + variableNameBase;
    }

    public String getVariableName() {
        return defaultVariableName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
