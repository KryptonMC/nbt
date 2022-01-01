/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

import kotlinx.collections.immutable.toPersistentList
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

/**
 * A tag holding a list of values of the given tag type [elementType].
 */
public sealed class ListTag : List<Tag>, Tag {

    /**
     * The backing data held by this list tag.
     */
    public abstract val data: List<Tag>

    /**
     * The type of element held by this list tag.
     */
    public abstract val elementType: Int

    final override val id: Int = ID
    final override val type: TagType = TYPE

    override val size: Int
        get() = data.size

    override fun contains(element: Tag): Boolean = data.contains(element)

    override fun containsAll(elements: Collection<Tag>): Boolean = data.containsAll(elements)

    /**
     * Gets the tag at the given [index].
     *
     * @param index the index
     * @return the tag
     */
    override fun get(index: Int): Tag = data[index]

    /**
     * Gets the boolean value at the given [index], or returns the given
     * [default] value if there is no boolean value at the given [index].
     *
     * @param index the index
     * @param default the default value
     * @return
     */
    @JvmOverloads
    public fun getBoolean(index: Int, default: Boolean = false): Boolean {
        val tag = get<ByteTag>(index, ByteTag.ID, null) ?: return default
        return tag.value != 0.toByte()
    }

    /**
     * Gets the byte value at the given [index], or returns the given
     * [default] value if there is no byte value at the given [index].
     *
     * @param index the index
     * @return the byte value, or the default if not present
     */
    @JvmOverloads
    public fun getByte(index: Int, default: Byte = 0): Byte = get<ByteTag>(index, ByteTag.ID, null)?.value ?: default

    /**
     * Gets the short value at the given [index], or returns the given
     * [default] value if there is no short value at the given [index].
     *
     * @param index the index
     * @return the short value, or the default if not present
     */
    @JvmOverloads
    public fun getShort(index: Int, default: Short = 0): Short = get<ShortTag>(index, ShortTag.ID, null)?.value ?: default

    /**
     * Gets the integer value at the given [index], or returns the given
     * [default] value if there is no integer value at the given [index].
     *
     * @param index the index
     * @return the integer value, or the default if not present
     */
    @JvmOverloads
    public fun getInt(index: Int, default: Int = 0): Int = get<IntTag>(index, IntTag.ID, null)?.value ?: default

    /**
     * Gets the long value at the given [index], or returns the given
     * [default] value if there is no long value at the given [index].
     *
     * @param index the index
     * @return the long value, or the default if not present
     */
    @JvmOverloads
    public fun getLong(index: Int, default: Long = 0L): Long = get<LongTag>(index, LongTag.ID, null)?.value ?: default

    /**
     * Gets the float value at the given [index], or returns the given
     * [default] value if there is no float value at the given [index].
     *
     * @param index the index
     * @return the float value, or the default if not present
     */
    @JvmOverloads
    public fun getFloat(index: Int, default: Float = 0F): Float = get<FloatTag>(index, FloatTag.ID, null)?.value ?: default

    /**
     * Gets the double value at the given [index], or returns the given
     * [default] value if there is no double value at the given [index].
     *
     * @param index the index
     * @return the double value, or the default if not present
     */
    @JvmOverloads
    public fun getDouble(index: Int, default: Double = 0.0): Double = get<DoubleTag>(index, DoubleTag.ID, null)?.value ?: default

    /**
     * Gets the string value at the given [index], or returns the given
     * [default] value if there is no string value at the given [index].
     *
     * @param index the index
     * @return the string value, or the default if not present
     */
    @JvmOverloads
    public fun getString(index: Int, default: String = ""): String = get<StringTag>(index, StringTag.ID, null)?.value ?: default

    /**
     * Gets the byte array at the given [index], or returns the given
     * [default] if there is no byte array at the given [index].
     *
     * @param index the index
     * @return the byte array, or the default if not present
     */
    @JvmOverloads
    public fun getByteArray(
        index: Int,
        default: ByteArray = ByteArrayTag.EMPTY_DATA
    ): ByteArray = get<ByteArrayTag>(index, ByteArrayTag.ID, null)?.data ?: default

