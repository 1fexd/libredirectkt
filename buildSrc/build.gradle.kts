import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("com.squareup:javapoet:1.13.0")
    implementation("com.google.code.gson:gson:2.13.2")
    implementation("net.nemerosa.versioning:net.nemerosa.versioning.gradle.plugin:3.1.0")
}

kotlin {
    compilerOptions {
        languageVersion.set(KotlinVersion.KOTLIN_1_9)
        apiVersion.set(KotlinVersion.KOTLIN_1_9)
    }
}
