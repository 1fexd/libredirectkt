package fe.buildsrc.task

import fe.buildsrc.BunLocator
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.AbstractExecTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import java.io.File
import javax.inject.Inject
import kotlin.io.path.pathString

abstract class LibRedirectCodeBuilder @Inject constructor(
    private val execOperations: ExecOperations
): AbstractExecTask<LibRedirectCodeBuilder>(LibRedirectCodeBuilder::class.java) {
    @get:InputFile
    abstract val inputFilePath: Property<File>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun extract() {
        executable = BunLocator().bunPath(execOperations)?.pathString ?: error("Bun not found")
        args = listOf(
            "build",
            inputFilePath.get().toString(),
            "--outdir",
            outputDir.get().toString(),
            "--minify"
        )
        super.exec()
    }
}
