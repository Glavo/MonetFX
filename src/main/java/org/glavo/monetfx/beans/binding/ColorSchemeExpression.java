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
package org.glavo.monetfx.beans.binding;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.scene.paint.Color;
import org.glavo.monetfx.ColorRole;
import org.glavo.monetfx.ColorScheme;
import org.glavo.monetfx.beans.value.ObservableColorSchemeValue;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.Objects;

/// `ColorSchemeExpression` is an [ObservableColorSchemeValue] plus additional convenience
/// methods to generate bindings in a fluent style.
///
/// A concrete subclass of `ColorSchemeExpression` has to implement the method
/// [get()][#get()], which provides the actual value of this expression.
public abstract class ColorSchemeExpression implements ObservableColorSchemeValue {

    @SuppressWarnings("unchecked")
    private final WeakReference<ObjectBinding<Color>>[] bindings = new WeakReference[ColorRole.ALL.size()];

    @Override
    public ColorScheme getValue() {
        return get();
    }

    /// Creates a new [BooleanBinding] that holds `true` if this
    /// `ColorSchemeExpression` is `null`.
    ///
    /// @return the new `BooleanBinding`
    public BooleanBinding isNull() {
        return Bindings.isNull(this);
    }

    /// Creates a new [BooleanBinding] that holds `true` if this
    /// `ColorSchemeExpression` is not `null`.
    ///
    /// @return the new `BooleanBinding`
    public BooleanBinding isNotNull() {
        return Bindings.isNotNull(this);
    }

    /// Returns an [ObjectBinding] that holds the color for the specified [ColorRole].
    ///
    /// @return the new `ObjectBinding`
    public ObjectBinding<Color> getColor(@NotNull ColorRole role) {
        Objects.requireNonNull(role);

        WeakReference<ObjectBinding<Color>> bindingRef = bindings[role.ordinal()];
        ObjectBinding<Color> binding;

        if (bindingRef != null && (binding = bindingRef.get()) != null) {
            return binding;
        }

        binding = new ObjectBinding<Color>() {
            {
                bind(ColorSchemeExpression.this);
            }

            @Override
            public void dispose() {
                unbind(ColorSchemeExpression.this);
            }

            @Override
            protected Color computeValue() {
                ColorScheme colorScheme = ColorSchemeExpression.this.get();
                return colorScheme != null ? colorScheme.getColor(role) : null;
            }
        };

        bindings[role.ordinal()] = new WeakReference<>(binding);
        return binding;
    }

    /// @see ColorScheme#getPrimary()
    /// @see ColorRole#PRIMARY
    public ObjectBinding<Color> getPrimary() {
        return getColor(ColorRole.PRIMARY);
    }

    /// @see ColorScheme#getOnPrimary()
    /// @see ColorRole#ON_PRIMARY
    public ObjectBinding<Color> getOnPrimary() {
        return getColor(ColorRole.ON_PRIMARY);
    }

    /// @see ColorScheme#getPrimaryContainer()
    /// @see ColorRole#PRIMARY_CONTAINER
    public ObjectBinding<Color> getPrimaryContainer() {
        return getColor(ColorRole.PRIMARY_CONTAINER);
    }

    /// @see ColorScheme#getOnPrimaryContainer()
    /// @see ColorRole#ON_PRIMARY_CONTAINER
    public ObjectBinding<Color> getOnPrimaryContainer() {
        return getColor(ColorRole.ON_PRIMARY_CONTAINER);
    }

    /// @see ColorScheme#getPrimaryFixed()
    /// @see ColorRole#PRIMARY_FIXED
    public ObjectBinding<Color> getPrimaryFixed() {
        return getColor(ColorRole.PRIMARY_FIXED);
    }

    /// @see ColorScheme#getPrimaryFixedDim()
    /// @see ColorRole#PRIMARY_FIXED_DIM
    public ObjectBinding<Color> getPrimaryFixedDim() {
        return getColor(ColorRole.PRIMARY_FIXED_DIM);
    }

