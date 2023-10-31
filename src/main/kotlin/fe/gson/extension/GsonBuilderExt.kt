package fe.gson.extension

import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import kotlin.reflect.KClass

fun GsonBuilder.registerTypeAdapters(adapters: Map<out KClass<out Any>, TypeAdapter<out Any>>): GsonBuilder {
    adapters.forEach { (clazz, adapter) ->
        registerTypeAdapter(clazz.java, adapter)
    }

    return this
}
