package fe.libredirectkt

import java.net.URI

public class UriKt(str: String) {
    private val uri = URI(str)

    public val host: String?
        get() = uri.host
    public val path: String?
        get() = uri.path

    public val hasQuery: Boolean
        get() = uri.query != null

    public val queryString: String
        get() = uri.query?.let { "?$it" } ?: ""

    public val splitQuery: MutableMap<String, String> by lazy {
        uri.query?.split("&")?.mapNotNull {
            with(it.split("=")) {
                if (this.size == 2) {
                    this[0] to this[1]
                } else null
            }
        }?.toMap()?.toMutableMap() ?: mutableMapOf()
    }

    public val fragment: String?
        get() = uri.fragment
}
