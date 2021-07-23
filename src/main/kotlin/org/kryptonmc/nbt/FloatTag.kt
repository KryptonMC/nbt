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

class FloatTag(override val value: Float) : NumberTag<FloatTag>(value) {

    override val id = 5
    override val type = TYPE
    override val reader = READER
    override val writer = WRITER

    override fun <T> examine(examiner: TagExaminer<T>) = examiner.examineFloat(this)

    override fun copy() = this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return value == (other as FloatTag).value
    }

    override fun hashCode() = value.toBits()

    override fun toInt() = value.floor()

    override fun toShort() = (value.floor() and '\uFFFF'.code).toShort()

    override fun toByte() = (value.floor() and 255).toByte()

    companion object {

        val ZERO = FloatTag(0F)
        val TYPE = TagType("TAG_Float", true)
        val READER = object : TagReader<FloatTag> {

            override fun read(input: DataInput) = FloatTag(input.readFloat())
        }
        val WRITER = object : TagWriter<FloatTag> {

            override fun write(output: DataOutput, tag: FloatTag) = output.writeFloat(tag.value)
        }
    }
}
