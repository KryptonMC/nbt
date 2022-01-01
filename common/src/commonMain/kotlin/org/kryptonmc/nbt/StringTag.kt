/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

import okio.BufferedSink
import okio.BufferedSource
import okio.utf8Size
import org.kryptonmc.nbt.io.TagReader
import org.kryptonmc.nbt.io.TagWriter
import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic

public class StringTag private constructor(public val value: String) : Tag {

    override val id: Int = ID
    override val type: TagType = TYPE

    override fun write(output: BufferedSink) {
        WRITER.write(output, this)
    }

    override fun <T> examine(examiner: TagExaminer<T>): Unit = examiner.examineString(this)

    override fun copy(): StringTag = this

    override fun asString(): String = value

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is StringTag) return false
        return value == other.value
    }

    override fun hashCode(): Int = value.hashCode()

    public companion object {

        public const val ID: Int = 8
        @JvmField
        public val TYPE: TagType = TagType("TAG_String", true)
        @JvmField
        public val READER: TagReader<StringTag> = object : TagReader<StringTag> {

            override fun read(input: BufferedSource, depth: Int): StringTag {
                val length = input.readShort()
                return of(input.readUtf8(length.toLong()))
            }
        }
        @JvmField
        public val WRITER: TagWriter<StringTag> = object : TagWriter<StringTag> {

            override fun write(output: BufferedSink, value: StringTag) {
                output.writeShort(value.value.utf8Size().toInt())
                output.writeUtf8(value.value)
            }
        }
        private val EMPTY = StringTag("")

        /**
         * Creates a new string tag holding the given [value].
         *
         * @param value the value
         * @return a new string tag
         */
        @JvmStatic
        public fun of(value: String): StringTag {
            if (value.isEmpty()) return empty()
            return StringTag(value)
        }

        /**
         * Gets the string tag that holds the empty string.
         *
         * @return the empty string tag
         */
        @JvmStatic
        public fun empty(): StringTag = EMPTY
    }
}
