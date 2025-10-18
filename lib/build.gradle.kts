import fe.build.dependencies.Grrfe
import fe.buildsrc.task.MetadataGeneratorTask
import fe.buildsrc.task.UpdateConfigTask
import fe.buildsrc.task.UpdateInstancesTask
import org.gradle.kotlin.dsl.register

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


val generatedSrcDir: File = layout.buildDirectory.dir("generated/sources/metadata/main/java").get().asFile

val main by sourceSets
main.java.srcDir(generatedSrcDir)

val generateMetadata = tasks.register<MetadataGeneratorTask>("generateMetadata") {
    group = "build"
    dir = generatedSrcDir
}

val assemble by tasks
assemble.dependsOn(generateMetadata)

val updateInstances = tasks.register<UpdateInstancesTask>("updateInstances") {
    group = "update"
    file.set(project.file("src/main/resources/libredirect_instances.json"))
}
val updateConfig = tasks.register<UpdateConfigTask>("updateConfig") {
    group = "update"
    file.set(project.file("src/main/resources/libredirect_config.json"))
}

val updateAll = tasks.register("updateAll") {
    group = "update"
    dependsOn(updateConfig, updateInstances)
}
