/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

import okio.BufferedSink

/**
 * The base supertype for all NBT tags.
 */
public interface Tag {

    /**
     * The ID of this tag.
     */
    public val id: Int

    /**
     * The type of this tag.
     */
    public val type: TagType

    /**
     * Writes this tag's contents to the given [output].
     *
     * @param output the output to write to
     */
    public fun write(output: BufferedSink)

    /**
     * Examines this tag's contents using the given [examiner].
     *
     * @param examiner the examiner
     */
    public fun <T> examine(examiner: TagExaminer<T>)

    /**
     * Converts this tag in to its SNBT form.
     */
    public fun asString(): String = StringTagExaminer().examine(this)

    /**
     * Makes a copy of this tag and returns the result.
     *
     * If this tag is immutable, this will return itself.
     */
    public fun copy(): Tag
}
