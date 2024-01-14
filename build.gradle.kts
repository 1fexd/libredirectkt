import fe.buildsrc.Dependency.bundledDependency

plugins {
    kotlin("jvm") version "1.9.22"
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow")
    id("net.nemerosa.versioning")
}

group = "fe.libredirectkt"
version = versioning.info.tag ?: versioning.info.full ?: "0.0.0"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

val implementation by configurations
val shadowImplementation = configurations.create("shadowImplementation") {
    implementation.extendsFrom(this)
    isTransitive = false
}

dependencies {
    api(kotlin("stdlib"))

    bundledDependency("com.gitlab.grrfe:gson-ext:11.0.0")
    bundledDependency("com.google.code.gson:gson:2.10.1")
    bundledDependency("com.github.1fexd:uriparser:0.0.7")

    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(17)
}

tasks.withType<Jar> {
    exclude("*/**/*.idea")
}

tasks.test {
    useJUnitPlatform()
}

tasks.named("jar") {
    enabled = false
}

publishing {
    publications {
        create<MavenPublication>("shadow") {
            shadow.component(this)

            groupId = project.group.toString()
            version = project.version.toString()
        }
    }
}
