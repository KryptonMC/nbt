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
 * A tag that holds a short value.
 *
 * The reason this is not directly constructable is that it is pooled and
 * cached. The cache contains the most common short values used in NBT, those
 * being the values from -128 to 1024.
 */
public class ShortTag private constructor(override val value: Short) : NumberTag(value) {

    override val id: Int = ID
    override val type: TagType = TYPE

    override fun write(output: BufferedSink) {
        WRITER.write(output, this)
    }

    override fun <T> examine(examiner: TagExaminer<T>): Unit = examiner.examineShort(this)

    override fun copy(): ShortTag = this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ShortTag) return false
        return value == other.value
    }

    override fun hashCode(): Int = value.toInt()

    public companion object {

        private val CACHE = Array(1153) { ShortTag((-128 + it).toShort()) }

        /**
         * The short tag representing the constant zero.
         */
        @JvmField
        public val ZERO: ShortTag = of(0)

        public const val ID: Int = 2
        @JvmField
        public val TYPE: TagType = TagType("TAG_Short", true)
        @JvmField
        public val READER: TagReader<ShortTag> = object : TagReader<ShortTag> {

            override fun read(input: BufferedSource, depth: Int) = of(input.readShort())
        }
        @JvmField
        public val WRITER: TagWriter<ShortTag> = object : TagWriter<ShortTag> {

            override fun write(output: BufferedSink, value: ShortTag) {
                output.writeShort(value.value.toInt())
            }
        }

        /**
         * Gets the short tag representing the given [value], if it is in the
         * range -128 to 1024, else creates a new short tag representing the
         * given [value].
         *
         * @param value the backing value
         * @return a short tag representing the value
         */
        @JvmStatic
        public fun of(value: Short): ShortTag = if (value in -128..1024) CACHE[value.toInt() + 128] else ShortTag(value)
    }
}
