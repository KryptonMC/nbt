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
 * https://github.com/SpongePowered/math/blob/34803829c6d136e07650176aef3e52f11ae921eb/build-logic/src/main/java/org/spongepowered/gradle/math/templates/Variant.java
 */
package org.kryptonmc.nbt.templates

import javax.inject.Inject
import net.kyori.mammoth.Configurable
import org.gradle.api.Action
import org.gradle.api.Named
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Nested
import org.gradle.kotlin.dsl.mapProperty

open class Variant @Inject constructor(private val name: String, objects: ObjectFactory) : Named {

    val dataFiles: ConfigurableFileCollection = objects.fileCollection()
        @InputFiles get
    val properties: MapProperty<String, Any> = objects.mapProperty()
        @Nested get

    fun properties(configureAction: Action<MapProperty<String, Any>>) {
        Configurable.configure(properties, configureAction)
    }

    @Input
    override fun getName(): String = name
}