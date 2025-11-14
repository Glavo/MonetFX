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

import org.glavo.monetfx.internal.dynamiccolor.ColorSpec;
import org.glavo.monetfx.internal.dynamiccolor.DynamicColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public enum ColorRole {
    PRIMARY(ColorSpec::primary),
    ON_PRIMARY(ColorSpec::onPrimary),
    PRIMARY_CONTAINER(ColorSpec::primaryContainer),
    ON_PRIMARY_CONTAINER(ColorSpec::onPrimaryContainer),
    PRIMARY_FIXED(ColorSpec::primaryFixed),
    PRIMARY_FIXED_DIM(ColorSpec::primaryFixedDim),
    ON_PRIMARY_FIXED(ColorSpec::onPrimaryFixed),
    ON_PRIMARY_FIXED_VARIANT(ColorSpec::onPrimaryFixedVariant),
    SECONDARY(ColorSpec::secondary),
    ON_SECONDARY(ColorSpec::onSecondary),
    SECONDARY_CONTAINER(ColorSpec::secondaryContainer),
    ON_SECONDARY_CONTAINER(ColorSpec::onSecondaryContainer),
    SECONDARY_FIXED(ColorSpec::secondaryFixed),
    SECONDARY_FIXED_DIM(ColorSpec::secondaryFixedDim),
    ON_SECONDARY_FIXED(ColorSpec::onSecondaryFixed),
    ON_SECONDARY_FIXED_VARIANT(ColorSpec::onSecondaryFixedVariant),
    TERTIARY(ColorSpec::tertiary),
    ON_TERTIARY(ColorSpec::onTertiary),
    TERTIARY_CONTAINER(ColorSpec::tertiaryContainer),
    ON_TERTIARY_CONTAINER(ColorSpec::onTertiaryContainer),
    TERTIARY_FIXED(ColorSpec::tertiaryFixed),
    TERTIARY_FIXED_DIM(ColorSpec::tertiaryFixedDim),
    ON_TERTIARY_FIXED(ColorSpec::onTertiaryFixed),
    ON_TERTIARY_FIXED_VARIANT(ColorSpec::onTertiaryFixedVariant),
    ERROR(ColorSpec::error),
    ON_ERROR(ColorSpec::onError),
    ERROR_CONTAINER(ColorSpec::errorContainer),
    ON_ERROR_CONTAINER(ColorSpec::onErrorContainer),
    SURFACE(ColorSpec::surface),
    ON_SURFACE(ColorSpec::onSurface),
    SURFACE_DIM(ColorSpec::surfaceDim),
    SURFACE_BRIGHT(ColorSpec::surfaceBright),
    SURFACE_CONTAINER_LOWEST(ColorSpec::surfaceContainerLowest),
    SURFACE_CONTAINER_LOW(ColorSpec::surfaceContainerLow),
    SURFACE_CONTAINER(ColorSpec::surfaceContainer),
    SURFACE_CONTAINER_HIGH(ColorSpec::surfaceContainerHigh),
    SURFACE_CONTAINER_HIGHEST(ColorSpec::surfaceContainerHighest),
    SURFACE_VARIANT(ColorSpec::surfaceVariant),
    ON_SURFACE_VARIANT(ColorSpec::onSurfaceVariant),
    BACKGROUND(ColorSpec::background),
    ON_BACKGROUND(ColorSpec::onBackground),
    OUTLINE(ColorSpec::outline),
    OUTLINE_VARIANT(ColorSpec::outlineVariant),
    SHADOW(ColorSpec::shadow),
    SCRIM(ColorSpec::scrim),
    INVERSE_SURFACE(ColorSpec::inverseSurface),
    INVERSE_ON_SURFACE(ColorSpec::inverseOnSurface),
    INVERSE_PRIMARY(ColorSpec::inversePrimary),
    SURFACE_TINT(ColorSpec::surfaceTint);

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
    final Function<ColorSpec, DynamicColor> accessor;

    ColorRole(Function<ColorSpec, DynamicColor> accessor) {
        this.accessor = accessor;

        String[] parts = this.name().split("_");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].charAt(0) + parts[i].substring(1).toLowerCase(Locale.ROOT);
        }
        displayName = String.join(" ", parts);
    }

    final String variableNameBase = name().toLowerCase(Locale.ROOT).replace("_", "-");
    final String defaultVariableName = DEFAULT_VARIABLE_NAME_PREFIX + "-" + variableNameBase;

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
