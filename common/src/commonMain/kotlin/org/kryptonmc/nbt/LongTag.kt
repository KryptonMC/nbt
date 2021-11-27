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

    override fun write(output: BufferedSink): Unit = WRITER.write(output, this)

    override fun <T> examine(examiner: TagExaminer<T>): Unit = examiner.examineLong(this)

    override fun copy(): LongTag = this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LongTag) return false
        return value == other.value
    }

    override fun hashCode(): Int = (value xor (value ushr 32)).toInt()

    public companion object {

        private val CACHE = Array(1153) { LongTag((-128 + it).toLong()) }

        /**
         * The long tag representing the constant zero.
         */
        @JvmField
        public val ZERO: LongTag = of(0)

        public const val ID: Int = 4
        @JvmField
        public val TYPE: TagType = TagType("TAG_Long", true)
        @JvmField
        public val READER: TagReader<LongTag> = object : TagReader<LongTag> {

            override fun read(input: BufferedSource, depth: Int) = of(input.readLong())
        }
        @JvmField
        public val WRITER: TagWriter<LongTag> = object : TagWriter<LongTag> {

            override fun write(output: BufferedSink, value: LongTag) {
                output.writeLong(value.value)
            }
        }

        /**
         * Gets the long tag representing the given [value], if it is in the
         * range -128 to 1024, else creates a new long tag representing the
         * given [value].
         *
         * @param value the backing value
         * @return a long tag representing the value
         */
        @JvmStatic
        public fun of(value: Long): LongTag = if (value in -128..1024) CACHE[value.toInt() + 128] else LongTag(value)
    }
}
