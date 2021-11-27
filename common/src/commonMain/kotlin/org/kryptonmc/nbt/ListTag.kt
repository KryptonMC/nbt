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

/**
 * A tag holding a list of values of the given tag type [elementType].
 *
 * @param data the backing data
 */
public sealed class ListTag(
    public open val data: List<Tag> = emptyList(),
    elementType: Int = 0
) : AbstractList<Tag>(), CollectionTag<Tag> {

    final override val id: Int = ID
    final override val type: TagType = TYPE
    final override var elementType: Int = elementType
        @JvmSynthetic internal set

    /**
     * Gets the byte value at the given [index], or returns the given
     * [default] value if there is no byte value at the given [index].
     *
     * @param index the index
     * @return the byte value, or the default if not present
     */
    @JvmOverloads
    public fun getByte(index: Int, default: Byte = 0): Byte {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == ByteTag.ID) return (tag as ByteTag).value
        }
        return default
    }

    /**
     * Gets the short value at the given [index], or returns the given
     * [default] value if there is no short value at the given [index].
     *
     * @param index the index
     * @return the short value, or the default if not present
     */
    @JvmOverloads
    public fun getShort(index: Int, default: Short = 0): Short {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == ShortTag.ID) return (tag as ShortTag).value
        }
        return default
    }

    /**
     * Gets the integer value at the given [index], or returns the given
     * [default] value if there is no integer value at the given [index].
     *
     * @param index the index
     * @return the integer value, or the default if not present
     */
    @JvmOverloads
    public fun getInt(index: Int, default: Int = 0): Int {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == IntTag.ID) return (tag as IntTag).value
        }
        return default
    }

    /**
     * Gets the long value at the given [index], or returns the given
     * [default] value if there is no long value at the given [index].
     *
     * @param index the index
     * @return the long value, or the default if not present
     */
    @JvmOverloads
    public fun getLong(index: Int, default: Long = 0L): Long {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == LongTag.ID) return (tag as LongTag).value
        }
        return default
    }

    /**
     * Gets the float value at the given [index], or returns the given
     * [default] value if there is no float value at the given [index].
     *
     * @param index the index
     * @return the float value, or the default if not present
     */
    @JvmOverloads
    public fun getFloat(index: Int, default: Float = 0F): Float {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == FloatTag.ID) return (tag as FloatTag).value
        }
        return default
    }

    /**
     * Gets the double value at the given [index], or returns the given
     * [default] value if there is no double value at the given [index].
     *
     * @param index the index
     * @return the double value, or the default if not present
     */
    @JvmOverloads
    public fun getDouble(index: Int, default: Double = 0.0): Double {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == DoubleTag.ID) return (tag as DoubleTag).value
        }
        return default
    }

    /**
     * Gets the string value at the given [index], or returns the given
     * [default] value if there is no string value at the given [index].
     *
     * @param index the index
     * @return the string value, or the default if not present
     */
    @JvmOverloads
    public fun getString(index: Int, default: String = ""): String {
        if (index in data.indices) {
            val tag = get(index)
            return if (tag.id == StringTag.ID) tag.asString() else tag.toString()
        }
        return default
    }

    /**
     * Gets the byte array at the given [index], or returns the given
     * [default] if there is no byte array at the given [index].
     *
     * @param index the index
     * @return the byte array, or the default if not present
     */
    @JvmOverloads
    public fun getByteArray(index: Int, default: ByteArray = ByteArray(0)): ByteArray {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == ByteArrayTag.ID) return (tag as ByteArrayTag).data
        }
        return default
    }

    /**
     * Gets the integer array at the given [index], or returns the given
     * [default] if there is no integer array at the given [index].
     *
     * @param index the index
     * @return the integer array, or the default if not present
     */
    @JvmOverloads
    public fun getIntArray(index: Int, default: IntArray = IntArray(0)): IntArray {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == IntArrayTag.ID) return (tag as IntArrayTag).data
        }
        return default
    }

    /**
     * Gets the long array at the given [index], or returns the given
     * [default] if there is no long array at the given [index].
     *
     * @param index the index
     * @return the long array, or the default if not present
     */
    @JvmOverloads
    public fun getLongArray(index: Int, default: LongArray = LongArray(0)): LongArray {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == LongArrayTag.ID) return (tag as LongArrayTag).data
        }
        return default
    }

    /**
     * Gets the list at the given [index], or returns the given [default] if
     * there is no list at the given [index].
     *
     * @param index the index
     * @return the list, or the default if not present
     */
    @JvmOverloads
    public fun getList(index: Int, default: ListTag = empty()): ListTag {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == ID) return tag as ListTag
        }
        return default
    }

    /**
     * Gets the compound at the given [index], or returns the given [default]
     * if there is no compound at the given [index].
     *
     * @param index the index
     * @return the compound, or the default if not present
     */
    @JvmOverloads
    public fun getCompound(index: Int, default: CompoundTag = CompoundTag.empty()): CompoundTag {
        if (index in data.indices) {
            val tag = get(index)
            if (tag.id == CompoundTag.ID) return tag as CompoundTag
        }
        return default
    }

    /**
     * Iterates over every tag in this list, and for every tag that is a
     * [ByteTag], applies the given [action] to the value.
     *
     * @param action the action to apply to every byte value
     */
    public inline fun forEachByte(action: (Byte) -> Unit) {
        for (i in indices) action(getByte(i))
    }

    /**
     * Iterates over every tag in this list, and for every tag that is a
     * [ShortTag], applies the given [action] to the value.
     *
     * @param action the action to apply to every short value
     */
    public inline fun forEachShort(action: (Short) -> Unit) {
        for (i in indices) action(getShort(i))
    }

    /**
     * Iterates over every tag in this list, and for every tag that is an
     * [IntTag], applies the given [action] to the value.
     *
     * @param action the action to apply to every integer value
     */
    public inline fun forEachInt(action: (Int) -> Unit) {
        for (i in indices) action(getInt(i))
    }

    /**
     * Iterates over every tag in this list, and for every tag that is a
     * [LongTag], applies the given [action] to the value.
     *
     * @param action the action to apply to every long value
     */
    public inline fun forEachLong(action: (Long) -> Unit) {
        for (i in indices) action(getLong(i))
    }

    /**
     * Iterates over every tag in this list, and for every tag that is a
     * [FloatTag], applies the given [action] to the value.
     *
     * @param action the action to apply to every float value
     */
    public inline fun forEachFloat(action: (Float) -> Unit) {
        for (i in indices) action(getFloat(i))
    }

    /**
     * Iterates over every tag in this list, and for every tag that is a
     * [DoubleTag], applies the given [action] to the value.
     *
     * @param action the action to apply to every double value
     */
    public inline fun forEachDouble(action: (Double) -> Unit) {
        for (i in indices) action(getDouble(i))
    }

    /**
     * Iterates over every tag in this list, and for every tag that is a
     * [StringTag], applies the given [action] to the value.
     *
     * @param action the action to apply to every string value
     */
    public inline fun forEachString(action: (String) -> Unit) {
        for (i in indices) action(getString(i))
    }

    /**
     * Iterates over every tag in this list, and for every tag that is a
     * [ByteArrayTag], applies the given [action] to the array.
     *
     * @param action the action to apply to every byte array
     */
    public inline fun forEachByteArray(action: (ByteArray) -> Unit) {
        for (i in indices) action(getByteArray(i))
    }

    /**
     * Iterates over every tag in this list, and for every tag that is an
     * [IntArrayTag], applies the given [action] to the array.
     *
     * @param action the action to apply to every integer array
     */
    public inline fun forEachIntArray(action: (IntArray) -> Unit) {
        for (i in indices) action(getIntArray(i))
    }

    /**
     * Iterates over every tag in this list, and for every tag that is a
     * [LongArrayTag], applies the given [action] to the array.
     *
     * @param action the action to apply to every long array
     */
    public inline fun forEachLongArray(action: (LongArray) -> Unit) {
        for (i in indices) action(getLongArray(i))
    }

    /**
     * Iterates over every tag in this list, and for every tag that is a
     * [ListTag], applies the given [action] to the list.
     *
     * @param action the action to apply to every list
     */
    public inline fun forEachList(action: (ListTag) -> Unit) {
        for (i in indices) action(getList(i))
    }

    /**
     * Iterates over every tag in this list, and for every tag that is a
     * [CompoundTag], applies the given [action] to the compound.
     *
     * @param action the action to apply to every compound
     */
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

    /**
     * A builder for building list tags.
     */
    public class Builder internal constructor(private val mutable: Boolean, private var elementType: Int) {

        private val data = mutableListOf<Tag>()

        /**
         * Adds the given [tag] to this list tag.
         *
         * @param tag the tag
         * @return this builder
         */
        @NBTDsl
        public fun add(tag: Tag): Builder = apply {
            require(data.isEmpty() || elementType == tag.id) {
                "Cannot append element of type ${tag.id} to a builder for type $elementType!"
            }
            data.add(tag)
            elementType = tag.id
        }

        /**
         * Adds the given byte [value] to this list tag.
         *
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        public fun addByte(value: Byte): Builder = add(ByteTag.of(value))

        /**
         * Adds the given boolean [value] to this list tag.
         *
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        public fun addBoolean(value: Boolean): Builder = add(ByteTag.of(value))

        /**
         * Adds the given short [value] to this list tag.
         *
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        public fun addShort(value: Short): Builder = add(ShortTag.of(value))

        /**
         * Adds the given integer [value] to this list tag.
         *
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        public fun addInt(value: Int): Builder = add(IntTag.of(value))

        /**
         * Adds the given long [value] to this list tag.
         *
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        public fun addLong(value: Long): Builder = add(LongTag.of(value))

        /**
         * Adds the given float [value] to this list tag.
         *
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        public fun addFloat(value: Float): Builder = add(FloatTag.of(value))

        /**
         * Adds the given double [value] to this list tag.
         *
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        public fun addDouble(value: Double): Builder = add(DoubleTag.of(value))

        /**
         * Adds the given byte array [value] to this list tag.
         *
         * @param value the array
         * @return this builder
         */
        @NBTDsl
        public fun addByteArray(value: ByteArray): Builder = add(ByteArrayTag(value))

        /**
         * Adds the given integer array [value] to this list tag.
         *
         * @param value the array
         * @return this builder
         */
        @NBTDsl
        public fun addIntArray(value: IntArray): Builder = add(IntArrayTag(value))

        /**
         * Adds the given long array [value] to this list tag.
         *
         * @param value the array
         * @return this builder
         */
        @NBTDsl
        public fun addLongArray(value: LongArray): Builder = add(LongArrayTag(value))

        /**
         * Adds the given string [value] to this list tag.
         *
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        public fun addString(value: String): Builder = add(StringTag.of(value))

        /**
         * Adds the given UUID [value] to this list tag.
         *
         * How UUIDs are stored is explained by [CompoundTag.hasUUID].
         *
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        public fun addUUID(value: UUID): Builder = add(value.toTag())

        /**
         * Adds the given byte [values] to this list tag.
         *
         * @param values the values
         * @return this builder
         */
        @NBTDsl
        public fun addBytes(vararg values: Byte): Builder = addByteArray(values)

        /**
         * Adds the given integer [values] to this list tag.
         *
         * @param values the values
         * @return this builder
         */
        @NBTDsl
        public fun addInts(vararg values: Int): Builder = addIntArray(values)

        /**
         * Adds the given long [values] to this list tag.
         *
         * @param values the values
         * @return this builder
         */
        @NBTDsl
        public fun addLongs(vararg values: Long): Builder = addLongArray(values)

        /**
         * Creates a new list tag containing the values set in this builder.
         * Whether the resulting tag is mutable or immutable depends on what
         * was supplied to the builder when the builder functions were called.
         *
         * @return a new list tag
         */
        public fun build(): ListTag = if (mutable) mutable(data, elementType) else immutable(data, elementType)
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
        private val EMPTY = ImmutableListTag()

        /**
         * Creates a new builder for building a list tag.
         *
         * @param elementType the type of tags that will be stored
         * @param mutable if the resulting built tag will be mutable
         */
        @JvmStatic
        @JvmOverloads
        public fun builder(elementType: Int = EndTag.ID, mutable: Boolean = true): Builder = Builder(mutable, elementType)

        @JvmStatic
        @Deprecated("Not all list tags are mutable any more.", ReplaceWith("ListTag.mutable"))
        public fun of(data: List<Tag>, elementType: Int): ListTag = mutable(data, elementType)

        /**
         * Creates a new mutable list tag with the given [data] and
         * [elementType].
         * If the [data] is not a [MutableList], it will be converted to one
         * using [List.toMutableList].
         *
         * @param data the data
         * @return a new mutable list tag
         */
        @JvmStatic
        public fun mutable(data: List<Tag>, elementType: Int): ListTag {
            val result = if (data is MutableList) data else data.toMutableList()
            return MutableListTag(result, elementType)
        }

        /**
         * Creates a new immutable list tag with the given [data].
         *
         * @param data the data
         * @return a new immutable list tag
         */
        @JvmStatic
        public fun immutable(data: List<Tag>, elementType: Int): ListTag = ImmutableListTag(data, elementType)

        /**
         * Gets the empty list tag.
         *
         * This is an immutable list tag with an empty list backing it,
         * meaning no data can be modified.
         */
        @JvmStatic
        public fun empty(): ListTag = EMPTY
    }
}
