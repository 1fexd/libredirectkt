package fe.buildsrc.task

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import fe.buildsrc.util.asJson
import fe.buildsrc.util.get
import fe.buildsrc.util.gson
import fe.buildsrc.util.httpClient
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File
import kotlin.collections.iterator

abstract class UpdateInstancesTask : DefaultTask() {
    companion object {
        private const val INSTANCES_JSON_URL = "https://raw.githubusercontent.com/libredirect/instances/main/data.json"
        private const val FIX_CHAR = "> "
    }

    @get:Input
    abstract val file: Property<File>

    @TaskAction
    fun fetch() {
        val jsonFile = file.get()
        val response = httpClient.send(get(INSTANCES_JSON_URL)) { asJson<JsonObject>() }

        val obj = response.body()

        for (key in obj.keySet()) {
            val service = obj.getAsJsonObject(key)
            val clearnet = service.getAsJsonArray("clearnet")

            fixClearnet(clearnet)
        }

        jsonFile.writeText(gson.toJson(obj))
    }

    // "https://icons.duckduckgo.com/ip3/send.vis.ee.ico\" height=\"16\"> https://send.vis.ee",
    private fun fixClearnet(array: JsonArray) {
        val changes = mutableMapOf<Int, String>()
        for ((i, element) in array.withIndex()) {
            val url = element.asJsonPrimitive.asString
            val fixIdx = url.indexOf(FIX_CHAR)
            if (fixIdx != -1) {
                val fixedUrl = url.substring(fixIdx + FIX_CHAR.length)
                changes[i] = fixedUrl
            }
        }

        for ((i, fixedUrl) in changes) {
            array.set(i, JsonPrimitive(fixedUrl))
        }
    }
}
