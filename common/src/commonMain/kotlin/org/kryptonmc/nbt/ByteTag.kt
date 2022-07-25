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
import org.kryptonmc.nbt.io.TagReader
import org.kryptonmc.nbt.io.TagWriter
import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic

/**
 * A tag that holds a byte value.
 *
 * The reason this is not directly constructable is that it is pooled and
 * cached. Because the minimum and maximum value of a byte is so low, we can
 * safely cache every possible byte tag value for less repetition.
 */
public class ByteTag private constructor(override val value: Byte) : NumberTag(value) {

    override val id: Int
        get() = ID
    override val type: TagType
        get() = TYPE

    override fun write(output: BufferedSink) {
        WRITER.write(output, this)
    }

    override fun <T> examine(examiner: TagExaminer<T>) {
        examiner.examineByte(this)
    }

    override fun copy(): ByteTag = this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ByteTag) return false
        return value == other.value
    }

    override fun hashCode(): Int = value.toInt()

    override fun toString(): String = "ByteTag(value=$value)"

    public companion object {

        private const val LOWER_CACHE_LIMIT = -128
        private const val CACHE_OFFSET = -LOWER_CACHE_LIMIT
        private val CACHE = Array(256) { ByteTag((LOWER_CACHE_LIMIT + it).toByte()) }

        /**
         * The byte tag representing the constant zero.
         */
        @JvmField
        public val ZERO: ByteTag = of(0)

        /**
         * The byte tag representing the constant one.
         */
        @JvmField
        public val ONE: ByteTag = of(1)

        public const val ID: Int = 1
        @JvmField
        public val TYPE: TagType = TagType("TAG_Byte", true)
        @JvmField
        public val READER: TagReader<ByteTag> = TagReader { input, _ -> of(input.readByte()) }
        @JvmField
        public val WRITER: TagWriter<ByteTag> = TagWriter { output, value -> output.writeByte(value.value.toInt()) }

        /**
         * Gets the byte tag representing the given [value].
         *
         * @param value the backing value
         * @return the byte tag representing the value
         */
        @JvmStatic
        public fun of(value: Byte): ByteTag = CACHE[value.toInt() + CACHE_OFFSET]

        /**
         * Gets the byte tag representing the given [value].
         * The boolean here will be converted to its byte representation, i.e.
         * [ONE] for a true value, and [ZERO] for a false one.
         *
         * @param value the value
         */
        @JvmStatic
        public fun of(value: Boolean): ByteTag = if (value) ONE else ZERO
    }
}
