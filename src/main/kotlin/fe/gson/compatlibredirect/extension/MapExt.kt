package fe.gson.compatlibredirect.extension

import com.google.gson.JsonObject

fun Map<String, Any?>.toJsonObject(): JsonObject {
    return JsonObject().apply {
        forEach { (key, value) -> add(key, value?.toJsonElement()) }
    }
}
