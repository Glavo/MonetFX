/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.glavo.monetfx.internal.dynamiccolor;

import org.glavo.monetfx.ColorStyle;
import org.glavo.monetfx.internal.dislike.DislikeAnalyzer;
import org.glavo.monetfx.internal.hct.Hct;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * Named colors, otherwise known as tokens, or roles, in the Material Design system.
 */
public final class MaterialDynamicColors {
    /**
     * Optionally use fidelity on most color schemes.
     */
    private final boolean isExtendedFidelity;

    public MaterialDynamicColors() {
        this.isExtendedFidelity = false;
    }

    @NotNull
    public DynamicColor highestSurface(@NotNull DynamicScheme s) {
        return s.isDark ? surfaceBright() : surfaceDim();
    }

    @NotNull
    public DynamicColor background() {
        return new DynamicColor(
                /* name= */ "background",
                /* palette= */ (s) -> s.neutralPalette,
                /* tone= */ (s) -> s.isDark ? 6.0 : 98.0,
                /* isBackground= */ true,
                /* background= */ null,
                /* secondBackground= */ null,
                /* contrastCurve= */ null,
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor onBackground() {
        return new DynamicColor(
                /* name= */ "on_background",
                /* palette= */ (s) -> s.neutralPalette,
                /* tone= */ (s) -> s.isDark ? 90.0 : 10.0,
                /* isBackground= */ false,
                /* background= */ (s) -> background(),
                /* secondBackground= */ null,
                /* contrastCurve= */ new ContrastCurve(3.0, 3.0, 4.5, 7.0),
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor surface() {
        return new DynamicColor(
                /* name= */ "surface",
                /* palette= */ (s) -> s.neutralPalette,
                /* tone= */ (s) -> s.isDark ? 6.0 : 98.0,
                /* isBackground= */ true,
                /* background= */ null,
                /* secondBackground= */ null,
                /* contrastCurve= */ null,
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor surfaceDim() {
        return new DynamicColor(
                /* name= */ "surface_dim",
                /* palette= */ (s) -> s.neutralPalette,
                /* tone= */ (s) ->
                s.isDark ? 6.0 : new ContrastCurve(87.0, 87.0, 80.0, 75.0).get(s.contrastLevel),
                /* isBackground= */ true,
                /* background= */ null,
                /* secondBackground= */ null,
                /* contrastCurve= */ null,
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor surfaceBright() {
        return new DynamicColor(
                /* name= */ "surface_bright",
                /* palette= */ (s) -> s.neutralPalette,
                /* tone= */ (s) ->
                s.isDark ? new ContrastCurve(24.0, 24.0, 29.0, 34.0).get(s.contrastLevel) : 98.0,
                /* isBackground= */ true,
                /* background= */ null,
                /* secondBackground= */ null,
                /* contrastCurve= */ null,
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor surfaceContainerLowest() {
        return new DynamicColor(
                /* name= */ "surface_container_lowest",
                /* palette= */ (s) -> s.neutralPalette,
                /* tone= */ (s) ->
                s.isDark ? new ContrastCurve(4.0, 4.0, 2.0, 0.0).get(s.contrastLevel) : 100.0,
                /* isBackground= */ true,
                /* background= */ null,
                /* secondBackground= */ null,
                /* contrastCurve= */ null,
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor surfaceContainerLow() {
        return new DynamicColor(
                /* name= */ "surface_container_low",
                /* palette= */ (s) -> s.neutralPalette,
                /* tone= */ (s) ->
                s.isDark
                        ? new ContrastCurve(10.0, 10.0, 11.0, 12.0).get(s.contrastLevel)
                        : new ContrastCurve(96.0, 96.0, 96.0, 95.0).get(s.contrastLevel),
                /* isBackground= */ true,
                /* background= */ null,
                /* secondBackground= */ null,
                /* contrastCurve= */ null,
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor surfaceContainer() {
        return new DynamicColor(
                /* name= */ "surface_container",
                /* palette= */ (s) -> s.neutralPalette,
                /* tone= */ (s) ->
                s.isDark
                        ? new ContrastCurve(12.0, 12.0, 16.0, 20.0).get(s.contrastLevel)
                        : new ContrastCurve(94.0, 94.0, 92.0, 90.0).get(s.contrastLevel),
                /* isBackground= */ true,
                /* background= */ null,
                /* secondBackground= */ null,
                /* contrastCurve= */ null,
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor surfaceContainerHigh() {
        return new DynamicColor(
                /* name= */ "surface_container_high",
                /* palette= */ (s) -> s.neutralPalette,
                /* tone= */ (s) ->
                s.isDark
                        ? new ContrastCurve(17.0, 17.0, 21.0, 25.0).get(s.contrastLevel)
                        : new ContrastCurve(92.0, 92.0, 88.0, 85.0).get(s.contrastLevel),
                /* isBackground= */ true,
                /* background= */ null,
                /* secondBackground= */ null,
                /* contrastCurve= */ null,
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor surfaceContainerHighest() {
        return new DynamicColor(
                /* name= */ "surface_container_highest",
                /* palette= */ (s) -> s.neutralPalette,
                /* tone= */ (s) ->
                s.isDark
                        ? new ContrastCurve(22.0, 22.0, 26.0, 30.0).get(s.contrastLevel)
                        : new ContrastCurve(90.0, 90.0, 84.0, 80.0).get(s.contrastLevel),
                /* isBackground= */ true,
                /* background= */ null,
                /* secondBackground= */ null,
                /* contrastCurve= */ null,
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor onSurface() {
        return new DynamicColor(
                /* name= */ "on_surface",
                /* palette= */ (s) -> s.neutralPalette,
                /* tone= */ (s) -> s.isDark ? 90.0 : 10.0,
                /* isBackground= */ false,
                /* background= */ this::highestSurface,
                /* secondBackground= */ null,
                /* contrastCurve= */ new ContrastCurve(4.5, 7.0, 11.0, 21.0),
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor surfaceVariant() {
        return new DynamicColor(
                /* name= */ "surface_variant",
                /* palette= */ (s) -> s.neutralVariantPalette,
                /* tone= */ (s) -> s.isDark ? 30.0 : 90.0,
                /* isBackground= */ true,
                /* background= */ null,
                /* secondBackground= */ null,
                /* contrastCurve= */ null,
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor onSurfaceVariant() {
        return new DynamicColor(
                /* name= */ "on_surface_variant",
                /* palette= */ (s) -> s.neutralVariantPalette,
                /* tone= */ (s) -> s.isDark ? 80.0 : 30.0,
                /* isBackground= */ false,
                /* background= */ this::highestSurface,
                /* secondBackground= */ null,
                /* contrastCurve= */ new ContrastCurve(3.0, 4.5, 7.0, 11.0),
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor inverseSurface() {
        return new DynamicColor(
                /* name= */ "inverse_surface",
                /* palette= */ (s) -> s.neutralPalette,
                /* tone= */ (s) -> s.isDark ? 90.0 : 20.0,
                /* isBackground= */ false,
                /* background= */ null,
                /* secondBackground= */ null,
                /* contrastCurve= */ null,
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor inverseOnSurface() {
        return new DynamicColor(
                /* name= */ "inverse_on_surface",
                /* palette= */ (s) -> s.neutralPalette,
                /* tone= */ (s) -> s.isDark ? 20.0 : 95.0,
                /* isBackground= */ false,
                /* background= */ (s) -> inverseSurface(),
                /* secondBackground= */ null,
                /* contrastCurve= */ new ContrastCurve(4.5, 7.0, 11.0, 21.0),
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor outline() {
        return new DynamicColor(
                /* name= */ "outline",
                /* palette= */ (s) -> s.neutralVariantPalette,
                /* tone= */ (s) -> s.isDark ? 60.0 : 50.0,
                /* isBackground= */ false,
                /* background= */ this::highestSurface,
                /* secondBackground= */ null,
                /* contrastCurve= */ new ContrastCurve(1.5, 3.0, 4.5, 7.0),
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor outlineVariant() {
        return new DynamicColor(
                /* name= */ "outline_variant",
                /* palette= */ (s) -> s.neutralVariantPalette,
                /* tone= */ (s) -> s.isDark ? 30.0 : 80.0,
                /* isBackground= */ false,
                /* background= */ this::highestSurface,
                /* secondBackground= */ null,
                /* contrastCurve= */ new ContrastCurve(1.0, 1.0, 3.0, 4.5),
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor shadow() {
        return new DynamicColor(
                /* name= */ "shadow",
                /* palette= */ (s) -> s.neutralPalette,
                /* tone= */ (s) -> 0.0,
                /* isBackground= */ false,
                /* background= */ null,
                /* secondBackground= */ null,
                /* contrastCurve= */ null,
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor scrim() {
        return new DynamicColor(
                /* name= */ "scrim",
                /* palette= */ (s) -> s.neutralPalette,
                /* tone= */ (s) -> 0.0,
                /* isBackground= */ false,
                /* background= */ null,
                /* secondBackground= */ null,
                /* contrastCurve= */ null,
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor surfaceTint() {
        return new DynamicColor(
                /* name= */ "surface_tint",
                /* palette= */ (s) -> s.primaryPalette,
                /* tone= */ (s) -> s.isDark ? 80.0 : 40.0,
                /* isBackground= */ true,
                /* background= */ null,
                /* secondBackground= */ null,
                /* contrastCurve= */ null,
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor primary() {
        return new DynamicColor(
                /* name= */ "primary",
                /* palette= */ (s) -> s.primaryPalette,
                /* tone= */ (s) -> {
            if (isMonochrome(s)) {
                return s.isDark ? 100.0 : 0.0;
            }
            return s.isDark ? 80.0 : 40.0;
        },
                /* isBackground= */ true,
                /* background= */ this::highestSurface,
                /* secondBackground= */ null,
                /* contrastCurve= */ new ContrastCurve(3.0, 4.5, 7.0, 7.0),
                /* toneDeltaPair= */ (s) ->
                new ToneDeltaPair(primaryContainer(), primary(), 10.0, TonePolarity.NEARER, false));
    }

    @NotNull
    public DynamicColor onPrimary() {
        return new DynamicColor(
                /* name= */ "on_primary",
                /* palette= */ (s) -> s.primaryPalette,
                /* tone= */ (s) -> {
            if (isMonochrome(s)) {
                return s.isDark ? 10.0 : 90.0;
            }
            return s.isDark ? 20.0 : 100.0;
        },
                /* isBackground= */ false,
                /* background= */ (s) -> primary(),
                /* secondBackground= */ null,
                /* contrastCurve= */ new ContrastCurve(4.5, 7.0, 11.0, 21.0),
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor primaryContainer() {
        return new DynamicColor(
                /* name= */ "primary_container",
                /* palette= */ (s) -> s.primaryPalette,
                /* tone= */ (s) -> {
            if (isFidelity(s)) {
                return s.sourceColorHct.getTone();
            }
            if (isMonochrome(s)) {
                return s.isDark ? 85.0 : 25.0;
            }
            return s.isDark ? 30.0 : 90.0;
        },
                /* isBackground= */ true,
                /* background= */ this::highestSurface,
                /* secondBackground= */ null,
                /* contrastCurve= */ new ContrastCurve(1.0, 1.0, 3.0, 4.5),
                /* toneDeltaPair= */ (s) ->
                new ToneDeltaPair(primaryContainer(), primary(), 10.0, TonePolarity.NEARER, false));
    }

    @NotNull
    public DynamicColor onPrimaryContainer() {
        return new DynamicColor(
                /* name= */ "on_primary_container",
                /* palette= */ (s) -> s.primaryPalette,
                /* tone= */ (s) -> {
            if (isFidelity(s)) {
                return DynamicColor.foregroundTone(primaryContainer().tone.apply(s), 4.5);
            }
            if (isMonochrome(s)) {
                return s.isDark ? 0.0 : 100.0;
            }
            return s.isDark ? 90.0 : 30.0;
        },
                /* isBackground= */ false,
                /* background= */ (s) -> primaryContainer(),
                /* secondBackground= */ null,
                /* contrastCurve= */ new ContrastCurve(3.0, 4.5, 7.0, 11.0),
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor inversePrimary() {
        return new DynamicColor(
                /* name= */ "inverse_primary",
                /* palette= */ (s) -> s.primaryPalette,
                /* tone= */ (s) -> s.isDark ? 40.0 : 80.0,
                /* isBackground= */ false,
                /* background= */ (s) -> inverseSurface(),
                /* secondBackground= */ null,
                /* contrastCurve= */ new ContrastCurve(3.0, 4.5, 7.0, 7.0),
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor secondary() {
        return new DynamicColor(
                /* name= */ "secondary",
                /* palette= */ (s) -> s.secondaryPalette,
                /* tone= */ (s) -> s.isDark ? 80.0 : 40.0,
                /* isBackground= */ true,
                /* background= */ this::highestSurface,
                /* secondBackground= */ null,
                /* contrastCurve= */ new ContrastCurve(3.0, 4.5, 7.0, 7.0),
                /* toneDeltaPair= */ (s) ->
                new ToneDeltaPair(secondaryContainer(), secondary(), 10.0, TonePolarity.NEARER, false));
    }

    @NotNull
    public DynamicColor onSecondary() {
        return new DynamicColor(
                /* name= */ "on_secondary",
                /* palette= */ (s) -> s.secondaryPalette,
                /* tone= */ (s) -> {
            if (isMonochrome(s)) {
                return s.isDark ? 10.0 : 100.0;
            } else {
                return s.isDark ? 20.0 : 100.0;
            }
        },
                /* isBackground= */ false,
                /* background= */ (s) -> secondary(),
                /* secondBackground= */ null,
                /* contrastCurve= */ new ContrastCurve(4.5, 7.0, 11.0, 21.0),
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor secondaryContainer() {
        return new DynamicColor(
                /* name= */ "secondary_container",
                /* palette= */ (s) -> s.secondaryPalette,
                /* tone= */ (s) -> {
            final double initialTone = s.isDark ? 30.0 : 90.0;
            if (isMonochrome(s)) {
                return s.isDark ? 30.0 : 85.0;
            }
            if (!isFidelity(s)) {
                return initialTone;
            }
            return findDesiredChromaByTone(
                    s.secondaryPalette.getHue(), s.secondaryPalette.getChroma(), initialTone, !s.isDark);
        },
                /* isBackground= */ true,
                /* background= */ this::highestSurface,
                /* secondBackground= */ null,
                /* contrastCurve= */ new ContrastCurve(1.0, 1.0, 3.0, 4.5),
                /* toneDeltaPair= */ (s) ->
                new ToneDeltaPair(secondaryContainer(), secondary(), 10.0, TonePolarity.NEARER, false));
    }

    @NotNull
    public DynamicColor onSecondaryContainer() {
        return new DynamicColor(
                /* name= */ "on_secondary_container",
                /* palette= */ (s) -> s.secondaryPalette,
                /* tone= */ (s) -> {
            if (isMonochrome(s)) {
                return s.isDark ? 90.0 : 10.0;
            }
            if (!isFidelity(s)) {
                return s.isDark ? 90.0 : 30.0;
            }
            return DynamicColor.foregroundTone(secondaryContainer().tone.apply(s), 4.5);
        },
                /* isBackground= */ false,
                /* background= */ (s) -> secondaryContainer(),
                /* secondBackground= */ null,
                /* contrastCurve= */ new ContrastCurve(3.0, 4.5, 7.0, 11.0),
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor tertiary() {
        return new DynamicColor(
                /* name= */ "tertiary",
                /* palette= */ (s) -> s.tertiaryPalette,
                /* tone= */ (s) -> {
            if (isMonochrome(s)) {
                return s.isDark ? 90.0 : 25.0;
            }
            return s.isDark ? 80.0 : 40.0;
        },
                /* isBackground= */ true,
                /* background= */ this::highestSurface,
                /* secondBackground= */ null,
                /* contrastCurve= */ new ContrastCurve(3.0, 4.5, 7.0, 7.0),
                /* toneDeltaPair= */ (s) ->
                new ToneDeltaPair(tertiaryContainer(), tertiary(), 10.0, TonePolarity.NEARER, false));
    }

    @NotNull
    public DynamicColor onTertiary() {
        return new DynamicColor(
                /* name= */ "on_tertiary",
                /* palette= */ (s) -> s.tertiaryPalette,
                /* tone= */ (s) -> {
            if (isMonochrome(s)) {
                return s.isDark ? 10.0 : 90.0;
            }
            return s.isDark ? 20.0 : 100.0;
        },
                /* isBackground= */ false,
                /* background= */ (s) -> tertiary(),
                /* secondBackground= */ null,
                /* contrastCurve= */ new ContrastCurve(4.5, 7.0, 11.0, 21.0),
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor tertiaryContainer() {
        return new DynamicColor(
                /* name= */ "tertiary_container",
                /* palette= */ (s) -> s.tertiaryPalette,
                /* tone= */ (s) -> {
            if (isMonochrome(s)) {
                return s.isDark ? 60.0 : 49.0;
            }
            if (!isFidelity(s)) {
                return s.isDark ? 30.0 : 90.0;
            }
            final Hct proposedHct = s.tertiaryPalette.getHct(s.sourceColorHct.getTone());
            return DislikeAnalyzer.fixIfDisliked(proposedHct).getTone();
        },
                /* isBackground= */ true,
                /* background= */ this::highestSurface,
                /* secondBackground= */ null,
                /* contrastCurve= */ new ContrastCurve(1.0, 1.0, 3.0, 4.5),
                /* toneDeltaPair= */ (s) ->
                new ToneDeltaPair(tertiaryContainer(), tertiary(), 10.0, TonePolarity.NEARER, false));
    }

    @NotNull
    public DynamicColor onTertiaryContainer() {
        return new DynamicColor(
                /* name= */ "on_tertiary_container",
                /* palette= */ (s) -> s.tertiaryPalette,
                /* tone= */ (s) -> {
            if (isMonochrome(s)) {
                return s.isDark ? 0.0 : 100.0;
            }
            if (!isFidelity(s)) {
                return s.isDark ? 90.0 : 30.0;
            }
            return DynamicColor.foregroundTone(tertiaryContainer().tone.apply(s), 4.5);
        },
                /* isBackground= */ false,
                /* background= */ (s) -> tertiaryContainer(),
                /* secondBackground= */ null,
                /* contrastCurve= */ new ContrastCurve(3.0, 4.5, 7.0, 11.0),
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor error() {
        return new DynamicColor(
                /* name= */ "error",
                /* palette= */ (s) -> s.errorPalette,
                /* tone= */ (s) -> s.isDark ? 80.0 : 40.0,
                /* isBackground= */ true,
                /* background= */ this::highestSurface,
                /* secondBackground= */ null,
                /* contrastCurve= */ new ContrastCurve(3.0, 4.5, 7.0, 7.0),
                /* toneDeltaPair= */ (s) ->
                new ToneDeltaPair(errorContainer(), error(), 10.0, TonePolarity.NEARER, false));
    }

    @NotNull
    public DynamicColor onError() {
        return new DynamicColor(
                /* name= */ "on_error",
                /* palette= */ (s) -> s.errorPalette,
                /* tone= */ (s) -> s.isDark ? 20.0 : 100.0,
                /* isBackground= */ false,
                /* background= */ (s) -> error(),
                /* secondBackground= */ null,
                /* contrastCurve= */ new ContrastCurve(4.5, 7.0, 11.0, 21.0),
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor errorContainer() {
        return new DynamicColor(
                /* name= */ "error_container",
                /* palette= */ (s) -> s.errorPalette,
                /* tone= */ (s) -> s.isDark ? 30.0 : 90.0,
                /* isBackground= */ true,
                /* background= */ this::highestSurface,
                /* secondBackground= */ null,
                /* contrastCurve= */ new ContrastCurve(1.0, 1.0, 3.0, 4.5),
                /* toneDeltaPair= */ (s) ->
                new ToneDeltaPair(errorContainer(), error(), 10.0, TonePolarity.NEARER, false));
    }

    @NotNull
    public DynamicColor onErrorContainer() {
        return new DynamicColor(
                /* name= */ "on_error_container",
                /* palette= */ (s) -> s.errorPalette,
                /* tone= */ (s) -> {
            if (isMonochrome(s)) {
                return s.isDark ? 90.0 : 10.0;
            }
            return s.isDark ? 90.0 : 30.0;
        },
                /* isBackground= */ false,
                /* background= */ (s) -> errorContainer(),
                /* secondBackground= */ null,
                /* contrastCurve= */ new ContrastCurve(3.0, 4.5, 7.0, 11.0),
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor primaryFixed() {
        return new DynamicColor(
                /* name= */ "primary_fixed",
                /* palette= */ (s) -> s.primaryPalette,
                /* tone= */ (s) -> isMonochrome(s) ? 40.0 : 90.0,
                /* isBackground= */ true,
                /* background= */ this::highestSurface,
                /* secondBackground= */ null,
                /* contrastCurve= */ new ContrastCurve(1.0, 1.0, 3.0, 4.5),
                /* toneDeltaPair= */ (s) ->
                new ToneDeltaPair(primaryFixed(), primaryFixedDim(), 10.0, TonePolarity.LIGHTER, true));
    }

    @NotNull
    public DynamicColor primaryFixedDim() {
        return new DynamicColor(
                /* name= */ "primary_fixed_dim",
                /* palette= */ (s) -> s.primaryPalette,
                /* tone= */ (s) -> isMonochrome(s) ? 30.0 : 80.0,
                /* isBackground= */ true,
                /* background= */ this::highestSurface,
                /* secondBackground= */ null,
                /* contrastCurve= */ new ContrastCurve(1.0, 1.0, 3.0, 4.5),
                /* toneDeltaPair= */ (s) ->
                new ToneDeltaPair(primaryFixed(), primaryFixedDim(), 10.0, TonePolarity.LIGHTER, true));
    }

    @NotNull
    public DynamicColor onPrimaryFixed() {
        return new DynamicColor(
                /* name= */ "on_primary_fixed",
                /* palette= */ (s) -> s.primaryPalette,
                /* tone= */ (s) -> isMonochrome(s) ? 100.0 : 10.0,
                /* isBackground= */ false,
                /* background= */ (s) -> primaryFixedDim(),
                /* secondBackground= */ (s) -> primaryFixed(),
                /* contrastCurve= */ new ContrastCurve(4.5, 7.0, 11.0, 21.0),
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor onPrimaryFixedVariant() {
        return new DynamicColor(
                /* name= */ "on_primary_fixed_variant",
                /* palette= */ (s) -> s.primaryPalette,
                /* tone= */ (s) -> isMonochrome(s) ? 90.0 : 30.0,
                /* isBackground= */ false,
                /* background= */ (s) -> primaryFixedDim(),
                /* secondBackground= */ (s) -> primaryFixed(),
                /* contrastCurve= */ new ContrastCurve(3.0, 4.5, 7.0, 11.0),
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor secondaryFixed() {
        return new DynamicColor(
                /* name= */ "secondary_fixed",
                /* palette= */ (s) -> s.secondaryPalette,
                /* tone= */ (s) -> isMonochrome(s) ? 80.0 : 90.0,
                /* isBackground= */ true,
                /* background= */ this::highestSurface,
                /* secondBackground= */ null,
                /* contrastCurve= */ new ContrastCurve(1.0, 1.0, 3.0, 4.5),
                /* toneDeltaPair= */ (s) ->
                new ToneDeltaPair(
                        secondaryFixed(), secondaryFixedDim(), 10.0, TonePolarity.LIGHTER, true));
    }

    @NotNull
    public DynamicColor secondaryFixedDim() {
        return new DynamicColor(
                /* name= */ "secondary_fixed_dim",
                /* palette= */ (s) -> s.secondaryPalette,
                /* tone= */ (s) -> isMonochrome(s) ? 70.0 : 80.0,
                /* isBackground= */ true,
                /* background= */ this::highestSurface,
                /* secondBackground= */ null,
                /* contrastCurve= */ new ContrastCurve(1.0, 1.0, 3.0, 4.5),
                /* toneDeltaPair= */ (s) ->
                new ToneDeltaPair(
                        secondaryFixed(), secondaryFixedDim(), 10.0, TonePolarity.LIGHTER, true));
    }

    @NotNull
    public DynamicColor onSecondaryFixed() {
        return new DynamicColor(
                /* name= */ "on_secondary_fixed",
                /* palette= */ (s) -> s.secondaryPalette,
                /* tone= */ (s) -> 10.0,
                /* isBackground= */ false,
                /* background= */ (s) -> secondaryFixedDim(),
                /* secondBackground= */ (s) -> secondaryFixed(),
                /* contrastCurve= */ new ContrastCurve(4.5, 7.0, 11.0, 21.0),
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor onSecondaryFixedVariant() {
        return new DynamicColor(
                /* name= */ "on_secondary_fixed_variant",
                /* palette= */ (s) -> s.secondaryPalette,
                /* tone= */ (s) -> isMonochrome(s) ? 25.0 : 30.0,
                /* isBackground= */ false,
                /* background= */ (s) -> secondaryFixedDim(),
                /* secondBackground= */ (s) -> secondaryFixed(),
                /* contrastCurve= */ new ContrastCurve(3.0, 4.5, 7.0, 11.0),
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor tertiaryFixed() {
        return new DynamicColor(
                /* name= */ "tertiary_fixed",
                /* palette= */ (s) -> s.tertiaryPalette,
                /* tone= */ (s) -> isMonochrome(s) ? 40.0 : 90.0,
                /* isBackground= */ true,
                /* background= */ this::highestSurface,
                /* secondBackground= */ null,
                /* contrastCurve= */ new ContrastCurve(1.0, 1.0, 3.0, 4.5),
                /* toneDeltaPair= */ (s) ->
                new ToneDeltaPair(
                        tertiaryFixed(), tertiaryFixedDim(), 10.0, TonePolarity.LIGHTER, true));
    }

    @NotNull
    public DynamicColor tertiaryFixedDim() {
        return new DynamicColor(
                /* name= */ "tertiary_fixed_dim",
                /* palette= */ (s) -> s.tertiaryPalette,
                /* tone= */ (s) -> isMonochrome(s) ? 30.0 : 80.0,
                /* isBackground= */ true,
                /* background= */ this::highestSurface,
                /* secondBackground= */ null,
                /* contrastCurve= */ new ContrastCurve(1.0, 1.0, 3.0, 4.5),
                /* toneDeltaPair= */ (s) ->
                new ToneDeltaPair(
                        tertiaryFixed(), tertiaryFixedDim(), 10.0, TonePolarity.LIGHTER, true));
    }

    @NotNull
    public DynamicColor onTertiaryFixed() {
        return new DynamicColor(
                /* name= */ "on_tertiary_fixed",
                /* palette= */ (s) -> s.tertiaryPalette,
                /* tone= */ (s) -> isMonochrome(s) ? 100.0 : 10.0,
                /* isBackground= */ false,
                /* background= */ (s) -> tertiaryFixedDim(),
                /* secondBackground= */ (s) -> tertiaryFixed(),
                /* contrastCurve= */ new ContrastCurve(4.5, 7.0, 11.0, 21.0),
                /* toneDeltaPair= */ null);
    }

    @NotNull
    public DynamicColor onTertiaryFixedVariant() {
        return new DynamicColor(
                /* name= */ "on_tertiary_fixed_variant",
                /* palette= */ (s) -> s.tertiaryPalette,
                /* tone= */ (s) -> isMonochrome(s) ? 90.0 : 30.0,
                /* isBackground= */ false,
                /* background= */ (s) -> tertiaryFixedDim(),
                /* secondBackground= */ (s) -> tertiaryFixed(),
                /* contrastCurve= */ new ContrastCurve(3.0, 4.5, 7.0, 11.0),
                /* toneDeltaPair= */ null);
    }

    /**
     * These colors were present in Android framework before Android U, and used by MDC controls. They
     * should be avoided, if possible. It's unclear if they're used on multiple backgrounds, and if
     * they are, they can't be adjusted for contrast.* For now, they will be set with no background,
     * and those won't adjust for contrast, avoiding issues.
     *
     * <p>* For example, if the same color is on a white background _and_ black background, there's no
     * way to increase contrast with either without losing contrast with the other.
     */
    // colorControlActivated documented as colorAccent in M3 & GM3.
    // colorAccent documented as colorSecondary in M3 and colorPrimary in GM3.
    // Android used Material's Container as Primary/Secondary/Tertiary at launch.
    // Therefore, this is a duplicated version of Primary Container.
    @NotNull
    public DynamicColor controlActivated() {
        return DynamicColor.fromPalette(
                "control_activated", (s) -> s.primaryPalette, (s) -> s.isDark ? 30.0 : 90.0);
    }

    // colorControlNormal documented as textColorSecondary in M3 & GM3.
    // In Material, textColorSecondary points to onSurfaceVariant in the non-disabled state,
    // which is Neutral Variant T30/80 in light/dark.
    @NotNull
    public DynamicColor controlNormal() {
        return DynamicColor.fromPalette(
                "control_normal", (s) -> s.neutralVariantPalette, (s) -> s.isDark ? 80.0 : 30.0);
    }

    // colorControlHighlight documented, in both M3 & GM3:
    // Light mode: #1f000000 dark mode: #33ffffff.
    // These are black and white with some alpha.
    // 1F hex = 31 decimal; 31 / 255 = 12% alpha.
    // 33 hex = 51 decimal; 51 / 255 = 20% alpha.
    // DynamicColors do not support alpha currently, and _may_ not need it for this use case,
    // depending on how MDC resolved alpha for the other cases.
    // Returning black in dark mode, white in light mode.
    @NotNull
    public DynamicColor controlHighlight() {
        return new DynamicColor(
                /* name= */ "control_highlight",
                /* palette= */ (s) -> s.neutralPalette,
                /* tone= */ (s) -> s.isDark ? 100.0 : 0.0,
                /* isBackground= */ false,
                /* background= */ null,
                /* secondBackground= */ null,
                /* contrastCurve= */ null,
                /* toneDeltaPair= */ null,
                /* opacity= */ s -> s.isDark ? 0.20 : 0.12);
    }

    // textColorPrimaryInverse documented, in both M3 & GM3, documented as N10/N90.
    @NotNull
    public DynamicColor textPrimaryInverse() {
        return DynamicColor.fromPalette(
                "text_primary_inverse", (s) -> s.neutralPalette, (s) -> s.isDark ? 10.0 : 90.0);
    }

    // textColorSecondaryInverse and textColorTertiaryInverse both documented, in both M3 & GM3, as
    // NV30/NV80
    @NotNull
    public DynamicColor textSecondaryAndTertiaryInverse() {
        return DynamicColor.fromPalette(
                "text_secondary_and_tertiary_inverse",
                (s) -> s.neutralVariantPalette,
                (s) -> s.isDark ? 30.0 : 80.0);
    }

    // textColorPrimaryInverseDisableOnly documented, in both M3 & GM3, as N10/N90
    @NotNull
    public DynamicColor textPrimaryInverseDisableOnly() {
        return DynamicColor.fromPalette(
                "text_primary_inverse_disable_only",
                (s) -> s.neutralPalette,
                (s) -> s.isDark ? 10.0 : 90.0);
    }

    // textColorSecondaryInverse and textColorTertiaryInverse in disabled state both documented,
    // in both M3 & GM3, as N10/N90
    @NotNull
    public DynamicColor textSecondaryAndTertiaryInverseDisabled() {
        return DynamicColor.fromPalette(
                "text_secondary_and_tertiary_inverse_disabled",
                (s) -> s.neutralPalette,
                (s) -> s.isDark ? 10.0 : 90.0);
    }

    // textColorHintInverse documented, in both M3 & GM3, as N10/N90
    @NotNull
    public DynamicColor textHintInverse() {
        return DynamicColor.fromPalette(
                "text_hint_inverse", (s) -> s.neutralPalette, (s) -> s.isDark ? 10.0 : 90.0);
    }

    /**
     * All dynamic colors in Material Design system.
     */
    public List<Supplier<DynamicColor>> allDynamicColors() {
        return Arrays.<Supplier<DynamicColor>>asList(
                this::background,
                this::onBackground,
                this::surface,
                this::surfaceDim,
                this::surfaceBright,
                this::surfaceContainerLowest,
                this::surfaceContainerLow,
                this::surfaceContainer,
                this::surfaceContainerHigh,
                this::surfaceContainerHighest,
                this::onSurface,
                this::surfaceVariant,
                this::onSurfaceVariant,
                this::inverseSurface,
                this::inverseOnSurface,
                this::outline,
                this::outlineVariant,
                this::shadow,
                this::scrim,
                this::surfaceTint,
                this::primary,
                this::onPrimary,
                this::primaryContainer,
                this::onPrimaryContainer,
                this::inversePrimary,
                this::secondary,
                this::onSecondary,
                this::secondaryContainer,
                this::onSecondaryContainer,
                this::tertiary,
                this::onTertiary,
                this::tertiaryContainer,
                this::onTertiaryContainer,
                this::error,
                this::onError,
                this::errorContainer,
                this::onErrorContainer,
                this::primaryFixed,
                this::primaryFixedDim,
                this::onPrimaryFixed,
                this::onPrimaryFixedVariant,
                this::secondaryFixed,
                this::secondaryFixedDim,
                this::onSecondaryFixed,
                this::onSecondaryFixedVariant,
                this::tertiaryFixed,
                this::tertiaryFixedDim,
                this::onTertiaryFixed,
                this::onTertiaryFixedVariant,
                this::controlActivated,
                this::controlNormal,
                this::controlHighlight,
                this::textPrimaryInverse,
                this::textSecondaryAndTertiaryInverse,
                this::textPrimaryInverseDisableOnly,
                this::textSecondaryAndTertiaryInverseDisabled,
                this::textHintInverse);
    }

    private boolean isFidelity(DynamicScheme scheme) {
        if (this.isExtendedFidelity
            && scheme.variant != ColorStyle.MONOCHROME
            && scheme.variant != ColorStyle.NEUTRAL) {
            return true;
        }
        return scheme.variant == ColorStyle.FIDELITY || scheme.variant == ColorStyle.CONTENT;
    }

    private static boolean isMonochrome(DynamicScheme scheme) {
        return scheme.variant == ColorStyle.MONOCHROME;
    }

    static double findDesiredChromaByTone(
            double hue, double chroma, double tone, boolean byDecreasingTone) {
        double answer = tone;

        Hct closestToChroma = Hct.from(hue, chroma, tone);
        if (closestToChroma.getChroma() < chroma) {
            double chromaPeak = closestToChroma.getChroma();
            while (closestToChroma.getChroma() < chroma) {
                answer += byDecreasingTone ? -1.0 : 1.0;
                Hct potentialSolution = Hct.from(hue, chroma, answer);
                if (chromaPeak > potentialSolution.getChroma()) {
                    break;
                }
                if (Math.abs(potentialSolution.getChroma() - chroma) < 0.4) {
                    break;
                }

                double potentialDelta = Math.abs(potentialSolution.getChroma() - chroma);
                double currentDelta = Math.abs(closestToChroma.getChroma() - chroma);
                if (potentialDelta < currentDelta) {
                    closestToChroma = potentialSolution;
                }
                chromaPeak = Math.max(chromaPeak, potentialSolution.getChroma());
            }
        }

        return answer;
    }
}
