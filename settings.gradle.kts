rootProject.name = "libredirectkt"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("https://jitpack.io")
        }
    }


    plugins {
        id("com.github.johnrengelman.shadow") version "8.1.1"
        id("net.nemerosa.versioning") version "3.0.0"
        `maven-publish`
//        id 'org.springframework.boot' version "2.3.3.RELEASE"
//        id 'io.spring.dependency-management' version '1.0.10.RELEASE'
    }
}
