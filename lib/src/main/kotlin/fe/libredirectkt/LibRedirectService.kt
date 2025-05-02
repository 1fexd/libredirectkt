package fe.libredirectkt

 public data class LibRedirectService(
    val key: String,
    val name: String,
    val url: String,
    val frontends: List<LibRedirectFrontend>,
    val defaultFrontend: LibRedirectFrontend,
    val targets: List<Regex>
)

 public data class LibRedirectFrontend(
    val key: String,
    val name: String,
    val excludeTargets: List<String>,
    val url: String
)





