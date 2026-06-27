package fe.buildsrc

import org.gradle.process.ExecOperations
import java.io.ByteArrayOutputStream

object BunLocator {
    fun bunPath(execOperations: ExecOperations): String? {
        val output = ByteArrayOutputStream()
        execOperations.exec {
            commandLine("bash", "-lc", "command -v bun 2>/dev/null || which bun 2>/dev/null")
            standardOutput = output
            isIgnoreExitValue = true
        }
        return output.toString().trim().ifBlank { null }
    }
}
