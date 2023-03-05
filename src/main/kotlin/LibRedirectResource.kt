import java.io.InputStream
object LibRedirectResource
fun getBuiltInLibRedirectJson(name: String = "libredirect_config.json"): InputStream? = LibRedirectResource::class.java.getResourceAsStream(name)
