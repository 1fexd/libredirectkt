package fe.buildsrc

import org.gradle.process.ExecOperations
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.Path
import java.nio.file.Paths

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
        if (Files.exists(foundPath)) {
            return foundPath
        }
        if (Files.exists(defaultLocation)) {
            return defaultLocation
        }
        return null
    }
}
