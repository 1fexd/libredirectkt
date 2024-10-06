plugins {
    kotlin("jvm") version "2.0.20" apply false
    id("com.gitlab.grrfe.common-gradle-plugin") apply false
}

repositories {
    mavenCentral()
    google()
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
}
