package fe.buildsrc

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import fe.buildsrc.Package.relocatePackages
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.provideDelegate

object Dependency {
    val bundledGroup = "com.gitlab.grrfe.bundled-dependencies"

    fun DependencyHandler.bundledDependency(dependencyNotation: String) {
        add("shadowImplementation", dependencyNotation)

        val (_, module, version) = dependencyNotation.split(":")
        add("shadow", "$bundledGroup:$module:${version}")
    }

    fun Project.createShadowJarTask(): TaskProvider<ShadowJar> {
        val shadowImplementation by configurations
        return tasks.named<ShadowJar>("shadowJar") {
            mergeServiceFiles()
            exclude("META-INF/**/*")


            project.relocatePackages(shadowImplementation).forEach { (from, to) ->
                relocate(from, to)
            }

            archiveClassifier.set("")
            configurations = listOf()
        }
    }
}