    /// @see ColorScheme#getOnPrimaryFixed()
    /// @see ColorRole#ON_PRIMARY_FIXED
    public ObjectBinding<Color> getOnPrimaryFixed() {
        return getColor(ColorRole.ON_PRIMARY_FIXED);
    }

    /// @see ColorScheme#getOnPrimaryFixedVariant()
    /// @see ColorRole#ON_PRIMARY_FIXED_VARIANT
    public ObjectBinding<Color> getOnPrimaryFixedVariant() {
        return getColor(ColorRole.ON_PRIMARY_FIXED_VARIANT);
    }

    /// @see ColorScheme#getSecondary()
    /// @see ColorRole#SECONDARY
    public ObjectBinding<Color> getSecondary() {
        return getColor(ColorRole.SECONDARY);
    }

    /// @see ColorScheme#getOnSecondary()
    /// @see ColorRole#ON_SECONDARY
    public ObjectBinding<Color> getOnSecondary() {
        return getColor(ColorRole.ON_SECONDARY);
    }

    /// @see ColorScheme#getSecondaryContainer()
    /// @see ColorRole#SECONDARY_CONTAINER
    public ObjectBinding<Color> getSecondaryContainer() {
        return getColor(ColorRole.SECONDARY_CONTAINER);
    }

    /// @see ColorScheme#getOnSecondaryContainer()
    /// @see ColorRole#ON_SECONDARY_CONTAINER
    public ObjectBinding<Color> getOnSecondaryContainer() {
        return getColor(ColorRole.ON_SECONDARY_CONTAINER);
    }

    /// @see ColorScheme#getSecondaryFixed()
    /// @see ColorRole#SECONDARY_FIXED
    public ObjectBinding<Color> getSecondaryFixed() {
        return getColor(ColorRole.SECONDARY_FIXED);
    }

    /// @see ColorScheme#getSecondaryFixedDim()
    /// @see ColorRole#SECONDARY_FIXED_DIM
    public ObjectBinding<Color> getSecondaryFixedDim() {
        return getColor(ColorRole.SECONDARY_FIXED_DIM);
    }

    /// @see ColorScheme#getOnSecondaryFixed()
    /// @see ColorRole#ON_SECONDARY_FIXED
    public ObjectBinding<Color> getOnSecondaryFixed() {
        return getColor(ColorRole.ON_SECONDARY_FIXED);
    }

    /// @see ColorScheme#getOnSecondaryFixedVariant()
    /// @see ColorRole#ON_SECONDARY_FIXED_VARIANT
    public ObjectBinding<Color> getOnSecondaryFixedVariant() {
        return getColor(ColorRole.ON_SECONDARY_FIXED_VARIANT);
    }

    /// @see ColorScheme#getTertiary()
    /// @see ColorRole#TERTIARY
    public ObjectBinding<Color> getTertiary() {
        return getColor(ColorRole.TERTIARY);
    }

    /// @see ColorScheme#getOnTertiary()
    /// @see ColorRole#ON_TERTIARY
    public ObjectBinding<Color> getOnTertiary() {
        return getColor(ColorRole.ON_TERTIARY);
    }

    /// @see ColorScheme#getTertiaryContainer()
    /// @see ColorRole#TERTIARY_CONTAINER
    public ObjectBinding<Color> getTertiaryContainer() {
        return getColor(ColorRole.TERTIARY_CONTAINER);
    }

    /// @see ColorScheme#getOnTertiaryContainer()
    /// @see ColorRole#ON_TERTIARY_CONTAINER
    public ObjectBinding<Color> getOnTertiaryContainer() {
        return getColor(ColorRole.ON_TERTIARY_CONTAINER);
    }

