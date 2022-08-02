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
import java.io.DataOutput
import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic

/**
 * A tag that holds an integer value.
 *
 * The reason this is not directly constructable is that it is pooled and
 * cached. The cache contains the most common integer values used in NBT, those
 * being the values from -128 to 1024.
 */
public class IntTag private constructor(public val value: Int) : NumberTag {

    override val id: Int
        get() = ID
    override val type: TagType
        get() = TYPE

    override fun write(output: DataOutput) {
        WRITER.write(output, this)
    }

    override fun <T> examine(examiner: TagExaminer<T>) {
        examiner.examineInt(this)
    }

    override fun copy(): IntTag = this

    override fun equals(other: Any?): Boolean = this === other || (other is IntTag && value == other.value)

    override fun hashCode(): Int = value

    override fun toString(): String = "IntTag(value=$value)"

    override fun asNumber(): Number = value

    override fun toDouble(): Double = value.toDouble()

    override fun toFloat(): Float = value.toFloat()

    override fun toLong(): Long = value.toLong()

    override fun toInt(): Int = value

    override fun toShort(): Short = (value and 0xFFFF).toShort()

    override fun toByte(): Byte = (value and 0xFF).toByte()

    public companion object {

        private const val LOWER_CACHE_LIMIT = -128
        private const val UPPER_CACHE_LIMIT = 1024
        private val CACHE = Array(UPPER_CACHE_LIMIT - LOWER_CACHE_LIMIT + 1) { IntTag(LOWER_CACHE_LIMIT + it) }

        /**
         * The integer tag representing the constant zero.
         */
        @JvmField
        public val ZERO: IntTag = of(0)

        public const val ID: Int = 3
        @JvmField
        public val TYPE: TagType = TagType("TAG_Int", true)
        @JvmField
        public val READER: TagReader<IntTag> = TagReader { input, _ -> of(input.readInt()) }
        @JvmField
        public val WRITER: TagWriter<IntTag> = TagWriter { output, value -> output.writeInt(value.value) }

        /**
         * Gets the integer tag representing the given [value], if it is in the
         * range -128 to 1024, else creates a new integer tag representing the
         * given [value].
         *
         * @param value the backing value
         * @return an integer tag representing the value
         */
        @JvmStatic
        public fun of(value: Int): IntTag {
            if (value in LOWER_CACHE_LIMIT..UPPER_CACHE_LIMIT) return CACHE[value - LOWER_CACHE_LIMIT]
            return IntTag(value)
        }
    }
}