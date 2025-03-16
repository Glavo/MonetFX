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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import javafx.scene.paint.Color;
import org.hildan.fxgson.adapters.extras.ColorTypeAdapter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ColorSchemeTest {
    private static String toWeb(Color color) {
        return String.format("#%02X%02X%02X",
                Math.round(color.getRed() * 255),
                Math.round(color.getGreen() * 255),
                Math.round(color.getBlue() * 255));
    }

    private static String toPostfix(DynamicSchemeVariant variant) {
        return variant == DynamicSchemeVariant.TONAL_SPOT ? "" : "-" + variant.name().toLowerCase(Locale.ROOT);
    }

    private static Stream<Arguments> testFromSeedArguments() {
        return Stream.of("red", "green", "blue", "yellow", "custom")
                .flatMap(name -> Arrays.stream(DynamicSchemeVariant.values())
                        .flatMap(variant -> {
                            String fileName = "theme-" + name + toPostfix(variant) + ".json";

                            URL url = ColorSchemeTest.class.getResource(fileName);
                            if (url != null) {
                                try {
                                    return Stream.of(MaterialTheme.load(url.openStream(), variant));
                                } catch (IOException e) {
                                    throw new UncheckedIOException(e);
                                }
                            } else {
                                return Stream.empty();
                            }
                        }))
                .flatMap(theme -> theme.schemes.entrySet().stream().map(pair -> {
                    Brightness brightness = pair.getKey().getKey();
                    Contrast contrast = pair.getKey().getValue();
                    Map<String, Color> coreColors = theme.coreColors;
                    Map<ColorRole, Color> colors = pair.getValue();

                    return Arguments.of(
                            coreColors.get("primary"), coreColors.get("secondary"), coreColors.get("tertiary"),
                            coreColors.get("neutral"), coreColors.get("neutralVariant"), coreColors.get("error"),
                            brightness, theme.variant, contrast, colors);
                }));
    }

    @ParameterizedTest
    @MethodSource("testFromSeedArguments")
    public void testFromSeed(Color primaryColor, Color secondaryColor, Color tertiaryColor,
                             Color neutralColor, Color neutralVariantColor, Color errorColor,
                             Brightness brightness, DynamicSchemeVariant variant, Contrast contrast, Map<ColorRole, Color> colors) {
        ColorScheme scheme = ColorScheme.newBuilder(primaryColor)
                .setSecondaryColor(secondaryColor)
                .setTertiaryColor(tertiaryColor)
                .setNeutralColor(neutralColor)
                .setNeutralVariantColor(neutralVariantColor)
                .setErrorColor(errorColor)
                .setBrightness(brightness)
                .setDynamicSchemeVariant(variant)
                .setContrast(contrast)
                .build();

        assertAll(colors.keySet().stream().map(role ->
                () -> {
                    Color expected = colors.get(role);
                    Color actual = scheme.getColor(role);
                    assertEquals(expected, actual, () -> String.format("Role: %s, Excepted: %s, Actual: %s", role, toWeb(expected), toWeb(actual)));
                }));
    }

    private static final class MaterialTheme {
        private static final Gson GSON = new GsonBuilder().registerTypeAdapter(Color.class, new ColorTypeAdapter()).create();

        private static Map.Entry<Brightness, Contrast> parseSchemeName(String name) {
            switch (name) {
                case "light":
                    return new AbstractMap.SimpleEntry<>(Brightness.LIGHT, Contrast.STANDARD);
                case "light-medium-contrast":
                    return new AbstractMap.SimpleEntry<>(Brightness.LIGHT, Contrast.MEDIUM);
                case "light-high-contrast":
                    return new AbstractMap.SimpleEntry<>(Brightness.LIGHT, Contrast.HIGH);
                case "dark":
                    return new AbstractMap.SimpleEntry<>(Brightness.DARK, Contrast.STANDARD);
                case "dark-medium-contrast":
                    return new AbstractMap.SimpleEntry<>(Brightness.DARK, Contrast.MEDIUM);
                case "dark-high-contrast":
                    return new AbstractMap.SimpleEntry<>(Brightness.DARK, Contrast.HIGH);
                default:
                    throw new AssertionError("Failed to parse scheme name: " + name);
            }
        }

        public static MaterialTheme load(InputStream inputStream, DynamicSchemeVariant variant) {
            JsonObject raw;
            try (Reader reader = new InputStreamReader(inputStream)) {
                raw = GSON.fromJson(reader, JsonObject.class);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }

            LinkedHashMap<String, Color> colorColors = new LinkedHashMap<>();

            raw.getAsJsonObject("coreColors").asMap().forEach((name, value) -> colorColors.put(name, Color.web(value.getAsString())));

            LinkedHashMap<Map.Entry<Brightness, Contrast>, Map<ColorRole, Color>> schemes = new LinkedHashMap<>();
            raw.get("schemes").getAsJsonObject().asMap().forEach((schemeName, scheme) -> {
                EnumMap<ColorRole, Color> colors = new EnumMap<>(ColorRole.class);
                scheme.getAsJsonObject().asMap().forEach((role, color) ->
                        colors.put(ColorRole.of(role), Color.web(color.getAsString())));
                schemes.put(parseSchemeName(schemeName), colors);
            });

            return new MaterialTheme(colorColors, variant, schemes);
        }

        public final Map<String, Color> coreColors;
        public final DynamicSchemeVariant variant;
        public final Map<Map.Entry<Brightness, Contrast>, Map<ColorRole, Color>> schemes;

        private MaterialTheme(Map<String, Color> coreColors, DynamicSchemeVariant variant, Map<Map.Entry<Brightness, Contrast>, Map<ColorRole, Color>> schemes) {
            this.coreColors = coreColors;
            this.variant = variant;
            this.schemes = schemes;
        }

    }
}
