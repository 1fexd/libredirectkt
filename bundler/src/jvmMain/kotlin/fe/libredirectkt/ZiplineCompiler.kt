@file:OptIn(EngineApi::class)

package fe.libredirectkt

import app.cash.zipline.EngineApi
import app.cash.zipline.Zipline
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ZiplineCompiler : AutoCloseable {
    private val executor: ExecutorService = Executors.newSingleThreadExecutor { Thread(it, "Zipline") }
    private val zipline = Zipline.create(executor.asCoroutineDispatcher())

    fun compile(text: String, fileName: String): ByteArray {
        val libredirect = zipline.quickJs.compile(text, fileName)

        return libredirect
    }

    override fun close() {
        zipline.close()
    }
}