    /// @see ColorScheme#getTertiaryFixed()
    /// @see ColorRole#TERTIARY_FIXED
    public ObjectBinding<Color> getTertiaryFixed() {
        return getColor(ColorRole.TERTIARY_FIXED);
    }

    /// @see ColorScheme#getTertiaryFixedDim()
    /// @see ColorRole#TERTIARY_FIXED_DIM
    public ObjectBinding<Color> getTertiaryFixedDim() {
        return getColor(ColorRole.TERTIARY_FIXED_DIM);
    }

    /// @see ColorScheme#getOnTertiaryFixed()
    /// @see ColorRole#ON_TERTIARY_FIXED
    public ObjectBinding<Color> getOnTertiaryFixed() {
        return getColor(ColorRole.ON_TERTIARY_FIXED);
    }

    /// @see ColorScheme#getOnTertiaryFixedVariant()
    /// @see ColorRole#ON_TERTIARY_FIXED_VARIANT
    public ObjectBinding<Color> getOnTertiaryFixedVariant() {
        return getColor(ColorRole.ON_TERTIARY_FIXED_VARIANT);
    }

    /// @see ColorScheme#getError()
    /// @see ColorRole#ERROR
    public ObjectBinding<Color> getError() {
        return getColor(ColorRole.ERROR);
    }

    /// @see ColorScheme#getOnError()
    /// @see ColorRole#ON_ERROR
    public ObjectBinding<Color> getOnError() {
        return getColor(ColorRole.ON_ERROR);
    }

    /// @see ColorScheme#getErrorContainer()
    /// @see ColorRole#ERROR_CONTAINER
    public ObjectBinding<Color> getErrorContainer() {
        return getColor(ColorRole.ERROR_CONTAINER);
    }

    /// @see ColorScheme#getOnErrorContainer()
    /// @see ColorRole#ON_ERROR_CONTAINER
    public ObjectBinding<Color> getOnErrorContainer() {
        return getColor(ColorRole.ON_ERROR_CONTAINER);
    }

    /// @see ColorScheme#getSurface()
    /// @see ColorRole#SURFACE
    public ObjectBinding<Color> getSurface() {
        return getColor(ColorRole.SURFACE);
    }

    /// @see ColorScheme#getOnSurface()
    /// @see ColorRole#ON_SURFACE
    public ObjectBinding<Color> getOnSurface() {
        return getColor(ColorRole.ON_SURFACE);
    }

    /// @see ColorScheme#getSurfaceDim()
    /// @see ColorRole#SURFACE_DIM
    public ObjectBinding<Color> getSurfaceDim() {
        return getColor(ColorRole.SURFACE_DIM);
    }

    /// @see ColorScheme#getSurfaceBright()
    /// @see ColorRole#SURFACE_BRIGHT
    public ObjectBinding<Color> getSurfaceBright() {
        return getColor(ColorRole.SURFACE_BRIGHT);
    }

    /// @see ColorScheme#getSurfaceContainerLowest()
    /// @see ColorRole#SURFACE_CONTAINER_LOWEST
    public ObjectBinding<Color> getSurfaceContainerLowest() {
        return getColor(ColorRole.SURFACE_CONTAINER_LOWEST);
    }

    /// @see ColorScheme#getSurfaceContainerLow()
    /// @see ColorRole#SURFACE_CONTAINER_LOW
    public ObjectBinding<Color> getSurfaceContainerLow() {
        return getColor(ColorRole.SURFACE_CONTAINER_LOW);
    }

    /// @see ColorScheme#getSurfaceContainer()
    /// @see ColorRole#SURFACE_CONTAINER
    public ObjectBinding<Color> getSurfaceContainer() {
        return getColor(ColorRole.SURFACE_CONTAINER);
    }

    /// @see ColorScheme#getSurfaceContainerHigh()
    /// @see ColorRole#SURFACE_CONTAINER_HIGH
    public ObjectBinding<Color> getSurfaceContainerHigh() {
        return getColor(ColorRole.SURFACE_CONTAINER_HIGH);
    }

