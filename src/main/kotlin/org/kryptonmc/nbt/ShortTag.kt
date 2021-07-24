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

@Suppress("UNCHECKED_CAST")
public class ShortTag private constructor(override val value: Short) : NumberTag(value) {

    override val id: Int = ID
    override val type: TagType = TYPE
    override val reader: TagReader<ShortTag> = READER
    override val writer: TagWriter<Tag> = WRITER as TagWriter<Tag>

    override fun <T> examine(examiner: TagExaminer<T>): Unit = examiner.examineShort(this)

    override fun copy(): ShortTag = this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return value == (other as ShortTag).value
    }

    override fun hashCode(): Int = value.toInt()

    public companion object {

        private val CACHE = Array(1153) { ShortTag((-128 + it).toShort()) }

        public const val ID: Int = 2
        public val TYPE: TagType = TagType("TAG_Short", true)
        public val READER: TagReader<ShortTag> = object : TagReader<ShortTag> {

            override fun read(input: DataInput, depth: Int) = of(input.readShort())
        }
        public val WRITER: TagWriter<ShortTag> = object : TagWriter<ShortTag> {

            override fun write(output: DataOutput, tag: ShortTag) = output.writeShort(tag.value.toInt())
        }

        public fun of(value: Short): ShortTag = if (value in -128..1024) CACHE[value.toInt() + 128] else ShortTag(value)
    }
}
