package fe.libredirectkt

import app.cash.zipline.EngineApi
import app.cash.zipline.Zipline
import fe.process.launchProcess
import kotlinx.coroutines.asCoroutineDispatcher
import java.nio.file.Path
import java.util.concurrent.Executors
import kotlin.io.path.*


@OptIn(EngineApi::class)
fun main() {
    val homeDir = Path(System.getProperty("user.home"))
    val bun = homeDir / ".bun" / "bin" / "bun"

    val libredirectDir = Path("libredirect")
    val distDir = libredirectDir / "dist"
    extractExtension(bun, libredirectDir, distDir)

    val executor = Executors.newSingleThreadExecutor { Thread(it, "Zipline") }
    val zipline = Zipline.create(executor.asCoroutineDispatcher())

    val script = distDir / "index.js"
    val libredirect = zipline.quickJs.compile(script.readText(), "libredirect")

    val outputFile = Path("lib", "src", "main", "resources", "libredirect.zipline")
    outputFile.writeBytes(libredirect)
}

fun extractExtension(bun: Path, dir: Path, distDir: Path) {
    val runExitCode = launchProcess(bun.pathString, "run", (dir / "api" / "main.ts").pathString)
    if (runExitCode != 0) {
        error("Extraction failed: $runExitCode")
    }

    val buildExitCode = launchProcess(bun.pathString, "build", (dir / "api" / "src" / "index.ts").pathString, "--outdir", distDir.pathString, "--minify")
    if (buildExitCode != 0) {
        error("Build failed: $buildExitCode")
    }
}
