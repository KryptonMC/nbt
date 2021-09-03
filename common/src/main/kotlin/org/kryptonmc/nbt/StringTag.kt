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

public class StringTag private constructor(public val value: String) : Tag {

    override val id: Int = ID
    override val type: TagType = TYPE

    override fun write(output: DataOutput): Unit = WRITER.write(output, this)

    override fun <T> examine(examiner: TagExaminer<T>): Unit = examiner.examineString(this)

    override fun copy(): StringTag = this

    override fun asString(): String = value

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return value == (other as StringTag).value
    }

    override fun hashCode(): Int = value.hashCode()

    public companion object {

        @JvmField
        public val EMPTY: StringTag = StringTag("")

        public const val ID: Int = 8
        @JvmField
        public val TYPE: TagType = TagType("TAG_String", true)
        @JvmField
        public val READER: TagReader<StringTag> = object : TagReader<StringTag> {

            override fun read(input: DataInput, depth: Int) = of(input.readUTF())
        }
        @JvmField
        public val WRITER: TagWriter<StringTag> = object : TagWriter<StringTag> {

            override fun write(output: DataOutput, tag: StringTag) = output.writeUTF(tag.value)
        }

        @JvmStatic
        public fun of(value: String): StringTag = if (value.isEmpty()) EMPTY else StringTag(value)
    }
}
