package fe.gson.compatlibredirect.extension

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonPrimitive
import fe.gson.compatlibredirect.GlobalGsonContext
import fe.gson.compatlibredirect.dsl.JsonArrayDsl
import fe.gson.compatlibredirect.dsl.JsonObjectDsl
import fe.gson.compatlibredirect.dsl.NumberValue


fun Any?.toJsonElement(): JsonElement? {
    if (this == null) return JsonNull.INSTANCE
    if (this is Iterable<*>) {
        val array = JsonArray()
        this.forEach { item -> array.add(item.toJsonElement()) }

        return array
    }

    return when (this) {
        is String -> JsonPrimitive(this)
        is Boolean -> JsonPrimitive(this)
        is Char -> JsonPrimitive(this)
        is Number -> JsonPrimitive(this)
        is NumberValue -> JsonPrimitive(this.value)
        is JsonObjectDsl, is JsonArrayDsl -> this.toJsonElement()
        is JsonElement -> this
        else -> GlobalGsonContext.gson.toJsonTree(this)
    }
}
