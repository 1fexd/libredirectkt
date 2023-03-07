package fe.libredirectkt

import LibRedirectResource
import com.google.gson.JsonElement
import fe.gson.extensions.*
import java.io.InputStream

object LibRedirectLoader {
    fun loadBuiltInServices() = loadLibRedirectServices(loadLibRedirectJson(LibRedirectResource.getBuiltInLibRedirectConfigJson()!!))
    fun loadBuiltInInstances() = loadLibRedirectInstances(loadLibRedirectJson(LibRedirectResource.getBuiltInLibRedirectInstancesJson()!!))

    fun loadLibRedirectJson(inputStream: InputStream) = inputStream.use { parseReaderAs<JsonElement>(it.reader()) }
    fun loadLibRedirectJson(text: String) = parseStringAs<JsonElement>(text)

    fun loadLibRedirectInstances(obj: JsonElement): List<LibRedirectInstance> {
        return obj.asJsonObject.keys(map = { it.asJsonObject }).map { (key, instanceObj) ->
            LibRedirectInstance(key, instanceObj.array("clearnet").map { it.asString() })
        }
    }

    fun loadLibRedirectServices(elem: JsonElement): List<LibRedirectService> {
        val obj = elem.asJsonObject
        val services = obj.getAsJsonObject("services")

        return services.keys(map = { it.asJsonObject }).map { (key, service) ->
            val frontends = service.obj("frontends").keys(map = { it.asJsonObject }).map { (frontendKey, frontendObj) ->
                LibRedirectFrontend(
                    frontendKey,
                    frontendObj.string("name")!!,
                    frontendObj.array("excludeTargets")?.map { it.asJsonPrimitive.asString } ?: listOf(),
                    frontendObj.string("url")!!)
            }

            LibRedirectService(
                key,
                service.string("name")!!,
                service.string("url")!!,
                frontends,
                frontends.find { it.name == service.obj("options").string("frontend") } ?: frontends.first(),
                service.array("targets").map { Regex(it.asString()) })
        }
    }
}
