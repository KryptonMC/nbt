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
import org.kryptonmc.nbt.DoubleTag.Companion.ZERO
import org.kryptonmc.nbt.io.TagReader
import org.kryptonmc.nbt.io.TagWriter
import org.kryptonmc.nbt.util.floor
import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic

/**
 * A tag that holds a float.
 *
 * This is not directly constructable, as the [ZERO] constant is cached, and we
 * want to return that if the value is zero.
 */
public class FloatTag private constructor(override val value: Float) : NumberTag(value) {

    override val id: Int = ID
    override val type: TagType = TYPE

    override fun write(output: BufferedSink) {
        WRITER.write(output, this)
    }

    override fun <T> examine(examiner: TagExaminer<T>): Unit = examiner.examineFloat(this)

    override fun copy(): FloatTag = this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FloatTag) return false
        return value == other.value
    }

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = "FloatTag(value=$value)"

    override fun toInt(): Int = value.floor()

    override fun toShort(): Short = (value.floor() and 65535).toShort()

    override fun toByte(): Byte = (value.floor() and 255).toByte()

    public companion object {

        /**
         * The float tag representing the constant zero.
         */
        @JvmField
        public val ZERO: FloatTag = FloatTag(0F)

        public const val ID: Int = 5
        @JvmField
        public val TYPE: TagType = TagType("TAG_Float", true)
        @JvmField
        public val READER: TagReader<FloatTag> = object : TagReader<FloatTag> {

            override fun read(input: BufferedSource, depth: Int) = FloatTag(Float.fromBits(input.readInt()))
        }
        @JvmField
        public val WRITER: TagWriter<FloatTag> = object : TagWriter<FloatTag> {

            override fun write(output: BufferedSink, value: FloatTag) {
                output.writeInt(value.value.toBits())
            }
        }

        /**
         * Creates a new float tag holding the given [value].
         *
         * @param value the value
         * @return a new double tag
         */
        @JvmStatic
        public fun of(value: Float): FloatTag = if (value == 0F) ZERO else FloatTag(value)
    }
}
