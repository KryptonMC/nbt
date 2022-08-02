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
import org.kryptonmc.nbt.io.TagReader
import org.kryptonmc.nbt.io.TagWriter
import org.kryptonmc.nbt.util.floor
import java.io.DataOutput
import kotlin.math.floor

/**
 * A tag that holds a double.
 *
 * This is not directly constructable, as the [ZERO] constant is cached, and we
 * want to return that if the value is zero.
 */
public class DoubleTag private constructor(public val value: Double) : NumberTag {

    override val id: Int
        get() = ID
    override val type: TagType
        get() = TYPE

    override fun write(output: DataOutput) {
        WRITER.write(output, this)
    }

    override fun <T> examine(examiner: TagExaminer<T>) {
        examiner.examineDouble(this)
    }

    override fun copy(): DoubleTag = this

    override fun equals(other: Any?): Boolean = this === other || (other is DoubleTag && value == other.value)

    override fun hashCode(): Int {
        val temp = value.toBits()
        return (temp xor (temp ushr 32)).toInt()
    }

    override fun toString(): String = "DoubleTag(value=$value)"

    override fun asNumber(): Number = value

    override fun toDouble(): Double = value

    override fun toFloat(): Float = value.toFloat()

    override fun toLong(): Long = floor(value).toLong()

    override fun toInt(): Int = value.floor()

    override fun toShort(): Short = (value.floor() and 0xFFFF).toShort()

    override fun toByte(): Byte = (value.floor() and 0xFF).toByte()

    public companion object {
        /**
         * The double tag representing the constant zero.
         */
        @JvmField
        public val ZERO: DoubleTag = DoubleTag(0.0)

        public const val ID: Int = 6
        @JvmField
        public val TYPE: TagType = TagType("TAG_Double", true)
        @JvmField
        public val READER: TagReader<DoubleTag> = TagReader { input, _ -> DoubleTag(input.readDouble()) }
        @JvmField
        public val WRITER: TagWriter<DoubleTag> = TagWriter { output, value -> output.writeDouble(value.value) }

        /**
         * Creates a new double tag holding the given [value].
         *
         * @param value the value
         * @return a new double tag
         */
        @JvmStatic
        public fun of(value: Double): DoubleTag = if (value == 0.0) ZERO else DoubleTag(value)
    }
}
