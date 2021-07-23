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
import java.io.DataInput
import java.io.DataOutput
import java.io.InputStream
import java.io.OutputStream

@Suppress("UNCHECKED_CAST")
object EndTag : Tag {

    const val ID = 0
    @JvmField val TYPE = TagType("TAG_End", true)
    @JvmField val READER = object : TagReader<EndTag> {

        override fun read(input: DataInput, depth: Int) = EndTag

        override fun read(input: InputStream, depth: Int) = EndTag
    }
    @JvmField val WRITER = object : TagWriter<EndTag> {

        override fun write(output: DataOutput, tag: EndTag) = Unit

        override fun write(output: OutputStream, tag: EndTag) = Unit
    }

    override val id = ID
    override val type = TYPE
    override val reader = READER
    override val writer = WRITER as TagWriter<Tag>

    override fun <T> examine(examiner: TagExaminer<T>) = examiner.examineEnd(this)

    override fun copy() = this
}