    /// @see ColorScheme#getSurfaceContainerHighest()
    /// @see ColorRole#SURFACE_CONTAINER_HIGHEST
    public ObjectBinding<Color> getSurfaceContainerHighest() {
        return getColor(ColorRole.SURFACE_CONTAINER_HIGHEST);
    }

    /// @see ColorScheme#getSurfaceVariant()
    /// @see ColorRole#SURFACE_VARIANT
    /// @deprecated Use [getSurfaceContainerHighest][#getSurfaceContainerHighest()] instead.
    @Deprecated
    public ObjectBinding<Color> getSurfaceVariant() {
        return getColor(ColorRole.SURFACE_VARIANT);
    }

    /// @see ColorScheme#getOnSurfaceVariant()
    /// @see ColorRole#ON_SURFACE_VARIANT
    public ObjectBinding<Color> getOnSurfaceVariant() {
        return getColor(ColorRole.ON_SURFACE_VARIANT);
    }

    /// @see ColorScheme#getBackground()
    /// @see ColorRole#BACKGROUND
    /// @deprecated Use [getSurface][#getSurface()] instead.
    @Deprecated
    public ObjectBinding<Color> getBackground() {
        return getColor(ColorRole.BACKGROUND);
    }

    /// @see ColorScheme#getOnBackground()
    /// @see ColorRole#ON_BACKGROUND
    /// @deprecated Use [getOnSurface][#getOnSurface()] instead.
    @Deprecated
    public ObjectBinding<Color> getOnBackground() {
        return getColor(ColorRole.ON_BACKGROUND);
    }

    /// @see ColorScheme#getOutline()
    /// @see ColorRole#OUTLINE
    public ObjectBinding<Color> getOutline() {
        return getColor(ColorRole.OUTLINE);
    }

    /// @see ColorScheme#getOutlineVariant()
    /// @see ColorRole#OUTLINE_VARIANT
    public ObjectBinding<Color> getOutlineVariant() {
        return getColor(ColorRole.OUTLINE_VARIANT);
    }

    /// @see ColorScheme#getShadow()
    /// @see ColorRole#SHADOW
    public ObjectBinding<Color> getShadow() {
        return getColor(ColorRole.SHADOW);
    }

    /// @see ColorScheme#getScrim()
    /// @see ColorRole#SCRIM
    public ObjectBinding<Color> getScrim() {
        return getColor(ColorRole.SCRIM);
    }

    /// @see ColorScheme#getInverseSurface()
    /// @see ColorRole#INVERSE_SURFACE
    public ObjectBinding<Color> getInverseSurface() {
        return getColor(ColorRole.INVERSE_SURFACE);
    }

    /// @see ColorScheme#getInverseOnSurface()
    /// @see ColorRole#INVERSE_ON_SURFACE
    public ObjectBinding<Color> getInverseOnSurface() {
        return getColor(ColorRole.INVERSE_ON_SURFACE);
    }

    /// @see ColorScheme#getInversePrimary()
    /// @see ColorRole#INVERSE_PRIMARY
    public ObjectBinding<Color> getInversePrimary() {
        return getColor(ColorRole.INVERSE_PRIMARY);
    }

    /// @see ColorScheme#getSurfaceTint()
    /// @see ColorRole#SURFACE_TINT
    public ObjectBinding<Color> getSurfaceTint() {
        return getColor(ColorRole.SURFACE_TINT);
    }

    public StringBinding toStyleSheet() {
        return new StringBinding() {
            {
                bind(ColorSchemeExpression.this);
            }

            @Override
            public void dispose() {
                unbind(ColorSchemeExpression.this);
            }

            @Override
            protected String computeValue() {
                ColorScheme scheme = ColorSchemeExpression.this.get();
                return scheme != null ? scheme.toStyleSheet() : null;
            }
        };
    }
}
