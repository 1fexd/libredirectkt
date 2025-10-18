package fe.buildsrc.task

import fe.buildsrc.MetadataClassGenerator
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class MetadataGeneratorTask : DefaultTask() {
    @get:Input
    abstract val dir: Property<File>

    @TaskAction
    fun generate() {
        val hash = getInstancesHash()
        val javaFile = MetadataClassGenerator.build(System.currentTimeMillis(), hash)

        val dir = dir.get()
        dir.mkdirs()

        File(dir, "${javaFile.typeSpec.name}.java").delete()
        javaFile.writeToFile(dir)
    }

    private fun getInstancesHash(): String {
        val process = Runtime.getRuntime().exec("git ls-remote https://github.com/libredirect/instances")
        val line = process.inputReader().use { it.readLine() }

        return line.substringBefore("\t")
    }
}
