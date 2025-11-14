/*
 * Copyright 2025 Google LLC
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
import org.glavo.monetfx.ColorPlatform;
import org.glavo.monetfx.internal.hct.Hct;
import org.glavo.monetfx.internal.palettes.TonalPalette;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/** An interface defining all the necessary methods that could be different between specs. */
public interface ColorSpec {

    ////////////////////////////////////////////////////////////////
  // Main Palettes                                              //
  ////////////////////////////////////////////////////////////////

  @NotNull DynamicColor primaryPaletteKeyColor();

  @NotNull DynamicColor secondaryPaletteKeyColor();

  @NotNull DynamicColor tertiaryPaletteKeyColor();

  @NotNull DynamicColor neutralPaletteKeyColor();

  @NotNull DynamicColor neutralVariantPaletteKeyColor();

  @NotNull DynamicColor errorPaletteKeyColor();

  ////////////////////////////////////////////////////////////////
  // Surfaces [S]                                               //
  ////////////////////////////////////////////////////////////////

  @NotNull DynamicColor background();

  @NotNull DynamicColor onBackground();

  @NotNull DynamicColor surface();

  @NotNull DynamicColor surfaceDim();

  @NotNull DynamicColor surfaceBright();

  @NotNull DynamicColor surfaceContainerLowest();

  @NotNull DynamicColor surfaceContainerLow();

  @NotNull DynamicColor surfaceContainer();

  @NotNull DynamicColor surfaceContainerHigh();

  @NotNull DynamicColor surfaceContainerHighest();

  @NotNull DynamicColor onSurface();

  @NotNull DynamicColor surfaceVariant();

  @NotNull DynamicColor onSurfaceVariant();

  @NotNull DynamicColor inverseSurface();

  @NotNull DynamicColor inverseOnSurface();

  @NotNull DynamicColor outline();

  @NotNull DynamicColor outlineVariant();

  @NotNull DynamicColor shadow();

  @NotNull DynamicColor scrim();

  @NotNull DynamicColor surfaceTint();

  ////////////////////////////////////////////////////////////////
  // Primaries [P]                                              //
  ////////////////////////////////////////////////////////////////

  @NotNull DynamicColor primary();

  @Nullable
  DynamicColor primaryDim();

  @NotNull DynamicColor onPrimary();

  @NotNull DynamicColor primaryContainer();

  @NotNull DynamicColor onPrimaryContainer();

  @NotNull DynamicColor inversePrimary();

  ////////////////////////////////////////////////////////////////
  // Secondaries [Q]                                            //
  ////////////////////////////////////////////////////////////////

  @NotNull DynamicColor secondary();

  @Nullable
  DynamicColor secondaryDim();

  @NotNull DynamicColor onSecondary();

  @NotNull DynamicColor secondaryContainer();

  @NotNull DynamicColor onSecondaryContainer();

  ////////////////////////////////////////////////////////////////
  // Tertiaries [T]                                             //
  ////////////////////////////////////////////////////////////////

  @NotNull DynamicColor tertiary();

  @Nullable
  DynamicColor tertiaryDim();

  @NotNull DynamicColor onTertiary();

  @NotNull DynamicColor tertiaryContainer();

  @NotNull DynamicColor onTertiaryContainer();

  ////////////////////////////////////////////////////////////////
  // Errors [E]                                                 //
  ////////////////////////////////////////////////////////////////

  @NotNull DynamicColor error();

  @Nullable
  DynamicColor errorDim();

  @NotNull DynamicColor onError();

  @NotNull DynamicColor errorContainer();

  @NotNull DynamicColor onErrorContainer();

  ////////////////////////////////////////////////////////////////
  // Primary Fixed Colors [PF]                                  //
  ////////////////////////////////////////////////////////////////

  @NotNull DynamicColor primaryFixed();

  @NotNull DynamicColor primaryFixedDim();

  @NotNull DynamicColor onPrimaryFixed();

  @NotNull DynamicColor onPrimaryFixedVariant();

  ////////////////////////////////////////////////////////////////
  // Secondary Fixed Colors [QF]                                //
  ////////////////////////////////////////////////////////////////

  @NotNull DynamicColor secondaryFixed();

  @NotNull DynamicColor secondaryFixedDim();

  @NotNull DynamicColor onSecondaryFixed();

  @NotNull DynamicColor onSecondaryFixedVariant();

  ////////////////////////////////////////////////////////////////
  // Tertiary Fixed Colors [TF]                                 //
  ////////////////////////////////////////////////////////////////

  @NotNull DynamicColor tertiaryFixed();

  @NotNull DynamicColor tertiaryFixedDim();

  @NotNull DynamicColor onTertiaryFixed();

  @NotNull DynamicColor onTertiaryFixedVariant();

  //////////////////////////////////////////////////////////////////
  // Android-only Colors                                          //
  //////////////////////////////////////////////////////////////////

  @NotNull DynamicColor controlActivated();

  @NotNull DynamicColor controlNormal();

  @NotNull DynamicColor controlHighlight();

  @NotNull DynamicColor textPrimaryInverse();

  @NotNull DynamicColor textSecondaryAndTertiaryInverse();

  @NotNull DynamicColor textPrimaryInverseDisableOnly();

  @NotNull DynamicColor textSecondaryAndTertiaryInverseDisabled();

  @NotNull DynamicColor textHintInverse();

  ////////////////////////////////////////////////////////////////
  // Other                                                      //
  ////////////////////////////////////////////////////////////////

  @NotNull DynamicColor highestSurface(@NotNull DynamicScheme s);

  /////////////////////////////////////////////////////////////////
  // Color value calculations                                    //
  /////////////////////////////////////////////////////////////////

  Hct getHct(DynamicScheme scheme, DynamicColor color);

  double getTone(DynamicScheme scheme, DynamicColor color);

  //////////////////////////////////////////////////////////////////
  // Scheme Palettes                                              //
  //////////////////////////////////////////////////////////////////

  @NotNull TonalPalette getPrimaryPalette(
          ColorStyle variant, Hct sourceColorHct, boolean isDark, ColorPlatform platform, double contrastLevel);

  @NotNull TonalPalette getSecondaryPalette(
          ColorStyle variant, Hct sourceColorHct, boolean isDark, ColorPlatform platform, double contrastLevel);

  @NotNull TonalPalette getTertiaryPalette(
          ColorStyle variant, Hct sourceColorHct, boolean isDark, ColorPlatform platform, double contrastLevel);

  @NotNull TonalPalette getNeutralPalette(
          ColorStyle variant, Hct sourceColorHct, boolean isDark, ColorPlatform platform, double contrastLevel);

  @NotNull TonalPalette getNeutralVariantPalette(
          ColorStyle variant, Hct sourceColorHct, boolean isDark, ColorPlatform platform, double contrastLevel);

  @NotNull Optional<TonalPalette> getErrorPalette(
          ColorStyle variant, Hct sourceColorHct, boolean isDark, ColorPlatform platform, double contrastLevel);
}
