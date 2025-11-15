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
package org.glavo.monetfx.beans.value;

import javafx.beans.value.WritableObjectValue;
import javafx.beans.value.WritableValue;
import org.glavo.monetfx.ColorScheme;

/// A writable [ColorScheme] value.
///
/// @see WritableValue
/// @see WritableObjectValue
public interface WritableColorSchemeValue extends WritableObjectValue<ColorScheme> {
}
