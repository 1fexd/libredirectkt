package fe.libredirectkt

import java.net.URI

class UriKt(str: String) {
    private val uri = URI(str)

    val host: String?
        get() = uri.host
    val path: String?
        get() = uri.path

    val hasQuery: Boolean
        get() = uri.query != null

    val queryString: String
        get() = uri.query?.let { "?$it" } ?: ""

    val splitQuery: MutableMap<String, String> by lazy {
        uri.query?.split("&")?.mapNotNull {
            with(it.split("=")) {
                if (this.size == 2) {
                    this[0] to this[1]
                } else null
            }
        }?.toMap()?.toMutableMap() ?: mutableMapOf()
    }

    val fragment: String?
        get() = uri.fragment
}
