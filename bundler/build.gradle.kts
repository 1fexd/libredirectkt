plugins {
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation("app.cash.zipline:zipline-loader-jvm:1.9.0") {
        isTransitive = true
    }

    implementation("app.cash.zipline:zipline-jvm:1.9.0") {
        isTransitive = true
    }
}
