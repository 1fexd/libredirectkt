plugins {
    `kotlin-dsl`
//    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    gradlePluginPortal()
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation("com.github.johnrengelman.shadow:com.github.johnrengelman.shadow.gradle.plugin:8.1.1")
//    implementation("org.gradle.maven-publish")
}
