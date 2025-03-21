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
import org.glavo.monetfx.ColorScheme;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

enum ThemeExporter {
    CSS("MonetFX CSS", new FileChooser.ExtensionFilter("CSS File", "*.css")) {
        @Override
        boolean export(Path file, ColorScheme scheme) {
            try (BufferedWriter writer = Files.newBufferedWriter(file, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                writer.write(scheme.toStyleSheet());
                return true;
            } catch (Throwable e) {
                e.printStackTrace(System.err);
                return false;
            }
        }
    };

    final String name;
    final FileChooser.ExtensionFilter extensionFilter;

    ThemeExporter(String name, FileChooser.ExtensionFilter extensionFilter) {
        this.name = name;
        this.extensionFilter = extensionFilter;
    }

    abstract boolean export(Path file, ColorScheme scheme);
}
