package fe.gson.extension.json.`object`

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import fe.gson.exception.NoSuchKeyInElementFoundException
import fe.gson.exception.noSuchKeyException

fun JsonObject.singleOrNull(): Pair<String, JsonElement>? {
    return if (size() == 1) {
        val single = asMap().iterator().next()
        return single.key to single.value
    } else null
}

@Throws(ClassCastException::class, NoSuchKeyInElementFoundException::class)
fun JsonObject.asObject(key: String): JsonObject = getAsJsonObject(key) ?: noSuchKeyException(this, key)
fun JsonObject.asObjectOrNull(key: String): JsonObject? = try {
    asObject(key)
} catch (e: Throwable) {
    null
}

@Throws(ClassCastException::class, NoSuchKeyInElementFoundException::class)
fun JsonObject.asArray(key: String): JsonArray = getAsJsonArray(key) ?: noSuchKeyException(this, key)
fun JsonObject.asArrayOrNull(key: String): JsonArray? = try {
    asArray(key)
} catch (e: Throwable) {
    null
}

@Throws(ClassCastException::class, NoSuchKeyInElementFoundException::class)
fun JsonObject.asPrimitive(key: String): JsonPrimitive = getAsJsonPrimitive(key) ?: noSuchKeyException(this, key)
fun JsonObject.asPrimitiveOrNull(key: String): JsonPrimitive? = try {
    asPrimitive(key)
} catch (e: Throwable) {
    null
}

@Throws(IllegalStateException::class, AssertionError::class, NumberFormatException::class)
inline fun <T> JsonObject.mapWithNulls(
    keepNulls: Boolean = true,
    transform: (JsonElement) -> T?
): Map<String, T?> {
    return asMap().mapNotNull { (key, value) ->
        val transformed = transform(value)
        if (keepNulls || transformed != null) {
            key to transform(value)
        } else null
    }.toMap()
}

inline fun <T> JsonObject.map(transform: (JsonElement) -> T): Map<String, T> {
    return asMap().map { (key, value) -> key to transform(value) }.toMap()
}


inline fun <reified T> JsonObject.asPrimitiveMappedOrNull(
    key: String,
    fn: (JsonPrimitive) -> T
): T? = try {
    fn(asPrimitive(key))
} catch (e: Throwable) {
    null
}
