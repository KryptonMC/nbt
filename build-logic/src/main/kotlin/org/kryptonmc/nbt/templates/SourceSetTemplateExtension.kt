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
 * https://github.com/SpongePowered/math/blob/34803829c6d136e07650176aef3e52f11ae921eb/build-logic/src/main/java/org/spongepowered/gradle/math/templates/SourceSetTemplateExtension.java
 */
package org.kryptonmc.nbt.templates

import net.kyori.mammoth.Configurable
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer

interface SourceSetTemplateExtension {

    val templateSets: NamedDomainObjectContainer<TemplateSet>

    fun singleSet(properties: Action<TemplateSet>)

    fun templateSets(configurer: Action<NamedDomainObjectContainer<TemplateSet>>) {
        Configurable.configure(templateSets, configurer)
    }
}