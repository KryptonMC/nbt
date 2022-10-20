/*
 * This file is part of Krypton NBT, and originates from Math, licensed under the MIT license (MIT).
 *
 * Copyright (c) SpongePowered <https://spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * For the original file that this file is derived from, see:
 * https://github.com/SpongePowered/math/blob/34803829c6d136e07650176aef3e52f11ae921eb/build-logic/src/main/java/org/spongepowered/gradle/math/templates/GenerateTemplates.java
 */
package org.kryptonmc.nbt.templates

import com.mitchellbosecke.pebble.PebbleEngine
import com.mitchellbosecke.pebble.loader.DelegatingLoader
import com.mitchellbosecke.pebble.loader.FileLoader
import com.mitchellbosecke.pebble.template.PebbleTemplate
import net.kyori.mammoth.Properties
import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.IOException
import java.io.StringWriter
import java.nio.file.FileVisitResult
import java.nio.file.FileVisitor
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes
import java.util.Locale

abstract class GenerateTemplates : DefaultTask() {

    abstract val baseSet: Property<TemplateSet>
        @Nested get
    abstract val includesDirectory: DirectoryProperty
        @InputDirectory @Optional get
    abstract val sourceDirectory: DirectoryProperty
        @InputDirectory get
    abstract val outputDir: DirectoryProperty
        @OutputDirectory get

    @TaskAction
    fun generate() {
        val sourceDirectory = sourceDirectory.get().asFile.toPath()
        val sourceLoader = FileLoader().apply { prefix = sourceDirectory.toAbsolutePath().toString() }
        val loader = if (includesDirectory.isPresent) {
            val includesLoader = FileLoader().apply { prefix = includesDirectory.get().asFile.absolutePath }
            DelegatingLoader(arrayOf(sourceLoader, includesLoader).asList())
        } else {
            sourceLoader
        }

        // By default, resolves FS paths
        // todo: restrict inputs to inputs and includes
        val engine = PebbleEngine.Builder()
            .autoEscaping(false) // no html escaping
            .defaultLocale(Locale.ROOT)
            .loader(loader)
            // .cacheActive(false) // xX: overlap between file names and template names causes issues
            .strictVariables(true) // make sure to fail when vars are not present
            .build()

        val outputDirectory = outputDir.get().asFile.toPath()
        Files.createDirectories(outputDirectory)
        val variants = baseSet.get().prepareDataForGeneration()
        val header = Properties.finalized(baseSet.get().header).orNull

        val seenOutputs = HashSet<String>()
        Files.walkFileTree(sourceDirectory, object : FileVisitor<Path> {

            override fun preVisitDirectory(dir: Path?, attrs: BasicFileAttributes?): FileVisitResult = FileVisitResult.CONTINUE

            override fun visitFileFailed(file: Path?, exc: IOException?): FileVisitResult = FileVisitResult.CONTINUE

            override fun postVisitDirectory(dir: Path?, exc: IOException?): FileVisitResult = FileVisitResult.CONTINUE

            override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                val relativePath = sourceDirectory.relativize(file).toString()
                val fileNameTemplate = engine.getLiteralTemplate(FILE_NAME_CACHE_DISAMBIGUATOR + relativePath)
                val template = engine.getTemplate(relativePath)

                variants.forEach { variant ->
                    var outputFile = evaluateToString(fileNameTemplate, variant).substring(FILE_NAME_CACHE_DISAMBIGUATOR.length)
                    if (outputFile.endsWith(PEBBLE_EXTENSION)) outputFile = outputFile.substring(0, outputFile.length - PEBBLE_EXTENSION.length)

                    if (!seenOutputs.add(outputFile)) {
                        throw InvalidUserDataException("Output file $outputFile (a variant of input $relativePath) has already been written in " +
                            "another variant!")
                    }

                    val output = outputDirectory.resolve(outputFile)
                    Files.createDirectories(output.parent)
                    Files.newBufferedWriter(output).use { writer ->
                        if (header != null) {
                            writer.write(header)
                            writer.write(System.lineSeparator())
                        }
                        template.evaluate(writer, variant)
                    }
                }
                return FileVisitResult.CONTINUE
            }
        })
    }

    private fun evaluateToString(template: PebbleTemplate, data: Map<String?, Any?>): String {
        val writer = StringWriter()
        template.evaluate(writer, data)
        return writer.toString()
    }

    companion object {

        private const val FILE_NAME_CACHE_DISAMBIGUATOR = "###"
        private const val PEBBLE_EXTENSION = ".peb"
    }
}