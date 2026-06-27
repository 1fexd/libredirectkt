package fe.buildsrc.task

import fe.buildsrc.BunLocator
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.AbstractExecTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import java.io.File
import javax.inject.Inject

abstract class LibRedirectCodeExtractor @Inject constructor(
    private val execOperations: ExecOperations
) : AbstractExecTask<LibRedirectCodeExtractor>(LibRedirectCodeExtractor::class.java) {
    @get:InputFile
    abstract val bundlerJsPath: Property<File>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun extract() {
        executable = BunLocator.bunPath(execOperations) ?: error("Bun not found")
        args = listOf(bundlerJsPath.get().toString(), outputDir.get().toString())
        super.exec()
    }
}
