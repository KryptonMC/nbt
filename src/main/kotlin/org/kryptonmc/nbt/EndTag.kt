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
public object EndTag : Tag {

    public const val ID: Int = 0
    @JvmField public val TYPE: TagType = TagType("TAG_End", true)
    @JvmField public val READER: TagReader<EndTag> = object : TagReader<EndTag> {

        override fun read(input: DataInput, depth: Int) = EndTag

        override fun read(input: InputStream, depth: Int) = EndTag
    }
    @JvmField public val WRITER: TagWriter<EndTag> = object : TagWriter<EndTag> {

        override fun write(output: DataOutput, tag: EndTag) = Unit

        override fun write(output: OutputStream, tag: EndTag) = Unit
    }

    override val id: Int = ID
    override val type: TagType = TYPE

    override fun write(output: DataOutput): Unit = Unit

    override fun <T> examine(examiner: TagExaminer<T>): Unit = examiner.examineEnd(this)

    override fun copy(): EndTag = this
}
