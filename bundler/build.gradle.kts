plugins {
}

repositories {
    mavenCentral()
    google()
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation("com.gitlab.grrfe:process-launcher:0.0.6")

    implementation("app.cash.zipline:zipline-loader-jvm:1.14.0") {
        isTransitive = true
    }

    implementation("app.cash.zipline:zipline-jvm:1.15.0") {
        isTransitive = true
    }
}
