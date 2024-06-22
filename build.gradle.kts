plugins {
    kotlin("jvm") version "1.9.23" apply false
    id("com.gitlab.grrfe.common-gradle-plugin") apply false
    id("app.cash.zipline") version "1.13.0"
}

repositories {
    mavenCentral()
    google()
}

allprojects {
    apply(plugin = "app.cash.zipline")
    apply(plugin = "org.jetbrains.kotlin.jvm")
}
