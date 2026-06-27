@file:OptIn(ExperimentalMainFunctionArgumentsDsl::class)

import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalMainFunctionArgumentsDsl

plugins {
    kotlin("multiplatform")
}

kotlin {
    js {
        // https://github.com/Kotlin/kotlinx-io/issues/345
        useCommonJs()
        nodejs {
            runTask {
            }
            this.passCliArgumentsToMainFunction()
        }
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(Square.okio)
            implementation("org.jetbrains.kotlinx:kotlinx-io-core:_")
            implementation(KotlinX.coroutines.core)
            implementation(KotlinX.serialization.json)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation("com.willowtreeapps.assertk:assertk:_")
            implementation(KotlinX.coroutines.test)
        }

        jsMain.dependencies {
            implementation(kotlinWrappers.typescript)
            implementation(kotlinWrappers.node)

            implementation(npm("typescript", "^5.0.0"))
            implementation("com.squareup.okio:okio-nodefilesystem:_")
        }
    }
}
