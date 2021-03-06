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
 * A tag that holds an integer value.
 *
 * The reason this is not directly constructable is that it is pooled and
 * cached. The cache contains the most common integer values used in NBT, those
 * being the values from -128 to 1024.
 */
public class IntTag private constructor(override val value: Int) : NumberTag(value) {

    override val id: Int = ID
    override val type: TagType = TYPE

    override fun write(output: BufferedSink) {
        WRITER.write(output, this)
    }

    override fun <T> examine(examiner: TagExaminer<T>): Unit = examiner.examineInt(this)

    override fun copy(): IntTag = this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is IntTag) return false
        return value == other.value
    }

    override fun hashCode(): Int = value

    override fun toString(): String = "IntTag(value=$value)"

    public companion object {

        private val CACHE = Array(1153) { IntTag(-128 + it) }

        /**
         * The integer tag representing the constant zero.
         */
        @JvmField
        public val ZERO: IntTag = of(0)

        public const val ID: Int = 3
        @JvmField
        public val TYPE: TagType = TagType("TAG_Int", true)
        @JvmField
        public val READER: TagReader<IntTag> = object : TagReader<IntTag> {

            override fun read(input: BufferedSource, depth: Int) = of(input.readInt())
        }
        @JvmField
        public val WRITER: TagWriter<IntTag> = object : TagWriter<IntTag> {

            override fun write(output: BufferedSink, value: IntTag) {
                output.writeInt(value.value)
            }
        }

        /**
         * Gets the integer tag representing the given [value], if it is in the
         * range -128 to 1024, else creates a new integer tag representing the
         * given [value].
         *
         * @param value the backing value
         * @return an integer tag representing the value
         */
        @JvmStatic
        public fun of(value: Int): IntTag = if (value in -128..1024) CACHE[value + 128] else IntTag(value)
    }
}
