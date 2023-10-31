package fe.gson.compatlibredirect

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import fe.gson.compatlibredirect.extension.registerTypeAdapters
import fe.gson.compatlibredirect.typeadapter.dateTimeAdapters


typealias ConfigureGson = GsonBuilder.() -> Unit

object GlobalGsonContext {
    private val defaultGsonConfiguration: ConfigureGson = {
        registerTypeAdapters(dateTimeAdapters)
    }

    private var _gson: Gson? = null
    val gson: Gson
        get() {
            if (_gson == null) configure()
            return _gson!!
        }

    fun configure(configure: ConfigureGson = defaultGsonConfiguration): Gson {
        if (_gson == null) {
            return GsonBuilder().setPrettyPrinting().disableHtmlEscaping().apply(configure).create()
                .apply { _gson = this }
        }

        return gson
    }
}
