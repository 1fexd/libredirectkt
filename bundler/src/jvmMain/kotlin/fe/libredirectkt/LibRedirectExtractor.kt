package fe.libredirectkt

import fe.std.process.jvm.Standalone
import fe.std.process.launchProcess
import java.nio.file.Path
import kotlin.io.path.pathString

class LibRedirectExtractor(
    private val bunPath: Path,
    private val bundlerJs: Path,
    private val gradlewPath: Path,
) {

    fun extractExtension(libRedirectDir: Path) {
        val runExitCode = launchProcess(
            args = arrayOf(bunPath.pathString, bundlerJs.pathString, libRedirectDir.pathString),
            config = Standalone,
            printTo = System.out
        )
//         launchProcess(

//            args = arrayOf(
//                "$gradlewPath",
//                "jsNodeProductionRun",
//                "--args",
//                """"${libRedirectDir.pathString}""""
//            ),
//            config = Standalone,
//            printTo = System.out
//        )
        if (runExitCode != 0) {
            error("Extraction failed: $runExitCode")
        }
    }
}
