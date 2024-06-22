import fe.plugin.library.LibraryConfig.Companion.library

plugins {
    id("com.gitlab.grrfe.common-gradle-plugin")
}

project.library("fe.libredirectkt") {
    jvm.set(17)
}

repositories {
    google()
}

dependencies {
    implementation("fe.gson-ext:core")
    implementation("fe.uribuilder:uriparser")

//    implementation("app.cash.zipline:zipline-gradle-plugin:1.8.0") {
//        isTransitive = true
//    }
    implementation("app.cash.zipline:zipline-loader-jvm:1.13.0") {
        isTransitive = true
    }

    implementation("app.cash.zipline:zipline-jvm:1.13.0") {
        isTransitive = true
    }
//    implementation("com.gitlab.grrfe.bundled-dependencies:core:14.0.9-gson2-koin3")

}
