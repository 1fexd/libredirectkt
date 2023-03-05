package fe.libredirectkt

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import fe.gson.extensions.array
import fe.gson.extensions.keys
import fe.gson.extensions.obj
import fe.gson.extensions.string

data class Service(val name: String, val url: String, val frontends: List<Frontend>, val targets: List<String>)

data class Frontend(
    val name: String,
    val excludeTargets: List<String>,
    val url: String
)


fun loadLibRedirectServices(elem: JsonElement): List<Service> {
    val obj = elem.asJsonObject
    val services = obj.getAsJsonObject("services")

    return services.keys(map = { it.asJsonObject }).map { (key, service) ->
        val frontends = service.obj("frontends").keys(map = { it.asJsonObject }).map { (frontendKey, frontendObj) ->
            Frontend(
                frontendKey,
                frontendObj.array("excludeTargets")?.map { it.asJsonPrimitive.asString } ?: listOf(),
                frontendObj.string("url")!!)
        }

        Service(
            key,
            service.string("url")!!,
            frontends,
            service.array("targets").map { it.asJsonPrimitive.asString })
    }
}
