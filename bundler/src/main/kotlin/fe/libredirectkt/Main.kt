package fe.libredirectkt

import app.cash.zipline.EngineApi
import app.cash.zipline.QuickJs
import app.cash.zipline.Zipline
import app.cash.zipline.ZiplineService
import kotlinx.coroutines.asCoroutineDispatcher
import java.io.File
import java.util.concurrent.Executors
import kotlin.time.measureTimedValue


@OptIn(EngineApi::class)
fun main() {
    val executor = Executors.newSingleThreadExecutor { Thread(it, "Zipline") }
    val zipline = Zipline.create(executor.asCoroutineDispatcher())

    val script = File("libredirect", "dist/index.js")
    val libredirect = zipline.quickJs.compile(script.readText(), "libredirect")

    File("lib/src/main/resources/libredirect.zipline").writeBytes(libredirect)
}
