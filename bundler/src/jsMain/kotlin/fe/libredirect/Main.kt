package fe.libredirect

import kotlinx.serialization.json.Json
import node.process.process
import okio.FileSystem
import okio.NodeJsFileSystem
import okio.Path.Companion.toPath
import okio.buffer
import okio.use
import typescript.SyntaxKind

fun run(fileSystem: FileSystem, inputFile: String, outputDir: okio.Path) {
    val sourceFile = toSourceFile(inputFile) ?: error("Services file does not exist at '${inputFile}'")
    val imports = listOf(
        createImport("../shim/XMLHttpRequest.shim.ts", "XMLHttpRequest")
    )

    val nodes = extractNodes(
        sourceFile, mapOf(
            "rewrite" to SyntaxKind.FunctionDeclaration,
            "defaultInstances" to SyntaxKind.VariableStatement
        )
    )

    println("imports=$imports")
    println("nodes=$nodes")

    val servicesJsPath = outputDir.resolve("services.js")
    fileSystem.sink(servicesJsPath).buffer().use {
        printNodes(sourceFile, (imports + nodes).toTypedArray()) { line ->
            it.writeUtf8(line + "\n")
        }
    }

    val defaultInstances = extractDefaultInstances(getDefaultInstancesNode(nodes.toTypedArray()))

    val defaultInstancesFile = outputDir.resolve("defaultInstances.json")
    val json = Json.Default
    val encoded = json.encodeToString(defaultInstances)

    fileSystem.sink(defaultInstancesFile, false).buffer().use {
        it.writeUtf8(encoded)
    }
}

fun main() {
    val currentDir = process.cwd()
    // {project-root}/build/js/packages/libredirect-bundler
    println("Current directory: $currentDir")

    // {project-root}/libredirect
    val baseDir = currentDir.toPath().parent?.parent?.parent?.parent?.resolve("libredirect")
    println("Base directory: $baseDir")

    val fileSystem = NodeJsFileSystem
    run(fileSystem, "$baseDir/browser_extension/src/assets/javascripts/services.js", "$baseDir/api/src/generated".toPath())
}
