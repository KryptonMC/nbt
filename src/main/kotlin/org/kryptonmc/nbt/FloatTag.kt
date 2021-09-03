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

public class FloatTag private constructor(override val value: Float) : NumberTag(value) {

    override val id: Int = ID
    override val type: TagType = TYPE

    override fun write(output: DataOutput): Unit = WRITER.write(output, this)

    override fun <T> examine(examiner: TagExaminer<T>): Unit = examiner.examineFloat(this)

    override fun copy(): FloatTag = this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return value == (other as FloatTag).value
    }

    override fun hashCode(): Int = value.toBits()

    override fun toInt(): Int = value.floor()

    override fun toShort(): Short = (value.floor() and 65535).toShort()

    override fun toByte(): Byte = (value.floor() and 255).toByte()

    public companion object {

        @JvmField
        public val ZERO: FloatTag = FloatTag(0F)

        public const val ID: Int = 5
        @JvmField
        public val TYPE: TagType = TagType("TAG_Float", true)
        @JvmField
        public val READER: TagReader<FloatTag> = object : TagReader<FloatTag> {

            override fun read(input: DataInput, depth: Int) = FloatTag(input.readFloat())
        }
        @JvmField
        public val WRITER: TagWriter<FloatTag> = object : TagWriter<FloatTag> {

            override fun write(output: DataOutput, tag: FloatTag) = output.writeFloat(tag.value)
        }

        @JvmStatic
        public fun of(value: Float): FloatTag = if (value == 0F) ZERO else FloatTag(value)
    }
}
