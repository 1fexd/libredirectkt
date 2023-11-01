package fe.libredirectkt

import LibRedirectResource
import com.google.gson.JsonElement
import fe.gson.extension.io.fromJson
import fe.gson.extension.json.array.strings
import fe.gson.extension.json.array.stringsOrNull
import fe.gson.extension.json.`object`.*
import fe.gson.util.Json
import java.io.InputStream

object LibRedirectLoader {
    fun loadBuiltInServices() =
        loadLibRedirectServices(loadLibRedirectJson(LibRedirectResource.getBuiltInLibRedirectConfigJson()!!))

    fun loadBuiltInInstances() =
        loadLibRedirectInstances(loadLibRedirectJson(LibRedirectResource.getBuiltInLibRedirectInstancesJson()!!))

    fun loadLibRedirectJson(inputStream: InputStream): JsonElement {
        return inputStream.fromJson<JsonElement>()
    }

    fun loadLibRedirectJson(text: String): JsonElement {
        return Json.fromJson<JsonElement>(text)
    }

    fun loadLibRedirectInstances(obj: JsonElement): List<LibRedirectInstance> {
        return obj.asJsonObject.map { it.asJsonObject }.map { (key, instanceObj) ->
            LibRedirectInstance(key, instanceObj.asArray("clearnet").strings())
        }
    }

    fun loadLibRedirectServices(elem: JsonElement): List<LibRedirectService> {
        val obj = elem.asJsonObject
        val services = obj.getAsJsonObject("services")

        return services.asJsonObject.map { it.asJsonObject }.map { (key, service) ->
            val frontends = service.asObject("frontends").map { it.asJsonObject }.map { (frontendKey, frontendObj) ->
                LibRedirectFrontend(
                    frontendKey,
                    frontendObj.asString("name"),
                    frontendObj.asArrayOrNull("excludeTargets")?.stringsOrNull(false)?.map { it!! } ?: listOf(),
                    frontendObj.asString("url"))
            }

            LibRedirectService(
                key,
                service.asString("name"),
                service.asString("url"),
                frontends,
                frontends.find { it.name == service.asObject("options").asString("frontend") } ?: frontends.first(),
                service.asArray("targets").strings().map { Regex(it) })
        }
    }
}
