package fe.gson.compatlibredirect.extension.json.element

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive


inline fun <T> JsonElement.primitiveMappedOrNull(fn: (JsonPrimitive) -> T): T? {
    val primitive = primitiveOrNull() ?: return null
    return try {
        fn(primitive)
    } catch (e: Throwable) {
        null
    }
}

@Throws(IllegalStateException::class)
fun JsonElement.primitive(): JsonPrimitive = asJsonPrimitive
fun JsonElement.primitiveOrNull(): JsonPrimitive? = try {
    asJsonPrimitive
} catch (e: Throwable) {
    null
}


@Throws(IllegalStateException::class)
fun JsonElement.`object`(): JsonObject = asJsonObject
fun JsonElement.objectOrNull(): JsonObject? = try {
    asJsonObject
} catch (e: Throwable) {
    null
}

@Throws(IllegalStateException::class)
fun JsonElement.array(): JsonArray = asJsonArray
fun JsonElement.arrayOrNull(): JsonArray? = try {
    asJsonArray
} catch (e: Throwable) {
    null
}
