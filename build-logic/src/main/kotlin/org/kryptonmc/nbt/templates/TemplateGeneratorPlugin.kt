package org.kryptonmc.nbt.templates

import net.kyori.mammoth.ProjectPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskContainer
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType

class TemplateGeneratorPlugin : ProjectPlugin {

    override fun apply(project: Project, plugins: PluginContainer, extensions: ExtensionContainer, tasks: TaskContainer) {
        plugins.withType<JavaBasePlugin> {
            tasks.register("generateTemplates") { dependsOn(tasks.withType<GenerateTemplates>()) }
            extensions.getByType<SourceSetContainer>().all sourceSet@{
                val extension = this.extensions.create(
                    SourceSetTemplateExtension::class.java,
                    "templates",
                    SourceSetTemplateExtensionImpl::class.java,
                    project.objects
                )
                val inputDir = project.layout.projectDirectory.dir("src/$name/template")
                val outputDir = project.layout.buildDirectory.dir("generated/sources/$name/template")

                extension.templateSets.all templateSet@{
                    val templateSetOutput = outputDir.map { it.dir(name) }
                    val generateTask = tasks.register<GenerateTemplates>(this@sourceSet.getTaskName("generate", "${name}Templates")) {
                        group = "generation"
                        baseSet.set(this@templateSet)
                        sourceDirectory.set(inputDir.dir(this@templateSet.name))
                        this.outputDir.set(templateSetOutput)
                    }
                    this@sourceSet.java.srcDir(generateTask.map(DefaultTask::getOutputs))
                }
            }
        }
    }
}
