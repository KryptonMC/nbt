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
import org.kryptonmc.nbt.util.floor
import java.io.DataInput
import java.io.DataOutput

@Suppress("UNCHECKED_CAST")
class DoubleTag private constructor(override val value: Double) : NumberTag(value) {

    override val id = ID
    override val type = TYPE
    override val reader = READER
    override val writer = WRITER as TagWriter<Tag>

    override fun <T> examine(examiner: TagExaminer<T>) = examiner.examineDouble(this)

    override fun copy() = this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return value == (other as DoubleTag).value
    }

    override fun hashCode(): Int {
        val temp = value.toBits()
        return (temp xor (temp ushr 32)).toInt()
    }

    override fun toLong() = value.floor().toLong()

    override fun toInt() = value.floor()

    override fun toShort() = (value.floor() and '\uFFFF'.code).toShort()

    override fun toByte() = (value.floor() and 255).toByte()

    companion object {

        val ZERO = DoubleTag(0.0)
        const val ID = 6
        val TYPE = TagType("TAG_Double", true)
        val READER = object : TagReader<DoubleTag> {

            override fun read(input: DataInput, depth: Int) = DoubleTag(input.readDouble())
        }
        val WRITER = object : TagWriter<DoubleTag> {

            override fun write(output: DataOutput, tag: DoubleTag) = output.writeDouble(tag.value)
        }

        fun of(value: Double) = if (value == 0.0) ZERO else DoubleTag(value)
    }
}
