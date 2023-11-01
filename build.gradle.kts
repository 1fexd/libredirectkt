plugins {
    kotlin("jvm") version "1.9.20"
    java
    `maven-publish`
    id("net.nemerosa.versioning") version "3.0.0"
}

group = "fe.libredirectkt"
version = versioning.info.tag ?: versioning.info.full ?: "0.0.0"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation("com.gitlab.grrfe:gson-ext:11.0.0")
    implementation("com.google.code.gson:gson:2.10.1")

    testImplementation(kotlin("test"))
}

tasks.withType<Jar> {
    exclude("fetch_latest_libredirect.sh")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            version = project.version.toString()

            from(components["java"])
        }
    }
}
