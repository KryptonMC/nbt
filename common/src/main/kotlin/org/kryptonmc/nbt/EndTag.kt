/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

import org.kryptonmc.nbt.io.TagReader
import org.kryptonmc.nbt.io.TagWriter
import java.io.DataOutput

/**
 * The tag representing the end of a compound tag.
 */
public object EndTag : Tag {

    public const val ID: Int = 0
    @JvmField
    public val TYPE: TagType = TagType("TAG_End", true)
    @JvmField
    public val READER: TagReader<EndTag> = TagReader { _, _ -> EndTag }
    @JvmField
    public val WRITER: TagWriter<EndTag> = TagWriter { _, _ -> }

    override val id: Int
        get() = ID
    override val type: TagType
        get() = TYPE

    override fun write(output: DataOutput) {
        // Nothing to write for end tags
    }

    override fun <T> examine(examiner: TagExaminer<T>) {
        examiner.examineEnd(this)
    }

    override fun copy(): EndTag = this

    override fun toString(): String = "EndTag"
}
