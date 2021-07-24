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
public class DoubleTag private constructor(override val value: Double) : NumberTag(value) {

    override val id: Int = ID
    override val type: TagType = TYPE

    override fun write(output: DataOutput): Unit = WRITER.write(output, this)

    override fun <T> examine(examiner: TagExaminer<T>): Unit = examiner.examineDouble(this)

    override fun copy(): DoubleTag = this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return value == (other as DoubleTag).value
    }

    override fun hashCode(): Int {
        val temp = value.toBits()
        return (temp xor (temp ushr 32)).toInt()
    }

    override fun toLong(): Long = value.floor().toLong()

    override fun toInt(): Int = value.floor()

    override fun toShort(): Short = (value.floor() and '\uFFFF'.code).toShort()

    override fun toByte(): Byte = (value.floor() and 255).toByte()

    public companion object {

        public val ZERO: DoubleTag = DoubleTag(0.0)

        public const val ID: Int = 6
        public val TYPE: TagType = TagType("TAG_Double", true)
        public val READER: TagReader<DoubleTag> = object : TagReader<DoubleTag> {

            override fun read(input: DataInput, depth: Int) = DoubleTag(input.readDouble())
        }
        public val WRITER: TagWriter<DoubleTag> = object : TagWriter<DoubleTag> {

            override fun write(output: DataOutput, tag: DoubleTag) = output.writeDouble(tag.value)
        }

        public fun of(value: Double): DoubleTag = if (value == 0.0) ZERO else DoubleTag(value)
    }
}
