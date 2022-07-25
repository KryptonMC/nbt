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
 * A tag that holds a long value.
 *
 * The reason this is not directly constructable is that it is pooled and
 * cached. The cache contains the most common long values used in NBT, those
 * being the values from -128 to 1024.
 */
public class LongTag private constructor(override val value: Long) : NumberTag(value) {

    override val id: Int = ID
    override val type: TagType = TYPE

    override fun write(output: BufferedSink) {
        WRITER.write(output, this)
    }

    override fun <T> examine(examiner: TagExaminer<T>) {
        examiner.examineLong(this)
    }

    override fun copy(): LongTag = this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LongTag) return false
        return value == other.value
    }

    override fun hashCode(): Int = (value xor (value ushr 32)).toInt()

    public companion object {

        private const val LOWER_CACHE_LIMIT = -128
        private const val UPPER_CACHE_LIMIT = 1024
        private val CACHE = Array(UPPER_CACHE_LIMIT - LOWER_CACHE_LIMIT + 1) { LongTag((LOWER_CACHE_LIMIT + it).toLong()) }

        /**
         * The long tag representing the constant zero.
         */
        @JvmField
        public val ZERO: LongTag = of(0)

        public const val ID: Int = 4
        @JvmField
        public val TYPE: TagType = TagType("TAG_Long", true)
        @JvmField
        public val READER: TagReader<LongTag> = TagReader { input, _ -> of(input.readLong()) }
        @JvmField
        public val WRITER: TagWriter<LongTag> = TagWriter { output, value -> output.writeLong(value.value) }

        /**
         * Gets the long tag representing the given [value], if it is in the
         * range -128 to 1024, else creates a new long tag representing the
         * given [value].
         *
         * @param value the backing value
         * @return a long tag representing the value
         */
        @JvmStatic
        public fun of(value: Long): LongTag {
            if (value in LOWER_CACHE_LIMIT..UPPER_CACHE_LIMIT) return CACHE[value.toInt() - LOWER_CACHE_LIMIT]
            return LongTag(value)
        }
    }
}
