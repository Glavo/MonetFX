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

import org.jetbrains.annotations.NotNull;

public final class Contrast {

    public static final Contrast LOW = new Contrast(-1.0);
    public static final Contrast STANDARD = new Contrast(0.0);
    public static final Contrast MEDIUM = new Contrast(0.5);
    public static final Contrast HIGH = new Contrast(1.0);

    public static final Contrast DEFAULT = STANDARD;

    public static @NotNull Contrast of(double value) {
        if (value < -1.0 || value > 1.0 || Double.isNaN(value)) {
            throw new IllegalArgumentException("Contrast value must be between -1.0 and 1.0.");
        }

        if (value == LOW.value) {
            return LOW;
        } else if (value == STANDARD.value) {
            return STANDARD;
        } else if (value == MEDIUM.value) {
            return MEDIUM;
        } else if (value == HIGH.value) {
            return HIGH;
        } else {
            return new Contrast(value);
        }
    }

    private final double value;

    private Contrast(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Contrast[" + value + ']';
    }
}
