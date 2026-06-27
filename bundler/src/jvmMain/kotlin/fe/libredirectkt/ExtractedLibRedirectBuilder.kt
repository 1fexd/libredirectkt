package fe.libredirectkt

import fe.std.process.jvm.Standalone
import fe.std.process.launchProcess
import java.nio.file.Path
import kotlin.io.path.pathString

class ExtractedLibRedirectBuilder(
    private val bunPath: Path,
) {
    fun build(inputFilePath: Path, outDirPath: Path) {
        val buildExitCode = launchProcess(
            args = arrayOf(
                bunPath.pathString,
                "build",
                inputFilePath.pathString,
                "--outdir",
                outDirPath.pathString,
                "--minify"
            ),
            config = Standalone,
            printTo = System.out
        )
        if (buildExitCode != 0) {
            error("Build failed: $buildExitCode")
        }
    }
}
