@file:Suppress("UnstableApiUsage")

import com.gitlab.grrfe.gradlebuild.config.MavenRepository
import com.gitlab.grrfe.gradlebuild.config.configureRepositories
import fe.build.dependencies.Grrfe

rootProject.name = "libredirect"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }
    }

    plugins {
        id("de.fayard.refreshVersions") version "0.60.6"
        id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
        kotlin("jvm")
    }

    when (val gradleBuildDir = extra.properties["gradle.build.dir"]) {
        null -> {
            val gradleBuildVersion = extra.properties["gradle.build.version"]
            resolutionStrategy {
                eachPlugin {
                    with(requested.id) {
                        if (namespace == "com.gitlab.grrfe") {
                            useModule("com.gitlab.grrfe.gradle-build:$name:$gradleBuildVersion")
                        }
                    }
                }
            }
        }
        else -> includeBuild(gradleBuildDir.toString())
    }
}

plugins {
    id("de.fayard.refreshVersions")
    id("org.gradle.toolchains.foojay-resolver-convention")
    id("com.gitlab.grrfe.settings-build-plugin")
}

configureRepositories(
    MavenRepository.MavenCentral,
    MavenRepository.Jitpack,
    MavenRepository.Google,
    mode = RepositoriesMode.PREFER_PROJECT
)

extra.properties["gradle.build.dir"]
    ?.let { includeBuild(it.toString()) }

dependencyResolutionManagement.versionCatalogs.create("kotlinWrappers") {
    val wrappersVersion = "2026.6.7"
    from("org.jetbrains.kotlin-wrappers:kotlin-wrappers-catalog:$wrappersVersion")
}

include(":lib")
include(":bundler")

buildSettings {
    substitutes {
        trySubstitute(Grrfe.std, properties["kotlin-ext.dir"])
        trySubstitute(Grrfe.gsonExt, properties["gson-ext.dir"])
    }
}
