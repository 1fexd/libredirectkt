package fe.gson.compatlibredirect.extension.json.array

import com.google.gson.JsonArray
import com.google.gson.JsonElement

@Throws(IllegalStateException::class, AssertionError::class, NumberFormatException::class)
inline fun <T> JsonArray.mapWithNulls(keepNulls: Boolean = true, transform: (JsonElement) -> T?): List<T?> {
    val list = ArrayList<T?>(size())
    for (element in this) {
        val transformed = transform(element)
        if (keepNulls || transformed != null) {
            list.add(transformed)
        }
    }

    return list
}

@Throws(ClassCastException::class)
inline fun <reified T : JsonElement> JsonArray.elements(): List<T> = map { it as T }

inline fun <reified T : JsonElement> JsonArray.elementsOrNull(
    keepNulls: Boolean = true
): List<T?> = mapWithNulls(keepNulls) { it as? T }
