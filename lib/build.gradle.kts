import fe.build.dependencies.Grrfe

dependencies {
    implementation(platform(Grrfe.std.bom))
    implementation(Grrfe.std.core)

    implementation(platform(Grrfe.httpkt.bom))
    implementation(Grrfe.httpkt.core2.core)
    implementation(Grrfe.httpkt.serialization.gson)

    implementation(platform(Grrfe.gsonExt.bom))
    implementation(Grrfe.gsonExt.core)

    implementation("com.google.code.gson:gson:_")
    testImplementation("com.google.code.gson:gson:_")
    implementation("app.cash.zipline:zipline:_") {
        isTransitive = true
    }

    testImplementation(KotlinX.coroutines.test)
    testImplementation("com.willowtreeapps.assertk:assertk:_")
    testImplementation(kotlin("test"))
}
