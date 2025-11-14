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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public final class ContrastTest {
    @Test
    public void testOf() {
        assertSame(Contrast.LOW, Contrast.of(-1.0));
        assertSame(Contrast.STANDARD, Contrast.of(0.0));
        assertSame(Contrast.MEDIUM, Contrast.of(0.5));
        assertSame(Contrast.HIGH, Contrast.of(1.0));

        for (double d = -1.0; d <= 1.0; d += 0.1) {
            assertEquals(d, Contrast.of(d).getValue());
        }

        assertThrows(IllegalArgumentException.class, () -> Contrast.of(Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> Contrast.of(-2));
        assertThrows(IllegalArgumentException.class, () -> Contrast.of(-1.1));
        assertThrows(IllegalArgumentException.class, () -> Contrast.of(1.1));
        assertThrows(IllegalArgumentException.class, () -> Contrast.of(2));
    }
}
