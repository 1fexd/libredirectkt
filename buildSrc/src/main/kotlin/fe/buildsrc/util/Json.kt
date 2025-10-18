package fe.buildsrc.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.net.http.HttpResponse

inline fun <reified W : JsonElement> asJson(): HttpResponse.BodySubscriber<W> {
    val upstream = HttpResponse.BodySubscribers.ofInputStream()
    return HttpResponse.BodySubscribers.mapping(upstream) {
        it.bufferedReader().use { stream -> JsonParser.parseReader(stream) as W }
    }
}

val gson: Gson = GsonBuilder().setPrettyPrinting().create()

