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

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.glavo.monetfx.ColorScheme;
import org.glavo.monetfx.internal.beans.ExpressionHelper;

import java.lang.ref.WeakReference;
import java.util.Objects;

/// The class `ColorSchemePropertyBase` is the base class for a property
/// wrapping an arbitrary [ColorScheme].
///
/// It provides all the functionality required for a property except for the
/// [#getBean()] and [#getName()] methods, which must be implemented
/// by extending classes.
///
/// @see ColorSchemeProperty
public abstract class ColorSchemePropertyBase extends ColorSchemeProperty {
    private ColorScheme value;
    private ObservableValue<? extends ColorScheme> observable = null;
    private InvalidationListener listener = null;
    private boolean valid = true;
    private ExpressionHelper<ColorScheme> helper = null;

    public ColorSchemePropertyBase() {
    }

    public ColorSchemePropertyBase(ColorScheme initialValue) {
        this.value = initialValue;
    }

    @Override
    public void addListener(InvalidationListener listener) {
        helper = ExpressionHelper.addListener(helper, this, listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        helper = ExpressionHelper.removeListener(helper, listener);
    }

    @Override
    public void addListener(ChangeListener<? super ColorScheme> listener) {
        helper = ExpressionHelper.addListener(helper, this, listener);
    }

    @Override
    public void removeListener(ChangeListener<? super ColorScheme> listener) {
        helper = ExpressionHelper.removeListener(helper, listener);
    }

    /// Sends notifications to all attached
    /// [InvalidationListeners][javafx.beans.InvalidationListener] and
    /// [ChangeListeners][javafx.beans.value.ChangeListener].
    ///
    /// This method is called when the value is changed, either manually by
    /// calling [#set] or in case of a bound property, if the
    /// binding becomes invalid.
    protected void fireValueChangedEvent() {
        ExpressionHelper.fireValueChangedEvent(helper);
    }

    private void markInvalid() {
        if (valid) {
            valid = false;
            invalidated();
            fireValueChangedEvent();
        }
    }

    /// The method `invalidated()` can be overridden to receive
    /// invalidation notifications. This is the preferred option in
    /// `Objects` defining the property, because it requires less memory.
    ///
    /// The default implementation is empty.
    protected void invalidated() {
    }

    @Override
    public ColorScheme get() {
        valid = true;
        return observable == null ? value : observable.getValue();
    }

    @Override
    public void set(ColorScheme newValue) {
        if (isBound()) {
            throw new java.lang.RuntimeException((getBean() != null && getName() != null ?
                    getBean().getClass().getSimpleName() + "." + getName() + " : " : "") + "A bound value cannot be set.");
        }
        if (!Objects.equals(value, newValue)) {
            value = newValue;
            markInvalid();
        }
    }

    @Override
    public boolean isBound() {
        return observable != null;
    }

    @Override
    public void bind(ObservableValue<? extends ColorScheme> newObservable) {
        if (newObservable == null) {
            throw new NullPointerException("Cannot bind to null");
        }
        if (!newObservable.equals(observable)) {
            unbind();
            observable = newObservable;
            if (listener == null) {
                listener = new Listener(this);
            }
            observable.addListener(listener);
            markInvalid();
        }
    }

    @Override
    public void unbind() {
        if (observable != null) {
            value = observable.getValue();
            observable.removeListener(listener);
            observable = null;
        }
    }

    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder("ColorSchemeProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if ((name != null) && (!name.isEmpty())) {
            result.append("name: ").append(name).append(", ");
        }
        if (isBound()) {
            result.append("bound, ");
            if (valid) {
                result.append("value: ").append(get());
            } else {
                result.append("invalid");
            }
        } else {
            result.append("value: ").append(get());
        }
        result.append("]");
        return result.toString();
    }

    private static final class Listener implements InvalidationListener, WeakListener {

        private final WeakReference<ColorSchemePropertyBase> weakRef;

        public Listener(ColorSchemePropertyBase ref) {
            this.weakRef = new WeakReference<>(ref);
        }

        @Override
        public boolean wasGarbageCollected() {
            return weakRef.get() == null;
        }

        @Override
        public void invalidated(Observable observable) {
            ColorSchemePropertyBase ref = weakRef.get();
            if (ref != null) {
                ref.markInvalid();
            } else {
                observable.removeListener(this);
            }
        }
    }
}
