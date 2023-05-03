import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"
    java
    maven
    id("net.nemerosa.versioning") version "3.0.0"
}

group = "fe.libredirectkt"
version = versioning.info.tag ?: versioning.info.full ?: "0.0.0"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    api("com.gitlab.grrfe:GSONKtExtensions:2.4.0")
    api("com.google.code.gson:gson:2.10.1")
    testImplementation(kotlin("test"))
}

tasks.withType<Jar> {
    exclude("fetch_latest_libredirect.sh")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
