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

/// The platform on which this scheme is intended to be used.
/// 
/// @since 0.2.0
public enum TargetPlatform {
    /// Standard platform (e.g., desktop, tablet).
    PHONE,

    /// Wear OS platform (e.g., smartwatches).
    WATCH;

    /// The default target platform.
    ///
    ///  It is equivalent to [PHONE][#PHONE].
    public static final TargetPlatform DEFAULT = TargetPlatform.PHONE;
}
