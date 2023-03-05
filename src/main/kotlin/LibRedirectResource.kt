import java.io.InputStream
object LibRedirectResource {
    fun getBuiltInLibRedirectConfigJson(name: String = "libredirect_config.json"): InputStream? = this::class.java.getResourceAsStream(name)
    fun getBuiltInLibRedirectInstancesJson(name: String = "libredirect_instances.json"): InputStream? = this::class.java.getResourceAsStream(name)
}
