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

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public final class JavaFXLauncher {

    private JavaFXLauncher() {
    }

    private static boolean started = false;

    static {
        // init JavaFX Toolkit
        try {
            // Java 9 or Latter
            final MethodHandle startup =
                    MethodHandles.publicLookup().findStatic(
                            javafx.application.Platform.class, "startup", MethodType.methodType(void.class, Runnable.class));
            startup.invokeExact((Runnable) () -> {
            });
            started = true;
        } catch (NoSuchMethodException e) {
            // Java 8
            try {
                Class.forName("javafx.embed.swing.JFXPanel").getDeclaredConstructor().newInstance();
                started = true;
            } catch (Throwable e0) {
                e0.printStackTrace(System.err);
            }
        } catch (IllegalStateException e) {
            started = true;
        } catch (Throwable e) {
            e.printStackTrace(System.err);
        }
    }

    public static boolean isStarted() {
        return started;
    }
}
