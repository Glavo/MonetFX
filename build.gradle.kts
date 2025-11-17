import kotlin.math.max

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
    id("java-library")
    id("org.glavo.compile-module-info-plugin") version "2.0"
    id("signing")
    id("maven-publish")
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
    id("org.glavo.load-maven-publish-properties") version "0.1.0"
}

allprojects {
    apply {
        plugin("java")
    }

    group = "org.glavo"
    version = "0.4.0" + "-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

description = "Material 3 color system for JavaFX"

dependencies {
    compileOnlyApi("org.jetbrains:annotations:26.0.2")

    testImplementation("com.google.code.gson:gson:2.12.1")
    testImplementation("org.hildan.fxgson:fx-gson:5.0.0")
    testImplementation(platform("org.junit:junit-bom:5.12.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "8"
    targetCompatibility = "8"
}

tasks.withType<Javadoc> {
    (options as StandardJavadocDocletOptions).also {
        it.encoding("UTF-8")
        it.addStringOption("link", "https://docs.oracle.com/en/java/javase/25/docs/api/")
        it.addStringOption("link", "https://openjfx.io/javadoc/25/")
        it.addStringOption("link", "https://javadoc.io/doc/org.jetbrains/annotations/26.0.2/")
        it.addBooleanOption("html5", true)
        it.addStringOption("Xdoclint:none", "-quiet")
    }
}

tasks.test {
    useJUnitPlatform()
    testLogging.showStandardStreams = true

    maxParallelForks = max(Runtime.getRuntime().availableProcessors() / 4, 1)
}

tasks.withType<GenerateModuleMetadata>().configureEach {
    enabled = false
}

configure<PublishingExtension> {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            version = project.version.toString()
            artifactId = project.name
            from(components["java"])

            pom {
                name.set(project.name)
                description.set(project.description)
                url.set("https://github.com/Glavo/MonetFX")

                licenses {
                    license {
                        name.set("Apache-2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        id.set("glavo")
                        name.set("Glavo")
                        email.set("zjx001202@gmail.com")
                    }
                }

                scm {
                    url.set("https://github.com/Glavo/MonetFX")
                }
            }
        }
    }
}

if (rootProject.ext.has("signing.key")) {
    signing {
        useInMemoryPgpKeys(
            rootProject.ext["signing.keyId"].toString(),
            rootProject.ext["signing.key"].toString(),
            rootProject.ext["signing.password"].toString(),
        )
        sign(publishing.publications["maven"])
    }
}

// ./gradlew publishToSonatype closeAndReleaseSonatypeStagingRepository
nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))

            username.set(rootProject.ext["sonatypeUsername"].toString())
            password.set(rootProject.ext["sonatypePassword"].toString())
        }
    }
}