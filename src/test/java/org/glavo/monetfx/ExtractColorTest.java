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
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import org.glavo.monetfx.internal.utils.ColorUtils;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@EnabledIf("org.glavo.monetfx.JavaFXLauncher#isStarted")
public final class ExtractColorTest {

    private static <K, V> Pair<K, V> pair(K key, V value) {
        return new Pair<>(key, value);
    }

    private static Image createMonochromeImage(Color color, int width, int height) {
        WritableImage writableImage = new WritableImage(width, height);

        int[] buffer = new int[width * height];
        Arrays.fill(buffer, ColorUtils.argbFromFx(color));

        writableImage.getPixelWriter().setPixels(0, 0, width, height,
                PixelFormat.getIntArgbInstance(),
                buffer, 0, width);

        return writableImage;
    }

    private static Stream<Arguments> monochromes() {
        List<Pair<Color, Color>> colors = Arrays.asList(
                pair(Color.BLACK, ColorScheme.FALLBACK_COLOR),
                pair(Color.WHITE, ColorScheme.FALLBACK_COLOR),
                pair(Color.GREEN, Color.GREEN),
                pair(Color.RED, Color.RED),
                pair(Color.BLUE, Color.BLUE)
        );

        List<Pair<Integer, Integer>> sizes = Arrays.asList(
                pair(1, 1),
                pair(50, 50),
                pair(50, 150),
                pair(150, 50),
                pair(150, 150)
        );

        return colors.stream()
                .flatMap(colorPair -> sizes.stream()
                        .map(size ->
                                Arguments.of(colorPair.getKey(), colorPair.getValue(), size.getKey(), size.getValue())));
    }

    @ParameterizedTest
    @MethodSource("monochromes")
    public void testExtractMonochrome(Color sourceColor, Color targetColor, int width, int height) {
        assertEquals(targetColor, ColorScheme.extractColor(createMonochromeImage(sourceColor, width, height)));
    }
}
