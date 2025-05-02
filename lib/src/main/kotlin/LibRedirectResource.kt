import java.io.InputStream

public object LibRedirectResource {
    public fun getBuiltInLibRedirectConfigJson(name: String = "libredirect_config.json"): InputStream? {
        return this::class.java.getResourceAsStream(name)
    }

    public fun getBuiltInLibRedirectInstancesJson(name: String = "libredirect_instances.json"): InputStream? {
        return this::class.java.getResourceAsStream(name)
    }

    public fun getLibRedirect(name: String = "libredirect.zipline"): InputStream {
        return this::class.java.getResourceAsStream(name)!!
    }
}
