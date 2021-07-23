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

class ShortTag private constructor(override val value: Short) : NumberTag(value) {

    override val id = ID
    override val type = TYPE
    override val reader = READER
    override val writer = WRITER

    override fun <T> examine(examiner: TagExaminer<T>) = examiner.examineShort(this)

    override fun copy() = this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return value == (other as ShortTag).value
    }

    override fun hashCode() = value.toInt()

    companion object {

        private val CACHE = Array(1153) { ShortTag((-128 + it).toShort()) }

        const val ID = 2
        val TYPE = TagType("TAG_Short", true)
        val READER = object : TagReader<ShortTag> {

            override fun read(input: DataInput, depth: Int) = of(input.readShort())
        }
        val WRITER = object : TagWriter<ShortTag> {

            override fun write(output: DataOutput, tag: ShortTag) = output.writeShort(tag.value.toInt())
        }

        fun of(value: Short) = if (value in -128..1024) CACHE[value.toInt()] else ShortTag(value)
    }
}
