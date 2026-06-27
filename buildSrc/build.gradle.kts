import fe.build.dependencies.Grrfe
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("com.squareup:javapoet:1.13.0")
    implementation("com.google.code.gson:gson:2.13.2")
    implementation("net.nemerosa.versioning:net.nemerosa.versioning.gradle.plugin:3.1.0")
    implementation(Grrfe.std.core.withVersion("0.0.159"))
    implementation(Grrfe.std.process.core.withVersion("0.0.159"))
    implementation(Grrfe.std.process.jvm.withVersion("0.0.159"))
    implementation("app.cash.zipline:zipline-jvm:_")
}

kotlin {
    compilerOptions {
        languageVersion.set(KotlinVersion.KOTLIN_1_9)
        apiVersion.set(KotlinVersion.KOTLIN_1_9)
    }
}
