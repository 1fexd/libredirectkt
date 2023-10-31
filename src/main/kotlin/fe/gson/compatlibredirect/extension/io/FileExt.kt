package fe.gson.compatlibredirect.extension.io

import com.google.gson.*
import fe.gson.compatlibredirect.util.*
import java.io.File

@Throws(JsonParseException::class, ClassCastException::class)
inline fun <reified T : JsonElement> File.parseJson(): T = Json.parseJson(bufferedReader())
inline fun <reified T : JsonElement> File.parseJsonOrNull(): T? = Json.parseJsonOrNull(bufferedReader())

@Throws(JsonParseException::class, ClassCastException::class)
inline fun <reified T> File.fromJson() = Json.fromJson<T>(bufferedReader())

inline fun <reified T> File.fromJsonOrNull() = Json.fromJsonOrNull<T>(bufferedReader())

@Throws(JsonIOException::class)
inline fun <reified T : JsonElement> File.toJson(element: T) = Json.toJson<T>(bufferedWriter(), element)

@Throws(JsonIOException::class)
fun File.toJson(src: Any) = Json.toJson(bufferedWriter(), src)
