package fe.buildsrc.task

import com.google.gson.JsonObject
import fe.buildsrc.util.asJson
import fe.buildsrc.util.get
import fe.buildsrc.util.gson
import fe.buildsrc.util.httpClient
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class UpdateConfigTask : DefaultTask() {
    companion object {
        private const val CONFIG_JSON_URL = "https://raw.githubusercontent.com/libredirect/libredirect/master/src/config.json"
    }

    @get:Input
    abstract val file: Property<File>

    @TaskAction
    fun fetch() {
        val jsonFile = file.get()
        val response = httpClient.send(get(CONFIG_JSON_URL)) { asJson<JsonObject>() }

        val obj = response.body()
        jsonFile.writeText(gson.toJson(obj))
    }
}
