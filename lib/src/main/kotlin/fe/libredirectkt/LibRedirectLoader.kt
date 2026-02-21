package fe.libredirectkt

import LibRedirectResource
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import fe.gson.extension.io.fromJson
import fe.gson.extension.json.array.strings
import fe.gson.extension.json.array.stringsOrNull
import fe.gson.extension.json.`object`.*
import fe.gson.util.Json
import java.io.InputStream

public object LibRedirectLoader {
    public fun loadBuiltInServices(): List<LibRedirectService> {
        return loadLibRedirectServices(loadLibRedirectJson(LibRedirectResource.getBuiltInLibRedirectConfigJson()!!))
    }

    public fun loadBuiltInInstances(): List<LibRedirectInstance> {
        return loadLibRedirectInstances(loadLibRedirectJson(LibRedirectResource.getBuiltInLibRedirectInstancesJson()!!))
    }

    public fun loadLibRedirectJson(inputStream: InputStream): JsonElement {
        return inputStream.fromJson<JsonElement>()
    }

    public fun loadLibRedirectJson(text: String): JsonElement {
        return Json.fromJson<JsonElement>(text)
    }

    public fun loadLibRedirectInstances(obj: JsonElement): List<LibRedirectInstance> {
        return obj.asJsonObject
            .map { it.asJsonObject }
            .map { (key, instanceObj) -> createInstance(key, instanceObj) }
    }

    public fun createInstance(key: String, instanceObj: JsonObject): LibRedirectInstance {
        return LibRedirectInstance(
            frontendKey = key,
            hosts = instanceObj.asArray("clearnet").strings()
        )
    }

    public fun loadLibRedirectServices(elem: JsonElement): List<LibRedirectService> {
        val obj = elem.asJsonObject
        val services = obj.getAsJsonObject("services")

        return services.asJsonObject
            .map { it.asJsonObject }
            .map { (key, service) -> createService(key, service) }
    }

    public fun createService(key: String, service: JsonObject): LibRedirectService {
        val frontends = service.asObject("frontends")
            .map { it.asJsonObject }
            .map { (frontendKey, frontendObj) -> createFrontend(frontendKey, frontendObj) }

        return LibRedirectService(
            key = key,
            name = service.asString("name"),
            url = service.asString("url"),
            frontends = frontends,
            defaultFrontend = getDefaultFrontend(service, frontends),
            targets = service.asArray("targets").strings().map { Regex(it) }
        )
    }

    public fun getDefaultFrontend(service: JsonObject, frontends: List<LibRedirectFrontend>): LibRedirectFrontend {
        return frontends.find { it.name == service.asObject("options").asString("frontend") } ?: frontends.first()
    }

    public fun createFrontend(frontendKey: String, frontendObj: JsonObject): LibRedirectFrontend {
        return LibRedirectFrontend(
            key = frontendKey,
            name = frontendObj.asString("name"),
            excludeTargets = frontendObj
                .asArrayOrNull("excludeTargets")
                ?.stringsOrNull(false)?.map { it!! }
                ?: listOf(),
            url = frontendObj.asString("url")
        )
    }
}
