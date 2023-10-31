package fe.gson.compatlibredirect.extension.io

import com.google.gson.*
import fe.gson.compatlibredirect.util.Json
import java.io.InputStream
import java.io.OutputStream

@Throws(JsonParseException::class, ClassCastException::class)
inline fun <reified T : JsonElement> InputStream.parseJson(): T = Json.parseJson(bufferedReader())
inline fun <reified T : JsonElement> InputStream.parseJsonOrNull(): T? = Json.parseJsonOrNull(bufferedReader())

@Throws(JsonParseException::class, ClassCastException::class)
inline fun <reified T> InputStream.fromJson() = Json.fromJson<T>(bufferedReader())

inline fun <reified T> InputStream.fromJsonOrNull() = Json.fromJsonOrNull<T>(bufferedReader())

@Throws(JsonIOException::class)
inline fun <reified T : JsonElement> OutputStream.toJson(element: T) = Json.toJson<T>(bufferedWriter(), element)

@Throws(JsonIOException::class)
fun OutputStream.toJson(src: Any) = Json.toJson(bufferedWriter(), src)
