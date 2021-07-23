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

class IntTag private constructor(override val value: Int) : NumberTag(value) {

    override val id = ID
    override val type = TYPE
    override val reader = READER
    override val writer = WRITER

    override fun <T> examine(examiner: TagExaminer<T>) = examiner.examineInt(this)

    override fun copy() = this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return value == (other as IntTag).value
    }

    override fun hashCode() = value

    companion object {

        private val CACHE = Array(1153) { IntTag(-128 + it) }

        const val ID = 3
        val TYPE = TagType("TAG_Int", true)
        val READER = object : TagReader<IntTag> {

            override fun read(input: DataInput, depth: Int) = of(input.readInt())
        }
        val WRITER = object : TagWriter<IntTag> {

            override fun write(output: DataOutput, tag: IntTag) = output.writeInt(tag.value)
        }

        fun of(value: Int) = if (value in -128..1024) CACHE[value] else IntTag(value)
    }
}
