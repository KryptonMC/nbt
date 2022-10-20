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
 * https://github.com/SpongePowered/math/blob/34803829c6d136e07650176aef3e52f11ae921eb/build-logic/src/main/java/org/spongepowered/gradle/math/templates/TemplateSet.java
 */
package org.kryptonmc.nbt.templates

import javax.inject.Inject
import net.kyori.mammoth.Configurable
import org.gradle.api.Action
import org.gradle.api.GradleException
import org.gradle.api.InvalidUserDataException
import org.gradle.api.Named
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectSet
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.Optional
import org.gradle.kotlin.dsl.mapProperty
import org.gradle.kotlin.dsl.property
import org.snakeyaml.engine.v2.api.Load
import org.snakeyaml.engine.v2.api.LoadSettings
import org.snakeyaml.engine.v2.exceptions.YamlEngineException
import java.io.IOException
import java.nio.file.Files

abstract class TemplateSet @Inject constructor(private val name: String, objects: ObjectFactory) : Named {

    val dataFiles: ConfigurableFileCollection = objects.fileCollection()
        @InputFiles get
    val properties: MapProperty<String, Any> = objects.mapProperty()
        @Nested get
    val header: Property<String> = objects.property()
        @Input @Optional get
    val variants: NamedDomainObjectContainer<Variant> = objects.domainObjectContainer(Variant::class.java)
        @Nested get

    fun variants(vararg variants: String) {
        variants.forEach(this.variants::register)
    }

    fun variants(configureAction: Action<NamedDomainObjectSet<Variant>>) {
        Configurable.configure(variants, configureAction)
    }

    @Input
    override fun getName(): String = name

    internal fun prepareDataForGeneration(): Set<Map<String?, Any?>> {
        val configData = loadConfig(dataFiles, !variants.isEmpty())
        if (variants.isEmpty()) {
            // non-variant mode
            val result = configData.get(null) ?: return setOf(properties.get())
            result.putAll(properties.get())
            return setOf(result)
        }

        // figure out any global data
        var global = configData.remove(null)
        if (global == null) {
            global = properties.getOrElse(emptyMap())
        } else {
            global.putAll(properties.getOrElse(emptyMap()))
        }

        val output = HashSet<Map<String?, Any?>>()
        // then get the per-variant bits
        variants.forEach { variant ->
            // global, from file
            val variantData = LinkedHashMap(global)
            // variant, from global files
            configData.remove(variant.name)?.let(variantData::putAll)
            // variant, from variant files
            loadConfig(variant.dataFiles, false).get(null)?.let(variantData::putAll)
            // variant, in-memory
            variantData.putAll(variant.properties.getOrElse(emptyMap()))
            output.add(variantData)
        }
        if (configData.isNotEmpty()) {
            throw InvalidUserDataException("Unknown variants declared in file for template set ${getName()}: ${configData.keys}")
        }
        return output
    }

    private fun loadConfig(files: ConfigurableFileCollection, useVariants: Boolean): MutableMap<String?, MutableMap<String?, Any?>> {
        val settings = LoadSettings.builder().setLabel("Template set ${getName()}").build()
        val load = Load(settings)

        val templateParameters = HashMap<String?, MutableMap<String?, Any?>>()
        files.asFileTree.forEach { file ->
            if (!file.isFile) return@forEach
            try {
                Files.newBufferedReader(file.toPath()).use { unmarshalData(templateParameters, load.loadFromReader(it), useVariants) }
            } catch (exception: YamlEngineException) {
                throw InvalidUserDataException("Invalid input in $file!", exception)
            } catch (exception: IOException) {
                throw GradleException("Failed to load data from $file!", exception)
            }
        }
        return templateParameters
    }

    private fun unmarshalData(output: MutableMap<String?, MutableMap<String?, Any?>>, data: Any, useVariants: Boolean) {
        if (data !is MutableMap<*, *>) throw InvalidUserDataException("Template data files must have a mapping as the root node!")
        if (useVariants) {
            val variants = data.remove("variants")
            if (variants != null) {
                if (variants !is MutableMap<*, *>) {
                    throw InvalidUserDataException("Value of 'variants' entry must be a mapping of String to Map<String, Object>!")
                }
                variants.forEach { (key, value) ->
                    if (value !is MutableMap<*, *>) {
                        throw InvalidUserDataException("Variant '$key' was expected to have a mapping value, but it was a ${value!!.javaClass}!")
                    }
                    output.put(java.lang.String.valueOf(key), makeStringKeys(value))
                }
            }
        }
        output.put(null, makeStringKeys(data))
    }

    companion object {

        @JvmStatic
        private fun makeStringKeys(map: Map<*, *>): MutableMap<String?, Any?> {
            val result = LinkedHashMap<String?, Any?>()
            map.forEach { (key, value) -> result.put(key?.toString(), value) }
            return result
        }
    }
}