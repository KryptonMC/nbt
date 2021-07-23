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
class LongTag private constructor(override val value: Long) : NumberTag(value) {

    override val id = ID
    override val type = TYPE
    override val reader = READER
    override val writer = WRITER as TagWriter<Tag>

    override fun <T> examine(examiner: TagExaminer<T>) = examiner.examineLong(this)

    override fun copy() = this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return value == (other as LongTag).value
    }

    override fun hashCode() = (value xor (value ushr 32)).toInt()

    companion object {

        private val CACHE = Array(1153) { LongTag((-128 + it).toLong()) }

        const val ID = 4
        val TYPE = TagType("TAG_Long", true)
        val READER = object : TagReader<LongTag> {

            override fun read(input: DataInput, depth: Int) = of(input.readLong())
        }
        val WRITER = object : TagWriter<LongTag> {

            override fun write(output: DataOutput, tag: LongTag) = output.writeLong(tag.value)
        }

        fun of(value: Long) = if (value in -128..1024) CACHE[value.toInt()] else LongTag(value)
    }
}
