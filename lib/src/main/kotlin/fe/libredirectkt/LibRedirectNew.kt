package fe.libredirectkt

import app.cash.zipline.EngineApi
import app.cash.zipline.Zipline
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import java.io.InputStream
import java.util.concurrent.Executors

object LibRedirectNew {
    fun create(dispatcher: CoroutineDispatcher, stream: InputStream): LibRedirectZipline {
        val zipline = Zipline.create(dispatcher)

        val bytes = stream.use { it.readBytes() }
        zipline.loadJsModule(bytes, "libredirect")

        return LibRedirectZipline(zipline)
    }
}

data class LibRedirectZipline(val zipline: Zipline) {
    @OptIn(EngineApi::class)
    fun redirect(url: String, frontend: String, randomInstance: String): String? {
        return zipline.quickJs.evaluate("require('libredirect').resolve('$url', '$frontend', '$randomInstance')") as? String?
    }

    fun close() {
        zipline.close()
    }
}