    /**
     * Gets the integer array at the given [index], or returns the given
     * [default] if there is no integer array at the given [index].
     *
     * @param index the index
     * @return the integer array, or the default if not present
     */
    @JvmOverloads
    public fun getIntArray(
        index: Int,
        default: IntArray = IntArrayTag.EMPTY_DATA
    ): IntArray = get<IntArrayTag>(index, IntArrayTag.ID, null)?.data ?: default

    /**
     * Gets the long array at the given [index], or returns the given
     * [default] if there is no long array at the given [index].
     *
     * @param index the index
     * @return the long array, or the default if not present
     */
    @JvmOverloads
    public fun getLongArray(
        index: Int,
        default: LongArray = LongArrayTag.EMPTY_DATA
    ): LongArray = get<LongArrayTag>(index, LongArrayTag.ID, null)?.data ?: default

    /**
     * Gets the list at the given [index], or returns the given [default] if
     * there is no list at the given [index].
     *
     * @param index the index
     * @return the list, or the default if not present
     */
    @JvmOverloads
    public fun getList(index: Int, default: ListTag = empty()): ListTag = get(index, ID, default)!!

    /**
     * Gets the compound at the given [index], or returns the given [default]
     * if there is no compound at the given [index].
     *
     * @param index the index
     * @return the compound, or the default if not present
     */
    @JvmOverloads
    public fun getCompound(index: Int, default: CompoundTag = CompoundTag.empty()): CompoundTag = get(index, CompoundTag.ID, default)!!

    @Suppress("UNCHECKED_CAST")
    private fun <T : Tag> get(index: Int, type: Int, default: T?): T? {
        if (index in data.indices) {
            val tag = data[index]
            if (tag.id == type) return tag as T
        }
        return default
    }

    public abstract fun add(tag: Tag): ListTag

    public abstract fun removeAt(index: Int): ListTag

    public abstract fun remove(tag: Tag): ListTag

    public abstract fun set(index: Int, tag: Tag): ListTag

    public abstract fun setBoolean(index: Int, value: Boolean): ListTag

    public abstract fun setByte(index: Int, value: Byte): ListTag

    public abstract fun setShort(index: Int, value: Short): ListTag

    public abstract fun setInt(index: Int, value: Int): ListTag

    public abstract fun setLong(index: Int, value: Long): ListTag

    public abstract fun setFloat(index: Int, value: Float): ListTag

    public abstract fun setDouble(index: Int, value: Double): ListTag

    public abstract fun setString(index: Int, value: String): ListTag

    public abstract fun setByteArray(index: Int, value: ByteArray): ListTag

    public abstract fun setIntArray(index: Int, value: IntArray): ListTag

    public abstract fun setLongArray(index: Int, value: LongArray): ListTag

    public abstract fun setBytes(index: Int, vararg values: Byte): ListTag

    public abstract fun setInts(index: Int, vararg values: Int): ListTag

    public abstract fun setLongs(index: Int, vararg values: Long): ListTag

    /**
     * Iterates over every tag in this list, and for every tag that is a
     * [ByteTag], applies the given [action] to the value.
     *
     * @param action the action to apply to every byte value
     */
    public inline fun forEachByte(action: (Byte) -> Unit) {
        for (i in indices) {
            action(getByte(i))
        }
    }

    /**
     * Iterates over every tag in this list, and for every tag that is a
     * [ShortTag], applies the given [action] to the value.
     *
     * @param action the action to apply to every short value
     */
    public inline fun forEachShort(action: (Short) -> Unit) {
        for (i in indices) {
            action(getShort(i))
        }
    }

    /**
     * Iterates over every tag in this list, and for every tag that is an
     * [IntTag], applies the given [action] to the value.
     *
     * @param action the action to apply to every integer value
     */
    public inline fun forEachInt(action: (Int) -> Unit) {
        for (i in indices) {
            action(getInt(i))
        }
    }

    /**
     * Iterates over every tag in this list, and for every tag that is a
     * [LongTag], applies the given [action] to the value.
     *
     * @param action the action to apply to every long value
     */
    public inline fun forEachLong(action: (Long) -> Unit) {
        for (i in indices) {
            action(getLong(i))
        }
    }

    /**
     * Iterates over every tag in this list, and for every tag that is a
     * [FloatTag], applies the given [action] to the value.
     *
     * @param action the action to apply to every float value
     */
    public inline fun forEachFloat(action: (Float) -> Unit) {
        for (i in indices) {
            action(getFloat(i))
        }
    }

