package fe.buildsrc

import org.gradle.process.ExecOperations
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.exists

class BunLocator(
    private val defaultLocation: Path = Paths.get(System.getProperty("user.home"), ".bun/bin/bun")
) {
    fun bunPath(execOperations: ExecOperations): Path? {
        val output = ByteArrayOutputStream()
        execOperations.exec {
            commandLine("bash", "-lc", "command -v bun 2>/dev/null || which bun 2>/dev/null")
            standardOutput = output
            isIgnoreExitValue = true
        }
        val foundPath = output.toString().trim().ifBlank { null }?.let { Paths.get(it) }
        if (foundPath?.exists() == true) {
            return foundPath
        }
        if (defaultLocation.exists()) {
            return defaultLocation
        }
        return null
    }
}
