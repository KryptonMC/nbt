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

class ByteTag private constructor(override val value: Byte) : NumberTag<ByteTag>(value) {

    override val id = 1
    override val type = TYPE
    override val reader = READER
    override val writer = WRITER

    override fun <T> examine(examiner: TagExaminer<T>) = examiner.examineByte(this)

    override fun copy() = this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return value == (other as ByteTag).value
    }

    override fun hashCode() = value.toInt()

    companion object {

        private val CACHE = Array(256) { ByteTag((it - 128).toByte()) }
        val ZERO = of(0)
        val ONE = of(1)

        val TYPE = TagType("TAG_Byte", true)
        val READER = object : TagReader<ByteTag> {

            override fun read(input: DataInput) = of(input.readByte())
        }
        val WRITER = object : TagWriter<ByteTag> {

            override fun write(output: DataOutput, tag: ByteTag) = output.writeByte(tag.value.toInt())
        }

        fun of(value: Byte) = CACHE[value.toInt()]
    }
}
