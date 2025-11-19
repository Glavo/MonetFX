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

import org.glavo.monetfx.ColorScheme;

/// This class provides a convenient class to define read-only properties. It
/// creates two properties that are synchronized. One property is read-only
/// and can be passed to external users. The other property is read- and
/// writable and should be used internally only.
///
/// @since 0.4.0
public class ReadOnlyColorSchemeWrapper extends SimpleColorSchemeProperty {

    private ReadOnlyColorSchemeWrapper.ReadOnlyPropertyImpl readOnlyProperty;

    /// The constructor of `ReadOnlyColorSchemeWrapper`
    public ReadOnlyColorSchemeWrapper() {
    }

    /// The constructor of `ReadOnlyColorSchemeWrapper`
    ///
    /// @param initialValue the initial value of the wrapped value
    public ReadOnlyColorSchemeWrapper(ColorScheme initialValue) {
        super(initialValue);
    }

    /// The constructor of `ReadOnlyColorSchemeWrapper`
    ///
    /// @param bean the bean of this `ReadOnlyColorSchemeWrapper`
    /// @param name the name of this `ReadOnlyColorSchemeWrapper`
    public ReadOnlyColorSchemeWrapper(Object bean, String name) {
        super(bean, name);
    }

    /// The constructor of `ReadOnlyColorSchemeWrapper`
    ///
    /// @param bean         the bean of this `ReadOnlyColorSchemeWrapper`
    /// @param name         the name of this `ReadOnlyColorSchemeWrapper`
    /// @param initialValue the initial value of the wrapped value
    public ReadOnlyColorSchemeWrapper(Object bean, String name, ColorScheme initialValue) {
        super(bean, name, initialValue);
    }

    /// Returns the readonly property, that is synchronized with this `ReadOnlyColorSchemeWrapper`.
    ///
    /// @return the readonly property
    public ReadOnlyColorSchemeProperty getReadOnlyProperty() {
        if (readOnlyProperty == null) {
            readOnlyProperty = new ReadOnlyColorSchemeWrapper.ReadOnlyPropertyImpl();
        }
        return readOnlyProperty;
    }

    @Override
    protected void fireValueChangedEvent() {
        super.fireValueChangedEvent();
        if (readOnlyProperty != null) {
            readOnlyProperty.fireValueChangedEvent();
        }
    }

    private final class ReadOnlyPropertyImpl extends ReadOnlyColorSchemePropertyBase {

        @Override
        public ColorScheme get() {
            return ReadOnlyColorSchemeWrapper.this.get();
        }

        @Override
        public Object getBean() {
            return ReadOnlyColorSchemeWrapper.this.getBean();
        }

        @Override
        public String getName() {
            return ReadOnlyColorSchemeWrapper.this.getName();
        }
    }
}
