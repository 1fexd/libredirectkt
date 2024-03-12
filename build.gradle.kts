import fe.plugin.library.LibraryConfig.Companion.library

plugins {
    id("com.gitlab.grrfe.common-gradle-plugin")
    id("app.cash.zipline") version "1.8.0"
}

library("fe.libredirectkt") {
    jvm.set(17)
}

repositories {
    google()
}


//zipline {
//
//}

dependencies {
    relocate("com.gitlab.grrfe:gson-ext:11.0.0")
    relocate("com.google.code.gson:gson:2.10.1")
    relocate("com.github.1fexd:uriparser:0.0.7")

//    implementation("app.cash.zipline:zipline-gradle-plugin:1.8.0") {
//        isTransitive = true
//    }
    implementation("com.gitlab.grrfe.bundled-dependencies:gson-ext:14.0.2-gson2-koin3")
    implementation("app.cash.zipline:zipline-loader-jvm:1.8.0") {
        isTransitive = true
    }

    implementation("app.cash.zipline:zipline-jvm:1.8.0") {
        isTransitive = true
    }
//    implementation("com.gitlab.grrfe.bundled-dependencies:core:14.0.9-gson2-koin3")

}
