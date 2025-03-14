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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public enum ColorRole {
    PRIMARY,
    ON_PRIMARY,
    PRIMARY_CONTAINER,
    ON_PRIMARY_CONTAINER,
    PRIMARY_FIXED,
    PRIMARY_FIXED_DIM,
    ON_PRIMARY_FIXED,
    ON_PRIMARY_FIXED_VARIANT,
    SECONDARY,
    ON_SECONDARY,
    SECONDARY_CONTAINER,
    ON_SECONDARY_CONTAINER,
    SECONDARY_FIXED,
    SECONDARY_FIXED_DIM,
    ON_SECONDARY_FIXED,
    ON_SECONDARY_FIXED_VARIANT,
    TERTIARY,
    ON_TERTIARY,
    TERTIARY_CONTAINER,
    ON_TERTIARY_CONTAINER,
    TERTIARY_FIXED,
    TERTIARY_FIXED_DIM,
    ON_TERTIARY_FIXED,
    ON_TERTIARY_FIXED_VARIANT,
    ERROR,
    ON_ERROR,
    ERROR_CONTAINER,
    ON_ERROR_CONTAINER,
    SURFACE,
    ON_SURFACE,
    SURFACE_DIM,
    SURFACE_BRIGHT,
    SURFACE_CONTAINER_LOWEST,
    SURFACE_CONTAINER_LOW,
    SURFACE_CONTAINER,
    SURFACE_CONTAINER_HIGH,
    SURFACE_CONTAINER_HIGHEST,
    SURFACE_VARIANT,
    ON_SURFACE_VARIANT,
    BACKGROUND,
    ON_BACKGROUND,
    OUTLINE,
    OUTLINE_VARIANT,
    SHADOW,
    SCRIM,
    INVERSE_SURFACE,
    INVERSE_ON_SURFACE,
    INVERSE_PRIMARY,
    SURFACE_TINT;

    public static final List<ColorRole> ALL = Collections.unmodifiableList(Arrays.asList(ColorRole.values()));

    final String displayName;

    {
        String[] parts = this.name().split("_");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].charAt(0) + parts[i].substring(1).toLowerCase(Locale.ROOT);
        }
        displayName = String.join(" ", parts);
    }

    final String variableNameBase = name().toLowerCase(Locale.ROOT).replace("_", "-");
    final String defaultVariableName = "-monet-" + variableNameBase;


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
        return "-" + prefix + "-" + variableNameBase;
    }

    public String getVariableName() {
        return defaultVariableName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
