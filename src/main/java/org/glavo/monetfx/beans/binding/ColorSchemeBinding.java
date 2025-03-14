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
package org.glavo.monetfx.beans.binding;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Binding;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.glavo.monetfx.ColorScheme;
import org.glavo.monetfx.internal.beans.BindingHelperObserver;
import org.glavo.monetfx.internal.beans.ExpressionHelper;

import java.util.function.Supplier;

public abstract class ColorSchemeBinding extends ColorSchemeExpression implements Binding<ColorScheme> {

    public static ColorSchemeBinding createColorSchemeBinding(Supplier<? extends ColorScheme> func, Observable... dependencies) {
        return new ColorSchemeBinding() {
            {
                bind(dependencies);
            }

            @Override
            protected ColorScheme computeValue() {
                return func.get();
            }

            @Override
            public void dispose() {
                super.unbind(dependencies);
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(dependencies));
            }
        };
    }

    private ColorScheme value;
    private boolean valid = false;
    private BindingHelperObserver observer;
    private ExpressionHelper<ColorScheme> helper = null;

    protected final void bind(Observable... dependencies) {
        if ((dependencies != null) && (dependencies.length > 0)) {
            if (observer == null) {
                observer = new BindingHelperObserver(this);
            }
            for (final Observable dep : dependencies) {
                dep.addListener(observer);
            }
        }
    }

    protected final void unbind(Observable... dependencies) {
        if (observer != null) {
            for (final Observable dep : dependencies) {
                dep.removeListener(observer);
            }
            observer = null;
        }
    }

    @Override
    public ColorScheme get() {
        if (!valid) {
            value = computeValue();
            valid = true;
        }
        return value;
    }

    @Override
    public void addListener(ChangeListener<? super ColorScheme> listener) {
        helper = ExpressionHelper.addListener(helper, this, listener);
    }

    @Override
    public void removeListener(ChangeListener<? super ColorScheme> listener) {
        helper = ExpressionHelper.removeListener(helper, listener);
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
    public void dispose() {
    }

    protected void onInvalidating() {
    }

    @Override
    public final void invalidate() {
        if (valid) {
            valid = false;
            onInvalidating();
            ExpressionHelper.fireValueChangedEvent(helper);
        }
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    protected abstract ColorScheme computeValue();

    @Override
    public String toString() {
        return valid ? "ColorSchemeBinding [value: " + get() + "]" : "ColorSchemeBinding [invalid]";
    }
}
