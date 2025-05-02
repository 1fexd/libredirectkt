package fe.libredirectkt

import app.cash.zipline.EngineApi
import app.cash.zipline.Zipline
import kotlinx.coroutines.asCoroutineDispatcher
import java.nio.file.Path
import java.util.concurrent.Executors
import kotlin.io.path.*


@OptIn(EngineApi::class)
fun main(args: Array<String>) {
    if (args.size != 1) error("No root directory path given!")
    val rootDir = Path(args[0])

    val homeDir = Path(System.getProperty("user.home"))
    val bun = homeDir / ".bun" / "bin" / "bun"

    val libredirectDir = rootDir / "libredirect"
    val distDir = libredirectDir / "dist"
//    extractExtension(bun, libredirectDir, distDir)

    val executor = Executors.newSingleThreadExecutor { Thread(it, "Zipline") }
    val zipline = Zipline.create(executor.asCoroutineDispatcher())

    val libredirect = zipline.quickJs.compile((distDir / "index.js").readText(), "libredirect")
    (rootDir / "lib" / "src" / "main" / "resources" / "libredirect.zipline").writeBytes(libredirect)
}

fun extractExtension(bun: Path, dir: Path, distDir: Path) {
//    val runExitCode =
//        launchProcess(args = arrayOf(bun.pathString, "run", (dir / "api" / "main.ts").pathString), config = Standalone)
//    if (runExitCode != 0) {
//        error("Extraction failed: $runExitCode")
//    }
//
//    val buildExitCode = launchProcess(
//        args = arrayOf(
//            bun.pathString,
//            "build",
//            (dir / "api" / "src" / "index.ts").pathString,
//            "--outdir",
//            distDir.pathString,
//            "--minify"
//        ),
//        config = Standalone
//    )
//    if (buildExitCode != 0) {
//        error("Build failed: $buildExitCode")
//    }
}
