/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

import org.kryptonmc.nbt.io.TagCompression
import org.kryptonmc.nbt.io.TagReader
import org.kryptonmc.nbt.io.TagWriter
import java.io.DataInput
import java.io.DataOutput
import java.io.InputStream
import java.io.OutputStream

object EndTag : Tag {

    override val id = 0
    override val type = TagType("TAG_End", true)
    override val reader = object : TagReader<EndTag> {

        override fun read(input: DataInput, depth: Int) = EndTag

        override fun read(input: InputStream, depth: Int) = EndTag

        override fun read(input: InputStream, depth: Int, compression: TagCompression) = EndTag
    }
    override val writer = object : TagWriter<EndTag> {

        override fun write(output: DataOutput, tag: EndTag) = Unit

        override fun write(output: OutputStream, tag: EndTag) = Unit

        override fun write(output: OutputStream, tag: EndTag, compression: TagCompression) = Unit
    }

    override fun <T> examine(examiner: TagExaminer<T>) = examiner.examineEnd(this)

    override fun copy() = this
}
