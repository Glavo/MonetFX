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

import javafx.beans.property.Property;
import org.glavo.monetfx.ColorScheme;

/// This class provides a full implementation of a [Property] wrapping a [ColorScheme] value.
///
/// @see ColorSchemePropertyBase
public class SimpleColorSchemeProperty extends ColorSchemePropertyBase {

    private static final Object DEFAULT_BEAN = null;
    private static final String DEFAULT_NAME = "";

    private final Object bean;
    private final String name;

    /// The constructor of `ColorSchemeProperty`.
    public SimpleColorSchemeProperty() {
        this(DEFAULT_BEAN, DEFAULT_NAME);
    }

    /// The constructor of `ColorSchemeProperty`.
    ///
    /// @param initialValue the initial value of the wrapped value
    public SimpleColorSchemeProperty(ColorScheme initialValue) {
        this(DEFAULT_BEAN, DEFAULT_NAME, initialValue);
    }


    /// The constructor of `ColorSchemeProperty`.
    ///
    /// @param bean the bean of this `ColorSchemeProperty`
    /// @param name the name of this `ColorSchemeProperty`
    public SimpleColorSchemeProperty(Object bean, String name) {
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }

    /// The constructor of `ColorSchemeProperty`
    ///
    /// @param bean         the bean of this `ColorSchemeProperty`
    /// @param name         the name of this `ColorSchemeProperty`
    /// @param initialValue the initial value of the wrapped value
    public SimpleColorSchemeProperty(Object bean, String name, ColorScheme initialValue) {
        super(initialValue);
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }

    @Override
    public Object getBean() {
        return bean;
    }

    @Override
    public String getName() {
        return name;
    }
}
