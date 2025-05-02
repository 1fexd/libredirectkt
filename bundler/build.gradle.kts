import fe.build.dependencies.Grrfe

plugins {
    kotlin("multiplatform")
}

kotlin {
    js(IR) {
        // https://github.com/Kotlin/kotlinx-io/issues/345
        useCommonJs()
        nodejs {}
        binaries.executable()
    }

    jvm {
    }

    sourceSets {
        commonMain.dependencies {
            implementation(Square.okio)
            implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.6.0")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0-RC")
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation("com.willowtreeapps.assertk:assertk:0.28.1")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.1")
        }

        jsMain.dependencies {
            implementation(kotlinWrappers.typescript)
            implementation(kotlinWrappers.node)

            implementation(npm("typescript", "^5.0.0"))
            implementation("com.squareup.okio:okio-nodefilesystem:3.10.2")
//            implementation(Grrfe.std.io.kotlinx.core)
//            println(Grrfe.std.io.kotlinx.core)
        }

//        val jsMain by getting {
//            dependencies {
//
//            }
//        }

        jvmMain.dependencies {
            implementation(Grrfe.std.process.jvm)
            implementation("app.cash.zipline:zipline-jvm:1.17.0")
        }

        jvmTest.dependencies {
        }
    }
}

//application {
//    mainClass.set("fe.libredirectkt.MainKt")
//}
//
//tasks.getByName<Jar>("jar") {
//    manifest {
//        attributes["Main-Class"] = application.mainClass.get()
//    }
//
//    excludes += setOf("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
//
//    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
//    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
//}
