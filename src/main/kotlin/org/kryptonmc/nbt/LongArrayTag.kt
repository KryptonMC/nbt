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
import java.io.DataInput
import java.io.DataOutput

@Suppress("UNCHECKED_CAST")
public class LongArrayTag(data: LongArray) : CollectionTag<LongTag>(LongTag.ID) {

    public var data: LongArray = data
        private set

    override val id: Int = ID
    override val type: TagType = TYPE
    override val reader: TagReader<LongArrayTag> = READER
    override val writer: TagWriter<Tag> = WRITER as TagWriter<Tag>
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

    override fun <T> examine(examiner: TagExaminer<T>): Unit = examiner.examineLongArray(this)

    override fun copy(): LongArrayTag {
        val copy = LongArray(data.size)
        System.arraycopy(data, 0, copy, 0, data.size)
        return LongArrayTag(copy)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return data.contentEquals((other as LongArrayTag).data)
    }

    override fun hashCode(): Int = data.contentHashCode()

    public companion object {

        public const val ID: Int = 12
        public val TYPE: TagType = TagType("TAG_Long_Array")
        public val READER: TagReader<LongArrayTag> = object : TagReader<LongArrayTag> {

            override fun read(input: DataInput, depth: Int): LongArrayTag {
                val size = input.readInt()
                val longs = LongArray(size)
                for (i in 0 until size) longs[i] = input.readLong()
                return LongArrayTag(longs)
            }
        }
        public val WRITER: TagWriter<LongArrayTag> = object : TagWriter<LongArrayTag> {

            override fun write(output: DataOutput, tag: LongArrayTag) {
                output.writeInt(tag.data.size)
                tag.data.forEach { output.writeLong(it) }
            }
        }
    }
}
