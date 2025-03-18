import org.gradle.internal.declarativedsl.parsing.main

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

plugins {
    id("application")
    id("com.gradleup.shadow") version "8.3.5"
}

dependencies {
    implementation(rootProject)
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "8"
    targetCompatibility = "8"
}

val mainClassName = "org.glavo.monetfx.builder.MonetFXThemeBuilder"

application {
    mainClass.set(mainClassName)
}

tasks.withType<Jar> {
    manifest.attributes("Main-Class" to mainClassName)
}
