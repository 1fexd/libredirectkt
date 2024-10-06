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
    api(platform("com.github.1fexd:super:0.0.5"))
    api("com.gitlab.grrfe.gson-ext:core")
    api("com.github.1fexd:uriparser")

    implementation("app.cash.zipline:zipline:1.17.0") {
        isTransitive = true
    }
}
