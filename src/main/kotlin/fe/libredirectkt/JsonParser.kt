package fe.libredirectkt

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.io.InputStream

fun loadLibRedirectJson(inputStream: InputStream) = loadLibRedirectJson(inputStream.use { JsonParser.parseReader(it.reader()) })
fun loadLibRedirectJson(text: String) = loadLibRedirectJson(JsonParser.parseString(text))

private fun loadLibRedirectJson(element: JsonElement) = element.asJsonObject
