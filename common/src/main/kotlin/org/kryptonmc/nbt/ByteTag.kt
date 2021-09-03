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

public class ByteTag private constructor(override val value: Byte) : NumberTag(value) {

    override val id: Int = ID
    override val type: TagType = TYPE

    override fun write(output: DataOutput): Unit = WRITER.write(output, this)

    override fun <T> examine(examiner: TagExaminer<T>): Unit = examiner.examineByte(this)

    override fun copy(): ByteTag = this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return value == (other as ByteTag).value
    }

    override fun hashCode(): Int = value.toInt()

    public companion object {

        private val CACHE = Array(256) { ByteTag((it - 128).toByte()) }
        @JvmField
        public val ZERO: ByteTag = of(0)
        @JvmField
        public val ONE: ByteTag = of(1)

        public const val ID: Int = 1
        @JvmField
        public val TYPE: TagType = TagType("TAG_Byte", true)
        @JvmField
        public val READER: TagReader<ByteTag> = object : TagReader<ByteTag> {

            override fun read(input: DataInput, depth: Int) = of(input.readByte())
        }
        @JvmField
        public val WRITER: TagWriter<ByteTag> = object : TagWriter<ByteTag> {

            override fun write(output: DataOutput, tag: ByteTag) = output.writeByte(tag.value.toInt())
        }

        @JvmStatic
        public fun of(value: Byte): ByteTag = CACHE[value.toInt() + 128]

        @JvmStatic
        public fun of(value: Boolean): ByteTag = if (value) ONE else ZERO
    }
}
