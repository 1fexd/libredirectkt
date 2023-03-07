package fe.libredirectkt

import com.google.gson.JsonElement
import fe.gson.extensions.*
import java.io.InputStream

data class LibRedirectService(
    val key: String,
    val name: String,
    val url: String,
    val frontends: List<LibRedirectFrontend>,
    val defaultFrontend: LibRedirectFrontend,
    val targets: List<Regex>
)

data class LibRedirectFrontend(
    val key: String,
    val name: String,
    val excludeTargets: List<String>,
    val url: String
)





