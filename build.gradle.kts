import com.gitlab.grrfe.gradlebuild.*
import com.gitlab.grrfe.gradlebuild.extension.isPlatform
import com.gitlab.grrfe.gradlebuild.extension.isTesting
import fe.build.dependencies.Grrfe

plugins {
    kotlin("jvm")
    id("com.gitlab.grrfe.library-build-plugin")
}

val baseGroup = "com.github.1fexd.libredirectkt"
val multiplatformProjects = arrayOf("bundler")

subprojects {
    val isJvm = !multiplatformProjects.any { name.startsWith(it) }
    logger.quiet("Init for $this, isJvm=$isJvm, isTesting=$isTesting, isPlatform=$isPlatform")

    if (!isPlatform && isJvm) {
        applyPlugin(Plugins.KotlinJvm)
    }

    applyPlugin(
        Plugins.MavenPublish,
        Plugins.GrrfeLibraryBuild,
    )

    group = baseGroup
    if (!isPlatform && isJvm) {
        kotlin {
            jvmToolchain(Version.JVM)
            if (!isTesting) {
                explicitApi()
            }
        }

        java {
            withJavadocJar()
            withSourcesJar()
        }
    }

    dependencies {
        configurations.findByName("implementation")?.let { implementation ->
            implementation(platform(Grrfe.std.bom))
        }
    }
}
