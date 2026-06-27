import fe.build.dependencies.Grrfe
import fe.buildsrc.task.LibRedirectCodeBuilder
import fe.buildsrc.task.LibRedirectCodeExtractor
import fe.buildsrc.task.MetadataGeneratorTask
import fe.buildsrc.task.UpdateConfigTask
import fe.buildsrc.task.UpdateInstancesTask
import fe.buildsrc.task.ZiplineCompileTask
import org.gradle.kotlin.dsl.register
import kotlin.io.path.div

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
    description = "Generate build metadata"
    group = "build"
    dir = generatedSrcDir
}

val assemble by tasks
assemble.dependsOn(generateMetadata)


val libRedirectDir = rootProject.layout.projectDirectory.dir("libredirect")
val resourcesDir = project.file("src/main/resources")

val updateInstances = tasks.register<UpdateInstancesTask>("updateInstances") {
    description = "Update LibRedirect instances"
    group = "update"
    file.set(resourcesDir.resolve("libredirect_instances.json"))
}
val updateConfig = tasks.register<UpdateConfigTask>("updateConfig") {
    description = "Update LibRedirect config"
    group = "update"
    file.set(resourcesDir.resolve("libredirect_config.json"))
}
val updateAll = tasks.register("updateAll") {
    description = "Update all"
    group = "update"
    dependsOn(updateConfig, updateInstances)
}

val extractCode = tasks.register<LibRedirectCodeExtractor>("extractCode") {
    description = "Extract LibRedirect code from browser extension"
    group = "build"
    dependsOn(":bundler:jsProductionExecutableCompileSync")
    val bundlerJs = findProject(":bundler")?.layout?.buildDirectory?.file("compileSync/js/main/productionExecutable/kotlin/libredirect-bundler.js")
    bundlerJsPath.set(bundlerJs!!.get().asFile)
    outputDir.set(libRedirectDir)
}
val buildCode = tasks.register<LibRedirectCodeBuilder>("buildCode") {
    description = "Build extracted LibRedirect code with stubs"
    group = "build"
    dependsOn(extractCode)
    inputFilePath.set(libRedirectDir.file("api/src/index.ts").asFile)
    outputDir.set(libRedirectDir.dir("dist"))
}
val compileZipline = tasks.register<ZiplineCompileTask>("compileZipline") {
    description = "Compile extracted LibRedirect code to Zipline"
    group = "build"
    dependsOn(buildCode)
    file.set(libRedirectDir.file("dist/index.js").asFile)
    fileName.set("libredirect")
    outputFile.set(resourcesDir.resolve("libredirect.zipline"))
}

assemble.dependsOn(compileZipline)

val processResources by tasks
processResources.dependsOn(compileZipline)
val sourcesJar by tasks
sourcesJar.dependsOn(compileZipline)
