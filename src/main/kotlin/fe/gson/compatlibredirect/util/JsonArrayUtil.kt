package fe.gson.compatlibredirect.util

import com.google.gson.JsonArray
import fe.gson.compatlibredirect.dsl.JsonArrayDsl
import fe.gson.compatlibredirect.dsl.JsonObjectDslInit
import fe.gson.compatlibredirect.dsl.jsonArray
import fe.gson.compatlibredirect.dsl.jsonObject

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
