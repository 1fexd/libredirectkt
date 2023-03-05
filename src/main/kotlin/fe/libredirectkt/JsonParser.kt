package fe.libredirectkt

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import fe.gson.extensions.parseReaderAs
import fe.gson.extensions.parseStringAs
import java.io.InputStream

fun loadLibRedirectJson(inputStream: InputStream) = inputStream.use { parseReaderAs<JsonElement>(it.reader()) }
fun loadLibRedirectJson(text: String) = parseStringAs<JsonElement>(text)
