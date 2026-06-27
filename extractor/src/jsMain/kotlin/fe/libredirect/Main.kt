package fe.libredirect

import kotlinx.serialization.json.Json
import node.process.Process
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

fun process(): Process {
    return js("require('process')").unsafeCast<Process>()
}

fun main(args: Array<String>) {
    val fileSystem = NodeJsFileSystem
//    val currentDir = process().cwd()
//    val baseDir = process().argv.getOrNull(2)?.toPath()
//    val baseDir = args.getOrNull(2)?.toPath()
    val baseDir = args.firstOrNull()?.toPath()
    if (baseDir == null) {
        println("First argument must be path to libredirect dir!")
        return
    }
//    fileSystem.exists()

    // {project-root}/build/js/packages/libredirect-extractor
//    println("Current directory: $currentDir")

    // {project-root}/libredirect
//    val baseDir = currentDir.toPath().parent?.parent?.parent?.parent?.resolve("libredirect")
    val inputFile = "$baseDir/browser_extension/src/assets/javascripts/services.js"
    val outputDir = "$baseDir/api/src/generated".toPath()
    fileSystem.createDirectories(outputDir)
    println("Base directory: $baseDir")
    println("Input file: $inputFile")
    println("Output directory: $outputDir")

    run(fileSystem, inputFile, outputDir)
}
