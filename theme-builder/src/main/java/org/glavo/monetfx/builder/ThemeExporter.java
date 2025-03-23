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
package org.glavo.monetfx.builder;

import javafx.stage.FileChooser;
import org.glavo.monetfx.Brightness;
import org.glavo.monetfx.ColorScheme;
import org.glavo.monetfx.Contrast;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

enum ThemeExporter {
    FX_CSS("MonetFX CSS", new FileChooser.ExtensionFilter("CSS File", "*.css")) {
        @Override
        boolean export(Path file, ColorScheme scheme) {
            try (BufferedWriter writer = Files.newBufferedWriter(file, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                writer.write(scheme.toStyleSheet());
                return true;
            } catch (Throwable e) {
                try {
                    Files.deleteIfExists(file);
                } catch (Throwable e2) {
                    e.addSuppressed(e2);
                }
                e.printStackTrace(System.err);
            }
            return false;
        }
    },
    WEB_CSS("Web CSS", new FileChooser.ExtensionFilter("Zip Archive", "*.zip")) {
        @Override
        boolean export(Path file, ColorScheme scheme) {
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(file))) {
                for (Map.Entry<String, Brightness> brightness : BRIGHTNESSES.entrySet()) {
                    for (Map.Entry<String, Contrast> contrast : CONTRASTS.entrySet()) {
                        ZipEntry entry = new ZipEntry("css/" + brightness.getKey() + (contrast.getKey().isEmpty() ? "" : "-" + contrast.getKey()) + ".css");

                        ColorScheme cs = ColorScheme.newBuilder()
                                .setPrimaryColorSeed(scheme.getPrimaryColorSeed())
                                .setSecondaryColorSeed(scheme.getSecondaryColorSeed())
                                .setTertiaryColorSeed(scheme.getTertiaryColorSeed())
                                .setNeutralColorSeed(scheme.getNeutralColorSeed())
                                .setNeutralVariantColorSeed(scheme.getNeutralVariantColorSeed())
                                .setErrorColorSeed(scheme.getErrorColorSeed())
                                .setBrightness(brightness.getValue())
                                .setContrast(contrast.getValue())
                                .setColorStyle(scheme.getColorStyle())
                                .build();

                        StringBuilder className = new StringBuilder();
                        className.append(brightness.getKey());
                        if (contrast.getValue() == Contrast.HIGH) {
                            className.append("-high-contrast");
                        } else if (contrast.getValue() == Contrast.MEDIUM) {
                            className.append("-medium-contrast");
                        }

                        String css = cs.toStyleSheet(className.toString(), "--md-sys-color", null);

                        zipOutputStream.putNextEntry(entry);
                        zipOutputStream.write(css.getBytes(StandardCharsets.UTF_8));
                        zipOutputStream.closeEntry();
                    }
                }
                return true;
            } catch (Throwable e) {
                try {
                    Files.deleteIfExists(file);
                } catch (Throwable e2) {
                    e.addSuppressed(e2);
                }
                e.printStackTrace(System.err);
            }

            return false;
        }
    };

    private static final Map<String, Brightness> BRIGHTNESSES = new LinkedHashMap<>();

    static {
        BRIGHTNESSES.put("dark", Brightness.DARK);
        BRIGHTNESSES.put("light", Brightness.LIGHT);
    }

    private static final Map<String, Contrast> CONTRASTS = new LinkedHashMap<>();

    static {
        CONTRASTS.put("", Contrast.STANDARD);
        CONTRASTS.put("mc", Contrast.MEDIUM);
        CONTRASTS.put("hc", Contrast.HIGH);
    }

    final String name;
    final FileChooser.ExtensionFilter extensionFilter;

    ThemeExporter(String name, FileChooser.ExtensionFilter extensionFilter) {
        this.name = name;
        this.extensionFilter = extensionFilter;
    }

    abstract boolean export(Path file, ColorScheme scheme);
}
