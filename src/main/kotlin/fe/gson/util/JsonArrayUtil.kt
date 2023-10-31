package fe.gson.util

import com.google.gson.JsonArray
import fe.gson.dsl.JsonArrayDsl
import fe.gson.dsl.JsonObjectDslInit
import fe.gson.dsl.jsonArray
import fe.gson.dsl.jsonObject

fun jsonArrayItems(vararg items: Any?): JsonArray {
    return JsonArrayDsl().add(items.toMutableList())
}

fun jsonArrayItems( items: Iterable<Any?>): JsonArray {
    return JsonArrayDsl().add(items)
}

fun jsonArrayWithSingleJsonObject(init: JsonObjectDslInit): JsonArray {
    return jsonArray {
        +jsonObject(init = init)
    }
}
