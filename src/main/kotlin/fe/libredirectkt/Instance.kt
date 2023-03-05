package fe.libredirectkt

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import fe.gson.extensions.array
import fe.gson.extensions.keys

data class Instance(val name: String, val hosts: List<String>)

fun loadLibRedirectInstances(obj: JsonElement): List<Instance> {
    return obj.asJsonObject.keys(map = { it.asJsonObject }).map { (key, instanceObj) ->
        Instance(key, instanceObj.array("clearnet").map { it.asJsonPrimitive.asString })
    }
}
