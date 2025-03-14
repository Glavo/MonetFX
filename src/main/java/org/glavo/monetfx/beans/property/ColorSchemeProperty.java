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
package org.glavo.monetfx.beans.property;

import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import org.glavo.monetfx.ColorScheme;
import org.glavo.monetfx.beans.value.WritableColorSchemeValue;

public abstract class ColorSchemeProperty extends ReadOnlyColorSchemeProperty implements Property<ColorScheme>, WritableColorSchemeValue {
    @Override
    public void setValue(ColorScheme v) {
        set(v);
    }

    @Override
    public void bindBidirectional(Property<ColorScheme> other) {
        Bindings.bindBidirectional(this, other);
    }

    @Override
    public void unbindBidirectional(Property<ColorScheme> other) {
        Bindings.unbindBidirectional(this, other);
    }

    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder("ColorSchemeProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if (name != null && (!name.isEmpty())) {
            result.append("name: ").append(name).append(", ");
        }
        result.append("value: ").append(get()).append("]");
        return result.toString();
    }
}
