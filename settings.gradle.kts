rootProject.name = "libredirect"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
        maven(url = "https://jitpack.io")
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.gitlab.grrfe.common-gradle-plugin") {
                useModule("${requested.id.id}:library:0.0.39")
            }
        }
    }

    plugins {
    }
}

include("lib", "bundler")
