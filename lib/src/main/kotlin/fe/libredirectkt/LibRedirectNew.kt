package fe.libredirectkt

import app.cash.zipline.EngineApi
import app.cash.zipline.Zipline
import kotlinx.coroutines.CoroutineDispatcher
import java.io.InputStream

public object LibRedirectNew {
    public fun create(dispatcher: CoroutineDispatcher, bytes: ByteArray): LibRedirectZipline {
        val zipline = Zipline.create(dispatcher)

        zipline.loadJsModule(bytes, "libredirect")
        return LibRedirectZipline(zipline)
    }

    public fun create(dispatcher: CoroutineDispatcher, stream: InputStream): LibRedirectZipline {
        return stream.use { create(dispatcher, it.readBytes()) }
    }
}

public data class LibRedirectZipline(val zipline: Zipline) : AutoCloseable {
    @OptIn(EngineApi::class)
    public fun redirect(url: String, frontend: String, randomInstance: String): String? {
        return zipline.quickJs.evaluate("require('libredirect').resolve('$url', '$frontend', '$randomInstance')") as? String?
    }

    override fun close() {
        zipline.close()
    }
}
