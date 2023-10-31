package fe.gson.compatlibredirect.extension

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken

fun JsonReader.nextStringOrNull(): String? {
    val jsonToken = peek()
    if (jsonToken == JsonToken.NULL || jsonToken != JsonToken.STRING) return null
    return nextString()
}
