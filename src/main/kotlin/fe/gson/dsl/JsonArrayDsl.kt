package fe.gson.dsl

import com.google.gson.JsonArray
import fe.gson.extension.toJsonElement

typealias JsonArrayDslInit = JsonArrayDsl.() -> Unit

inline fun jsonArray(jsonArray: JsonArray = JsonArray(), init: JsonArrayDslInit): JsonArray {
    JsonArrayDsl(jsonArray).apply(init)
    return jsonArray
}

fun jsonArray(vararg items: Any?): JsonArray {
    return JsonArrayDsl().add(items.toMutableList())
}

@JvmInline
value class NumberValue(val value: Number)

class JsonArrayDsl(val jsonArray: JsonArray = JsonArray()) {
    fun add(item: Any?): JsonArray {
        item.toJsonElement()?.let { jsonArray.add(it) }
        return jsonArray
    }

    fun add(items: Iterable<Any?>): JsonArray {
        items.forEach { add(it) }
        return jsonArray
    }

    operator fun Any?.unaryPlus() = add(this)

    operator fun NumberValue.unaryPlus() = add(this)

    fun n(number: Number) = NumberValue(number)
}
