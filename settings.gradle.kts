pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
        maven { url = uri("https://jitpack.io") }
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.gitlab.grrfe.common-gradle-plugin") {
                useModule("${requested.id.id}:library:0.0.39")
            }
        }
    }

    plugins {
        id("app.cash.zipline") version "1.8.0"
        id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"

    }
}

rootProject.name = "libredirectkt"
include("lib", "bundler")
