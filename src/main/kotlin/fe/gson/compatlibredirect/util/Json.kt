package fe.gson.compatlibredirect.util

import com.google.gson.*
import fe.gson.compatlibredirect.GlobalGsonContext
import fe.gson.compatlibredirect.extension.readJson
import fe.gson.compatlibredirect.extension.writeJson
import java.io.Reader
import java.io.Writer

object Json {
    @Throws(JsonParseException::class, ClassCastException::class)
    inline fun <reified T : JsonElement> parseJson(reader: Reader): T = reader.use { JsonParser.parseReader(it) as T }

    @Throws(JsonParseException::class, ClassCastException::class)
    inline fun <reified T : JsonElement> parseJson(string: String): T = parseJson<T>(string.reader())

    inline fun <reified T : JsonElement> parseJsonOrNull(reader: Reader): T? = try {
        parseJson<T>(reader)
    } catch (e: Throwable) {
        null
    }

    inline fun <reified T : JsonElement> parseJsonOrNull(string: String): T? = parseJsonOrNull(string.reader())

    @Throws(JsonParseException::class, ClassCastException::class)
    inline fun <reified T> fromJson(reader: Reader): T = reader.use { GlobalGsonContext.gson.readJson<T>(it) }

    @Throws(JsonParseException::class, ClassCastException::class)
    inline fun <reified T> fromJson(string: String): T = fromJson(string.reader())

    inline fun <reified T> fromJsonOrNull(
        reader: Reader
    ): T? = try {
        fromJson<T>(reader)
    } catch (e: Throwable) {
        null
    }

    inline fun <reified T> fromJsonOrNull(
        string: String,
    ): T? = fromJsonOrNull(string.reader())

    @Throws(JsonIOException::class)
    inline fun <reified T : JsonElement> toJson(writer: Writer, element: T) = writer.use {
        GlobalGsonContext.gson.writeJson<T>(element, writer)
    }

    @Throws(JsonIOException::class)
    fun toJson(writer: Writer, src: Any) = writer.use { GlobalGsonContext.gson.writeJson(src, writer) }
}