    /**
     * Iterates over every tag in this list, and for every tag that is a
     * [DoubleTag], applies the given [action] to the value.
     *
     * @param action the action to apply to every double value
     */
    public inline fun forEachDouble(action: (Double) -> Unit) {
        for (i in indices) {
            action(getDouble(i))
        }
    }

    /**
     * Iterates over every tag in this list, and for every tag that is a
     * [StringTag], applies the given [action] to the value.
     *
     * @param action the action to apply to every string value
     */
    public inline fun forEachString(action: (String) -> Unit) {
        for (i in indices) {
            action(getString(i))
        }
    }

    /**
     * Iterates over every tag in this list, and for every tag that is a
     * [ByteArrayTag], applies the given [action] to the array.
     *
     * @param action the action to apply to every byte array
     */
    public inline fun forEachByteArray(action: (ByteArray) -> Unit) {
        for (i in indices) {
            action(getByteArray(i))
        }
    }

    /**
     * Iterates over every tag in this list, and for every tag that is an
     * [IntArrayTag], applies the given [action] to the array.
     *
     * @param action the action to apply to every integer array
     */
    public inline fun forEachIntArray(action: (IntArray) -> Unit) {
        for (i in indices) {
            action(getIntArray(i))
        }
    }

    /**
     * Iterates over every tag in this list, and for every tag that is a
     * [LongArrayTag], applies the given [action] to the array.
     *
     * @param action the action to apply to every long array
     */
    public inline fun forEachLongArray(action: (LongArray) -> Unit) {
        for (i in indices) {
            action(getLongArray(i))
        }
    }

    /**
     * Iterates over every tag in this list, and for every tag that is a
     * [ListTag], applies the given [action] to the list.
     *
     * @param action the action to apply to every list
     */
    public inline fun forEachList(action: (ListTag) -> Unit) {
        for (i in indices) {
            action(getList(i))
        }
    }

    /**
     * Iterates over every tag in this list, and for every tag that is a
     * [CompoundTag], applies the given [action] to the compound.
     *
     * @param action the action to apply to every compound
     */
    public inline fun forEachCompound(action: (CompoundTag) -> Unit) {
        for (i in indices) {
            action(getCompound(i))
        }
    }

    final override fun iterator(): Iterator<Tag> = data.iterator()

    final override fun indexOf(element: Tag): Int = data.indexOf(element)

    final override fun lastIndexOf(element: Tag): Int = data.lastIndexOf(element)

    final override fun listIterator(): ListIterator<Tag> = data.listIterator()

    final override fun listIterator(index: Int): ListIterator<Tag> = data.listIterator(index)

    final override fun subList(fromIndex: Int, toIndex: Int): List<Tag> = data.subList(fromIndex, toIndex)

    final override fun isEmpty(): Boolean = data.isEmpty()

    /**
     * Converts this tag to its mutable list equivalent.
     *
     * If this tag is already a mutable tag, this will simply return itself.
     */
    public fun mutable(): MutableListTag {
        if (this is MutableListTag) return this
        val newData = if (data is MutableList) data as MutableList else data.toMutableList()
        return MutableListTag(newData, elementType)
    }

    /**
     * Converts this tag to its immutable list equivalent.
     *
     * If this tag is already an immutable tag, this will simply return itself.
     */
    public fun immutable(): ImmutableListTag {
        if (this is ImmutableListTag) return this
        return ImmutableListTag(data.toPersistentList(), elementType)
    }

    final override fun write(output: BufferedSink) {
        WRITER.write(output, this)
    }

    final override fun <T> examine(examiner: TagExaminer<T>): Unit = examiner.examineList(this)

    public abstract override fun copy(): ListTag

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ListTag) return false
        return data == other.data
    }

    final override fun hashCode(): Int = data.hashCode()

    final override fun toString(): String = "ListTag(data=$data)"

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
        public fun builder(elementType: Int = EndTag.ID, mutable: Boolean = false): Builder = Builder(mutable, elementType)

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
        public fun immutable(data: List<Tag>, elementType: Int): ListTag = ImmutableListTag(data.toPersistentList(), elementType)

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
