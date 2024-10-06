plugins {
    application
}

repositories {
    mavenCentral()
    google()
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation("com.gitlab.grrfe:process-launcher:0.0.6")
    implementation("app.cash.zipline:zipline-jvm:1.17.0")
}

application {
    mainClass.set("fe.libredirectkt.MainKt")
}

tasks.getByName<Jar>("jar") {
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }

    excludes += setOf("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")

    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
