@file:OptIn(ExperimentalMainFunctionArgumentsDsl::class)

import fe.build.dependencies.Grrfe
import fe.build.dependencies.external.Ajalt
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

    jvm {
        mainRun {
            mainClass.set("fe.libredirectkt.MainKt")
        }

        this.binaries {
            this.executable {
                mainClass.set("fe.libredirectkt.MainKt")

//    manifest {
//        attributes["Main-Class"] = application.mainClass.get()
//    }
//                excludes += setOf("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
//
//    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
//    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            }
        }
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

        jvmMain.dependencies {
            implementation("app.cash.zipline:zipline-jvm:_")
            implementation(Ktor.client.core)
            implementation(Ktor.client.cio)
            implementation("io.ktor:ktor-client-cio-jvm:_")
            implementation(Ktor.client.okHttp)
            implementation(Ktor.client.contentNegotiation)
            implementation(Ktor.plugins.serialization.kotlinx.json)
            implementation(Grrfe.std.core.withVersion("0.0.159"))
            implementation(Grrfe.std.process.core.withVersion("0.0.159"))
            implementation(Grrfe.std.process.jvm.withVersion("0.0.159"))
            implementation(Ajalt.clikt.core)
            implementation(Ajalt.clikt.markdown)
            implementation(Ajalt.mordant.core)
            implementation(Ajalt.mordant.coroutines)
        }

        jvmTest.dependencies {
        }
    }
}
