package fe.gson.extension

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import fe.gson.dsl.JsonArrayDsl
import java.io.Reader
import java.io.Writer
import java.lang.reflect.Type


@Throws(JsonParseException::class)
inline fun <reified T> Gson.readJson(reader: Reader, typeToken: Type = object : TypeToken<T>() {}.type): T {
    return this.fromJson(reader, typeToken)
}

@Throws(JsonIOException::class)
inline fun <reified T : JsonElement> Gson.writeJson(jsonElement: T, writer: Writer) {
    this.toJson(jsonElement, writer)
}

@Throws(JsonIOException::class)
fun Gson.writeJson(src: Any, writer: Writer) {
    this.toJson(src, writer)
}


