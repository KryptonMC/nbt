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
public class LongTag private constructor(override val value: Long) : NumberTag(value) {

    override val id: Int = ID
    override val type: TagType = TYPE

    override fun write(output: DataOutput): Unit = WRITER.write(output, this)

    override fun <T> examine(examiner: TagExaminer<T>): Unit = examiner.examineLong(this)

    override fun copy(): LongTag = this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return value == (other as LongTag).value
    }

    override fun hashCode(): Int = (value xor (value ushr 32)).toInt()

    public companion object {

        private val CACHE = Array(1153) { LongTag((-128 + it).toLong()) }

        public const val ID: Int = 4
        public val TYPE: TagType = TagType("TAG_Long", true)
        public val READER: TagReader<LongTag> = object : TagReader<LongTag> {

            override fun read(input: DataInput, depth: Int) = of(input.readLong())
        }
        public val WRITER: TagWriter<LongTag> = object : TagWriter<LongTag> {

            override fun write(output: DataOutput, tag: LongTag) = output.writeLong(tag.value)
        }

        public fun of(value: Long): LongTag = if (value in -128..1024) CACHE[value.toInt() + 128] else LongTag(value)
    }
}
