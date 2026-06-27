@file:OptIn(EngineApi::class)

package fe.buildsrc.task

import app.cash.zipline.EngineApi
import app.cash.zipline.Zipline
import com.google.gson.JsonObject
import fe.buildsrc.util.asJson
import fe.buildsrc.util.get
import fe.buildsrc.util.gson
import fe.buildsrc.util.httpClient
import kotlinx.coroutines.asCoroutineDispatcher
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

abstract class ZiplineCompileTask : DefaultTask() {
    @get:InputFile
    abstract val file: Property<File>
    @get:Input
    abstract val fileName: Property<String>
    @get:OutputFile
    abstract val outputFile: Property<File>

    @TaskAction
    fun compile() {
        val executor = Executors.newSingleThreadExecutor { Thread(it, "Zipline") }
        val zipline = Zipline.create(executor.asCoroutineDispatcher())

        try {
            val text = file.get().readText()
            val compiledBytes = zipline.quickJs.compile(text, fileName.get())
            outputFile.get().writeBytes(compiledBytes)
        } finally {
            executor.shutdown()
            executor.awaitTermination(1, TimeUnit.MINUTES)
        }
    }
}
