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
package org.glavo.monetfx.internal.dislike;

import org.glavo.monetfx.internal.hct.Hct;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class DislikeAnalyzerTest {

    @ParameterizedTest
    @ValueSource(ints = {
            0xfff6ede4,
            0xfff3e7db,
            0xfff7ead0,
            0xffeadaba,
            0xffd7bd96,
            0xffa07e56,
            0xff825c43,
            0xff604134,
            0xff3a312a,
            0xff292420,
    })
    public void testMonkSkinToneScaleColorsLiked(int color) {
        assertFalse(DislikeAnalyzer.isDisliked(Hct.fromInt(color)));
    }

    @ParameterizedTest
    @ValueSource(ints = {
            0xff95884B,
            0xff716B40,
            0xffB08E00,
            0xff4C4308,
            0xff464521,
    })
    public void testBileColorsBecameLikable(int color) {
        final Hct hct = Hct.fromInt(color);
        assertTrue(DislikeAnalyzer.isDisliked(hct));

        final Hct likable = DislikeAnalyzer.fixIfDisliked(hct);
        assertFalse(DislikeAnalyzer.isDisliked(likable));
    }

    @Test
    public void testTone67NotDisliked() {
        final Hct color = Hct.from(100.0, 50.0, 67.0);
        assertFalse(DislikeAnalyzer.isDisliked(color));
        assertEquals(color.toInt(), DislikeAnalyzer.fixIfDisliked(color).toInt());
    }
}
