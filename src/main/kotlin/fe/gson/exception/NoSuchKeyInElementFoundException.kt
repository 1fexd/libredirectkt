package fe.gson.exception

import com.google.gson.JsonElement

class NoSuchKeyInElementFoundException(
    element: JsonElement,
    key: String
) : Exception("$element does not have a key $key")

fun noSuchKeyException(element: JsonElement, key: String): Nothing = throw NoSuchKeyInElementFoundException(element, key)
