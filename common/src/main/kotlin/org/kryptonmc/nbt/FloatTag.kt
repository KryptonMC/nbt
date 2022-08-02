/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

import org.kryptonmc.nbt.DoubleTag.Companion.ZERO
import org.kryptonmc.nbt.FloatTag.Companion.ZERO
import org.kryptonmc.nbt.io.TagReader
import org.kryptonmc.nbt.io.TagWriter
import org.kryptonmc.nbt.util.floor
import java.io.DataOutput

/**
 * A tag that holds a float.
 *
 * This is not directly constructable, as the [ZERO] constant is cached, and we
 * want to return that if the value is zero.
 */
public class FloatTag private constructor(public val value: Float) : NumberTag {

    override val id: Int
        get() = ID
    override val type: TagType
        get() = TYPE

    override fun write(output: DataOutput) {
        WRITER.write(output, this)
    }

    override fun <T> examine(examiner: TagExaminer<T>) {
        examiner.examineFloat(this)
    }

    override fun copy(): FloatTag = this

    override fun equals(other: Any?): Boolean = this === other || (other is FloatTag && value == other.value)

    override fun hashCode(): Int = value.toBits()

    override fun toString(): String = "FloatTag(value=$value)"

    override fun asNumber(): Number = value

    override fun toDouble(): Double = value.toDouble()

    override fun toFloat(): Float = value

    override fun toLong(): Long = value.toLong()

    override fun toInt(): Int = value.floor()

    override fun toShort(): Short = (value.floor() and 0xFFFF).toShort()

    override fun toByte(): Byte = (value.floor() and 0xFF).toByte()

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
        public val READER: TagReader<FloatTag> = TagReader { input, _ -> FloatTag(input.readFloat()) }
        @JvmField
        public val WRITER: TagWriter<FloatTag> = TagWriter { output, value -> output.writeFloat(value.value) }

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
