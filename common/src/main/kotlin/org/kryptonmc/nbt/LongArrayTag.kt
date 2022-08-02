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
import org.kryptonmc.nbt.util.add
import org.kryptonmc.nbt.util.remove
import java.io.DataOutput

/**
 * A tag that holds a long array.
 */
public class LongArrayTag(data: LongArray) : Tag {

    /**
     * The backing data for this tag.
     */
    public var data: LongArray = data
        private set
    public val size: Int
        get() = data.size

    override val id: Int
        get() = ID
    override val type: TagType
        get() = TYPE

    /**
     * Creates a new long array tag from the given [data].
     *
     * @param data the backing data for the tag
     */
    public constructor(data: Collection<Long>) : this(data.toLongArray())

    public fun get(index: Int): Long = data[index]

    public fun set(index: Int, value: Long) {
        data[index] = value
    }

    public fun add(value: Long) {
        data = data.copyOf(data.size + 1)
        data[data.size - 1] = value
    }

    public fun add(index: Int, value: Long) {
        data = data.add(index, value)
    }

    public fun remove(index: Int) {
        data = data.remove(index)
    }

    public fun forEach(action: (Long) -> Unit) {
        data.forEach(action)
    }

    public fun clear() {
        data = EMPTY_DATA
    }

    override fun write(output: DataOutput) {
        WRITER.write(output, this)
    }

    override fun <T> examine(examiner: TagExaminer<T>) {
        examiner.examineLongArray(this)
    }

    override fun copy(): LongArrayTag {
        val copy = LongArray(data.size)
        System.arraycopy(data, 0, copy, 0, data.size)
        return LongArrayTag(copy)
    }

    override fun equals(other: Any?): Boolean = this === other || (other is LongArrayTag && data.contentEquals(other.data))

    override fun hashCode(): Int = data.contentHashCode()

    public companion object {

        public const val ID: Int = 12
        @JvmField
        public val TYPE: TagType = TagType("TAG_Long_Array")
        @JvmField
        public val READER: TagReader<LongArrayTag> = TagReader { input, _ ->
            val size = input.readInt()
            val longs = LongArray(size)
            for (i in 0 until size) {
                longs[i] = input.readLong()
            }
            LongArrayTag(longs)
        }
        @JvmField
        public val WRITER: TagWriter<LongArrayTag> = TagWriter { output, value ->
            output.writeInt(value.data.size)
            for (i in value.data.indices) {
                output.writeLong(value.data[i])
            }
        }
        @JvmSynthetic
        internal val EMPTY_DATA: LongArray = LongArray(0)
    }
}
