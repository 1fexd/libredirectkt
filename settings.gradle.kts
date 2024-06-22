rootProject.name = "libredirect"

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
        id("app.cash.zipline") version "1.13.0"
    }
}

include("lib", "bundler")

includeBuild("../gson-ext")
includeBuild("../uriparser")
