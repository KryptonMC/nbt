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
import org.kryptonmc.nbt.util.add
import org.kryptonmc.nbt.util.arraycopy
import org.kryptonmc.nbt.util.remove
import kotlin.jvm.JvmField

public class LongArrayTag(data: LongArray) : AbstractMutableList<LongTag>(), MutableCollectionTag<LongTag> {

    public var data: LongArray = data
        private set

    override val id: Int = ID
    override val elementType: Int = LongTag.ID
    override val type: TagType = TYPE
    override val size: Int
        get() = data.size

    public constructor(data: Collection<Long>) : this(data.toLongArray())

    override fun get(index: Int): LongTag = LongTag.of(data[index])

    override fun set(index: Int, element: LongTag): LongTag {
        val oldValue = data[index]
        data[index] = element.value
        return LongTag.of(oldValue)
    }

    override fun add(index: Int, element: LongTag) {
        data = data.add(index, element.value)
    }

    override fun setTag(index: Int, tag: Tag): Boolean {
        if (tag !is NumberTag) return false
        data[index] = tag.toLong()
        return true
    }

    override fun addTag(index: Int, tag: Tag): Boolean {
        if (tag !is NumberTag) return false
        data = data.add(index, tag.toLong())
        return true
    }

    override fun removeAt(index: Int): LongTag {
        val oldValue = data[index]
        data = data.remove(index)
        return LongTag.of(oldValue)
    }

    override fun clear() {
        data = LongArray(0)
    }

    override fun write(output: BufferedSink): Unit = WRITER.write(output, this)

    override fun <T> examine(examiner: TagExaminer<T>): Unit = examiner.examineLongArray(this)

    override fun copy(): LongArrayTag {
        val copy = LongArray(data.size)
        arraycopy(data, 0, copy, 0, data.size)
        return LongArrayTag(copy)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LongArrayTag) return false
        return data.contentEquals(other.data)
    }

    override fun hashCode(): Int = data.contentHashCode()

    public companion object {

        public const val ID: Int = 12
        @JvmField
        public val TYPE: TagType = TagType("TAG_Long_Array")
        @JvmField
        public val READER: TagReader<LongArrayTag> = object : TagReader<LongArrayTag> {

            override fun read(input: BufferedSource, depth: Int): LongArrayTag {
                val size = input.readInt()
                val longs = LongArray(size)
                for (i in 0 until size) longs[i] = input.readLong()
                return LongArrayTag(longs)
            }
        }
        @JvmField
        public val WRITER: TagWriter<LongArrayTag> = object : TagWriter<LongArrayTag> {

            override fun write(output: BufferedSink, value: LongArrayTag) {
                output.writeInt(value.data.size)
                for (i in value.data.indices) output.writeLong(value.data[i])
            }
        }
    }
}
