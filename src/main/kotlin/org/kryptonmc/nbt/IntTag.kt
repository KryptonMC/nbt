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
public class IntTag private constructor(override val value: Int) : NumberTag(value) {

    override val id: Int = ID
    override val type: TagType = TYPE
    override val reader: TagReader<IntTag> = READER
    override val writer: TagWriter<Tag> = WRITER as TagWriter<Tag>

    override fun <T> examine(examiner: TagExaminer<T>): Unit = examiner.examineInt(this)

    override fun copy(): IntTag = this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return value == (other as IntTag).value
    }

    override fun hashCode(): Int = value

    public companion object {

        private val CACHE = Array(1153) { IntTag(-128 + it) }

        public const val ID: Int = 3
        public val TYPE: TagType = TagType("TAG_Int", true)
        public val READER: TagReader<IntTag> = object : TagReader<IntTag> {

            override fun read(input: DataInput, depth: Int) = of(input.readInt())
        }
        public val WRITER: TagWriter<IntTag> = object : TagWriter<IntTag> {

            override fun write(output: DataOutput, tag: IntTag) = output.writeInt(tag.value)
        }

        public fun of(value: Int): IntTag = if (value in -128..1024) CACHE[value] else IntTag(value)
    }
}
