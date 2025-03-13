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
package org.glavo.monetfx.property;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectPropertyBase;
import javafx.scene.paint.Color;
import org.glavo.monetfx.ColorRole;
import org.glavo.monetfx.ColorScheme;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.EnumMap;
import java.util.Objects;

public final class SimpleColorSchemeProperty extends ObjectPropertyBase<ColorScheme> {

    private static final Object DEFAULT_BEAN = null;
    private static final String DEFAULT_NAME = "";

    private final Object bean;
    private final String name;

    private final EnumMap<ColorRole, WeakReference<ObjectBinding<Color>>> bindings = new EnumMap<>(ColorRole.class);

    public SimpleColorSchemeProperty() {
        this(DEFAULT_BEAN, DEFAULT_NAME);
    }

    public SimpleColorSchemeProperty(ColorScheme initialValue) {
        this(DEFAULT_BEAN, DEFAULT_NAME, initialValue);
    }

    public SimpleColorSchemeProperty(Object bean, String name) {
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }

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

    public @NotNull ObjectBinding<Color> getColor(@NotNull ColorRole role) {
        Objects.requireNonNull(role);

        WeakReference<ObjectBinding<Color>> bindingRef = bindings.get(role);
        ObjectBinding<Color> binding;

        if (bindingRef != null && (binding = bindingRef.get()) != null) {
            return binding;
        }

        binding = new ObjectBinding<Color>() {
            {
                bind(SimpleColorSchemeProperty.this);
            }

            @Override
            public void dispose() {
                unbind(SimpleColorSchemeProperty.this);
            }

            @Override
            protected Color computeValue() {
                ColorScheme colorScheme = SimpleColorSchemeProperty.this.get();
                return colorScheme != null ? colorScheme.getColor(role) : null;
            }
        };

        bindings.put(role, new WeakReference<>(binding));
        return binding;
    }
}
