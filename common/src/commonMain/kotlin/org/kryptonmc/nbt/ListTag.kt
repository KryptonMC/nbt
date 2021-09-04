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
import org.kryptonmc.nbt.io.Types
import org.kryptonmc.nbt.util.UUID
import org.kryptonmc.nbt.util.toTag
import kotlin.jvm.JvmField
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic
import kotlin.jvm.JvmSynthetic

public sealed class ListTag(
    public open val data: List<Tag> = mutableListOf(),
    elementType: Int = 0
) : AbstractList<Tag>(), CollectionTag<Tag> {

    final override val id: Int = ID
    final override val type: TagType = TYPE
    final override var elementType: Int = elementType
        @JvmSynthetic internal set

    @JvmOverloads
    public fun getByte(index: Int, default: Byte = 0): Byte {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == ByteTag.ID) return (tag as ByteTag).value
        }
        return default
    }

    @JvmOverloads
    public fun getShort(index: Int, default: Short = 0): Short {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == ShortTag.ID) return (tag as ShortTag).value
        }
        return default
    }

    @JvmOverloads
    public fun getInt(index: Int, default: Int = 0): Int {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == IntTag.ID) return (tag as IntTag).value
        }
        return default
    }

    @JvmOverloads
    public fun getLong(index: Int, default: Long = 0L): Long {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == LongTag.ID) return (tag as LongTag).value
        }
        return default
    }

    @JvmOverloads
    public fun getFloat(index: Int, default: Float = 0F): Float {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == FloatTag.ID) return (tag as FloatTag).value
        }
        return default
    }

    @JvmOverloads
    public fun getDouble(index: Int, default: Double = 0.0): Double {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == DoubleTag.ID) return (tag as DoubleTag).value
        }
        return default
    }

    @JvmOverloads
    public fun getString(index: Int, default: String = ""): String {
        if (index in data.indices) {
            val tag = get(index)
            return if (tag.id == StringTag.ID) tag.asString() else tag.toString()
        }
        return default
    }

    @JvmOverloads
    public fun getByteArray(index: Int, default: ByteArray = ByteArray(0)): ByteArray {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == ByteArrayTag.ID) return (tag as ByteArrayTag).data
        }
        return default
    }

    @JvmOverloads
    public fun getIntArray(index: Int, default: IntArray = IntArray(0)): IntArray {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == IntArrayTag.ID) return (tag as IntArrayTag).data
        }
        return default
    }

    @JvmOverloads
    public fun getLongArray(index: Int, default: LongArray = LongArray(0)): LongArray {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == LongArrayTag.ID) return (tag as LongArrayTag).data
        }
        return default
    }

    @JvmOverloads
    public fun getList(index: Int, default: ListTag = MutableListTag()): ListTag {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == ID) return tag as ListTag
        }
        return default
    }

    @JvmOverloads
    public fun getCompound(index: Int, default: CompoundTag = MutableCompoundTag()): CompoundTag {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == CompoundTag.ID) return tag as CompoundTag
        }
        return default
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

    public inline fun forEachFloat(action: (Float) -> Unit) {
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

    public abstract override fun copy(): ListTag

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ListTag) return false
        return data == other.data
    }

    final override fun hashCode(): Int = data.hashCode()

    final override fun toString(): String = "ListTag(data=$data)"

    final override fun write(output: BufferedSink) {
        WRITER.write(output, this)
    }

    final override fun <T> examine(examiner: TagExaminer<T>): Unit = examiner.examineList(this)

    public class Builder internal constructor(private var elementType: Int) {

        private val data = mutableListOf<Tag>()

        @NBTDsl
        public fun add(tag: Tag): Builder = apply {
            require(data.isEmpty() || elementType == tag.id) {
                "Cannot append element of type ${tag.id} to a builder for type $elementType!"
            }
            data.add(tag)
            elementType = tag.id
        }

        @NBTDsl
        public fun addByte(value: Byte): Builder = add(ByteTag.of(value))

        @NBTDsl
        public fun addShort(value: Short): Builder = add(ShortTag.of(value))

        @NBTDsl
        public fun addInt(value: Int): Builder = add(IntTag.of(value))

        @NBTDsl
        public fun addLong(value: Long): Builder = add(LongTag.of(value))

        @NBTDsl
        public fun addFloat(value: Float): Builder = add(FloatTag.of(value))

        @NBTDsl
        public fun addDouble(value: Double): Builder = add(DoubleTag.of(value))

        @NBTDsl
        public fun addByteArray(value: ByteArray): Builder = add(ByteArrayTag(value))

        @NBTDsl
        public fun addIntArray(value: IntArray): Builder = add(IntArrayTag(value))

        @NBTDsl
        public fun addLongArray(value: LongArray): Builder = add(LongArrayTag(value))

        @NBTDsl
        public fun addString(value: String): Builder = add(StringTag.of(value))

        @NBTDsl
        public fun addUUID(value: UUID): Builder = add(value.toTag())

        @NBTDsl
        public fun addBytes(vararg values: Byte): Builder = addByteArray(values)

        @NBTDsl
        public fun addInts(vararg values: Int): Builder = addIntArray(values)

        @NBTDsl
        public fun addLongs(vararg values: Long): Builder = addLongArray(values)

        public fun build(): ListTag = MutableListTag(data)
    }

    public companion object {

        public const val ID: Int = 9
        @JvmField
        public val TYPE: TagType = TagType("TAG_List")
        @JvmField
        public val READER: TagReader<ListTag> = object : TagReader<ListTag> {

            override fun read(input: BufferedSource, depth: Int): ListTag {
                if (depth > 512) throw RuntimeException("Depth too high! Given depth $depth is higher than maximum depth 512!")
                val type = input.readByte().toInt()
                val size = input.readInt()
                if (type == 0 && size > 0) throw RuntimeException("Missing required type byte for ListTag!")
                val reader = Types.reader(type)
                val data = ArrayList<Tag>(size)
                for (i in 0 until size) {
                    data.add(reader.read(input, depth + 1))
                }
                return MutableListTag(data, type)
            }
        }
        @JvmField
        public val WRITER: TagWriter<ListTag> = object : TagWriter<ListTag> {

            @Suppress("UNCHECKED_CAST")
            override fun write(output: BufferedSink, value: ListTag) {
                value.elementType = if (value.data.isEmpty()) 0 else value.data[0].id
                output.writeByte(value.elementType)
                output.writeInt(value.data.size)
                for (i in value.data.indices) {
                    value.data[i].write(output)
                }
            }
        }

        @JvmStatic
        @JvmOverloads
        public fun builder(elementType: Int = EndTag.ID): Builder = Builder(elementType)

        @JvmStatic
        public fun of(data: List<Tag>, elementType: Int): ListTag =
            MutableListTag(if (data is MutableList) data else data.toMutableList(), elementType)
    }
}
