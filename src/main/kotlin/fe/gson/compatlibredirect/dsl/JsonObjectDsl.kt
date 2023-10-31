package fe.gson.compatlibredirect.dsl

import com.google.gson.JsonObject
import fe.gson.compatlibredirect.extension.toJsonElement

typealias JsonObjectDslInit = JsonObjectDsl.() -> Unit

inline fun jsonObject(jsonObject: JsonObject = JsonObject(), init: JsonObjectDslInit): JsonObject {
    JsonObjectDsl(jsonObject).apply(init)
    return jsonObject
}

class JsonObjectDsl(val jsonObject: JsonObject = JsonObject()) {
    operator fun String.plusAssign(any: Any?) {
        any.toJsonElement()?.let { jsonObject.add(this, it) }
    }
}
