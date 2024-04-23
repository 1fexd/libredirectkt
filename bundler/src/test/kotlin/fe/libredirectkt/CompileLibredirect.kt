package fe.libredirectkt

import app.cash.zipline.EngineApi
import app.cash.zipline.Zipline
import kotlinx.coroutines.asCoroutineDispatcher
import java.io.File
import java.util.concurrent.Executors

@OptIn(EngineApi::class)
fun main() {
    val executor = Executors.newSingleThreadExecutor { Thread(it, "Zipline") }
    val zipline = Zipline.create(executor.asCoroutineDispatcher())

    val script = File("upstreamjs", "out/libredirect.js")
    val libredirect = zipline.quickJs.compile(script.readText(), "libredirect.js")

    File("upstreamjs", "libredirect.zipline").writeBytes(libredirect)
}
