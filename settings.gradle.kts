@file:Suppress("UnstableApiUsage")

import fe.build.dependencies.Grrfe
import fe.buildsettings.config.MavenRepository
import fe.buildsettings.config.configureRepositories

rootProject.name = "libredirect"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }
    }

    plugins {
        id("de.fayard.refreshVersions") version "0.60.5"
        id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
        id("net.nemerosa.versioning")
        kotlin("jvm")
    }

    when (val gradleBuildDir = extra.properties["gradle.build.dir"]) {
        null -> {
            val gradleBuildVersion = extra.properties["gradle.build.version"]
            val plugins = extra.properties["gradle.build.plugins"]
                .toString().trim().split(",")
                .map { it.trim().split("=") }
                .filter { it.size == 2 }
                .associate { it[0] to it[1] }

            resolutionStrategy {
                eachPlugin {
                    plugins[requested.id.id]?.let { useModule("$it:$gradleBuildVersion") }
                }
            }
        }

        else -> includeBuild(gradleBuildDir.toString())
    }
}

plugins {
    id("de.fayard.refreshVersions")
    id("org.gradle.toolchains.foojay-resolver-convention")
    id("com.gitlab.grrfe.build-settings-plugin")
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
    val wrappersVersion = "2025.2.10"
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
