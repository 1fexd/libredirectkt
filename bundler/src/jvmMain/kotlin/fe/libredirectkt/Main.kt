package fe.libredirectkt

import app.cash.zipline.EngineApi
import com.github.ajalt.mordant.rendering.AnsiLevel
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.terminal.Terminal
import fe.std.process.jvm.Standalone
import fe.std.process.launchProcess
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import okio.Path.Companion.toPath
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.div
import kotlin.io.path.*

class DirLayout2(
    val libResourcesDir: Path,
    val libRedirectDir: Path,
    val bundlerDir: Path
)

fun DirLayout(rootDir: Path): DirLayout2 {
    return DirLayout2(
        libResourcesDir = rootDir / "lib" / "src" / "main" / "resources",
        libRedirectDir = rootDir / "libredirect",
        bundlerDir = rootDir / "bundler"
    )
}

@OptIn(EngineApi::class)
suspend fun main(args: Array<String>) {
    val t = Terminal(ansiLevel = AnsiLevel.TRUECOLOR, interactive = true)
    if (args.size != 1) error("No root directory path given!")
    val rootDir = Path(args[0])
    val layout = DirLayout(rootDir)

//    t.println(TextColors.green("Fetching config and instances"))
//    val fetcher = InstanceFetcher(HttpClient(CIO))
//    val configText = fetcher.fetchConfig()
//    val instancesText = fetcher.fetchInstances()
//    (layout.libResourcesDir / "libredirect_config.json").writeText(configText)
//    (layout.libResourcesDir / "libredirect_instances.json").writeText(instancesText)

    val bunPath = findTool("bun")!!

    t.println(TextColors.green("Extracting LibRedirect code from browser extension"))
    val bundlerJs =
        layout.bundlerDir / "build" / "compileSync" / "js" / "main" / "productionExecutable" / "kotlin" / "libredirect-bundler.js"
    val extractor = LibRedirectExtractor(bunPath, bundlerJs, rootDir / "gradlew")
    extractor.extractExtension(layout.libRedirectDir)

    t.println(TextColors.green("Building extracted LibRedirect code + stubs"))
    val builder = ExtractedLibRedirectBuilder(bunPath)
    val extractedLibRedirectIndexTs = (layout.libRedirectDir / "api" / "src" / "index.ts")
    val extractedLibRedirectDist = layout.libRedirectDir / "dist"
    builder.build(extractedLibRedirectIndexTs, extractedLibRedirectDist)

    t.println(TextColors.green("Compiling Zipline"))
    val compiler = ZiplineCompiler()
    val indexJsPath = layout.libRedirectDir / "dist" / "index.js"
    val indexJsText = indexJsPath.readText()
    val libRedirectZiplinePath = layout.libResourcesDir / "libredirect.zipline"
    val libredirect = compiler.use { it.compile(indexJsText, "libredirect") }
    libRedirectZiplinePath.writeBytes(libredirect)
    t.println(TextColors.green("Wrote output to $libRedirectZiplinePath"))
}

fun findTool(name: String): Path? {
    var path: String? = null
    launchProcess("which", name, config = Standalone) {
        if (!it.contains("not found")) {
            path = it
        }
    }
    return path?.toPath()?.toNioPath()
}
