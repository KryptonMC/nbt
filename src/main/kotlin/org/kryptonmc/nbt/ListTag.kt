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
import java.io.DataInput
import java.io.DataOutput

@Suppress("UNCHECKED_CAST")
public class ListTag(private val data: MutableList<Tag> = mutableListOf(), elementType: Int = 0) : CollectionTag<Tag>(elementType) {

    override var elementType: Int = elementType
        @JvmSynthetic internal set

    override val id: Int = ID
    override val type: TagType = TYPE
    override val size: Int
        get() = data.size

    public fun getByte(index: Int): Byte {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == ByteTag.ID) return (tag as ByteTag).value
        }
        return 0
    }

    public fun getShort(index: Int): Short {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == ShortTag.ID) return (tag as ShortTag).value
        }
        return 0
    }

    public fun getInt(index: Int): Int {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == IntTag.ID) return (tag as IntTag).value
        }
        return 0
    }

    public fun getLong(index: Int): Long {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == LongTag.ID) return (tag as LongTag).value
        }
        return 0
    }

    public fun getFloat(index: Int): Float {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == FloatTag.ID) return (tag as FloatTag).value
        }
        return 0F
    }

    public fun getDouble(index: Int): Double {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == DoubleTag.ID) return (tag as DoubleTag).value
        }
        return 0.0
    }

    public fun getString(index: Int): String {
        if (index in data.indices) {
            val tag = get(index)
            return if (tag.id == StringTag.ID) tag.asString() else tag.toString()
        }
        return ""
    }

    public fun getByteArray(index: Int): ByteArray {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == ByteArrayTag.ID) return (tag as ByteArrayTag).data
        }
        return ByteArray(0)
    }

    public fun getIntArray(index: Int): IntArray {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == IntArrayTag.ID) return (tag as IntArrayTag).data
        }
        return IntArray(0)
    }

    public fun getLongArray(index: Int): LongArray {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == LongArrayTag.ID) return (tag as LongArrayTag).data
        }
        return LongArray(0)
    }

    public fun getList(index: Int): ListTag {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == ID) return tag as ListTag
        }
        return ListTag()
    }

    public fun getCompound(index: Int): CompoundTag {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == CompoundTag.ID) return tag as CompoundTag
        }
        return CompoundTag()
    }

    public inline fun forEachByte(action: (Byte) -> Unit) {
        for (i in indices) action(getByte(i))
    }

    public inline fun forEachShort(action: (Short) -> Unit) {
        for (i in indices) action(getShort(i))
    }

    public inline fun forEachInt(action: (Int) -> Unit) {
        for (i in indices) action(getInt(i))
    }

    public inline fun forEachLong(action: (Long) -> Unit) {
        for (i in indices) action(getLong(i))
    }

    public inline fun forEach(action: (Float) -> Unit) {
        for (i in indices) action(getFloat(i))
    }

    public inline fun forEachDouble(action: (Double) -> Unit) {
        for (i in indices) action(getDouble(i))
    }

    public inline fun forEachString(action: (String) -> Unit) {
        for (i in indices) action(getString(i))
    }

    public inline fun forEachByteArray(action: (ByteArray) -> Unit) {
        for (i in indices) action(getByteArray(i))
    }

    public inline fun forEachIntArray(action: (IntArray) -> Unit) {
        for (i in indices) action(getIntArray(i))
    }

    public inline fun forEachLongArray(action: (LongArray) -> Unit) {
        for (i in indices) action(getLongArray(i))
    }

    public inline fun forEachList(action: (ListTag) -> Unit) {
        for (i in indices) action(getList(i))
    }

    public inline fun forEachCompound(action: (CompoundTag) -> Unit) {
        for (i in indices) action(getCompound(i))
    }

    override fun get(index: Int): Tag = data[index]

    override fun set(index: Int, element: Tag): Tag {
        val oldValue = data[index]
        if (!setTag(index, element)) throw UnsupportedOperationException("Trying to add tag of type ${element.id} to list of type $elementType")
        return oldValue
    }

    override fun add(index: Int, element: Tag) {
        if (!addTag(index, element)) throw UnsupportedOperationException("Trying to add tag of type ${element.id} to list of type $elementType")
    }

    override fun setTag(index: Int, tag: Tag): Boolean = if (updateType(tag)) {
        data[index] = tag
        true
    } else false

    override fun addTag(index: Int, tag: Tag): Boolean = if (updateType(tag)) {
        data.add(index, tag)
        true
    } else false

    override fun removeAt(index: Int): Tag {
        val oldValue = data.removeAt(index)
        if (data.isEmpty()) elementType = 0
        return oldValue
    }

    override fun write(output: DataOutput): Unit = WRITER.write(output, this)

    override fun <T> examine(examiner: TagExaminer<T>): Unit = examiner.examineList(this)

    override fun copy(): ListTag {
        val iterable = if (elementType.toTagType().isValue) data else data.map { it.copy() }
        val list = ArrayList(iterable)
        return ListTag(list, elementType)
    }

    override fun clear() {
        data.clear()
        elementType = 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return data == (other as ListTag).data
    }

    override fun hashCode(): Int = data.hashCode()

    override fun toString(): String = asString()

    private fun updateType(tag: Tag): Boolean {
        if (tag.id == 0) return false
        if (elementType == 0) {
            elementType = tag.id
            return true
        }
        return elementType == tag.id
    }

    public companion object {

        public const val ID: Int = 9
        public val TYPE: TagType = TagType("TAG_List")
        public val READER: TagReader<ListTag> = object : TagReader<ListTag> {

            override fun read(input: DataInput, depth: Int): ListTag {
                if (depth > 512) throw RuntimeException("Depth too high! Given depth $depth is higher than maximum depth 512!")
                val type = input.readByte().toInt()
                val size = input.readInt()
                if (type == 0 && size > 0) throw RuntimeException("Missing required type byte for ListTag!")
                val reader = type.toTagReader()
                val data = ArrayList<Tag>(size)
                for (i in 0 until size) data.add(reader.read(input, depth + 1))
                return ListTag(data, type)
            }
        }
        public val WRITER: TagWriter<ListTag> = object : TagWriter<ListTag> {

            @Suppress("UNCHECKED_CAST")
            override fun write(output: DataOutput, tag: ListTag) {
                tag.elementType = if (tag.data.isEmpty()) 0 else tag.data[0].id
                output.writeByte(tag.elementType)
                output.writeInt(tag.data.size)
                for (i in tag.data.indices) tag.data[i].write(output)
            }
        }
    }
}
