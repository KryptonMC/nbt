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
import okio.utf8Size
import org.kryptonmc.nbt.io.TagReader
import org.kryptonmc.nbt.io.TagWriter
import org.kryptonmc.nbt.io.Types
import org.kryptonmc.nbt.util.UUID
import org.kryptonmc.nbt.util.toTag
import org.kryptonmc.nbt.util.toUUID
import kotlin.jvm.JvmField
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

/**
 * A tag that holds a map of keys to tags.
 *
 * @param tags the tags being held by this compound
 */
public sealed class CompoundTag(public open val tags: Map<String, Tag> = emptyMap()) : Tag, Map<String, Tag> by tags {

    final override val id: Int = ID
    final override val type: TagType = TYPE

    public fun type(name: String): Int = tags[name]?.id ?: 0

    /**
     * Checks if this compound contains a tag with the given [name], and that
     * the retrieved tag is of the given expected type with ID [typeId].
     *
     * @param name the name of the tag
     * @param typeId the ID of the expected tag type
     * @return true if this compound contains a tag with the given name and of
     * the given type, false otherwise
     */
    public fun contains(name: String, typeId: Int): Boolean {
        val type = type(name)
        if (type == typeId) return true
        if (typeId != 99) return false
        return type == ByteTag.ID || type == ShortTag.ID || type == IntTag.ID || type == LongTag.ID ||
            type == FloatTag.ID || type == DoubleTag.ID
    }

    /**
     * Checks if this compound contains a UUID value with the given [name].
     *
     * UUIDs are represented by Mojang as an integer array with four values.
     * The first two elements of the array should be the least significant bits
     * split in two 32 bit parts, and the same applies to the most significant
     * bits in the last two elements.
     *
     * @param name the name of the tag
     * @return true if this compound contains a UUID with the name, false
     * otherwise
     */
    public fun hasUUID(name: String): Boolean {
        val value = tags[name] ?: return false
        return value.type === IntArrayTag.TYPE && (value as IntArrayTag).data.size == 4
    }

    /**
     * Gets the boolean value with the given [name], or returns the given
     * [default] value if there is no boolean value with the given [name].
     *
     * @param name the name of the tag
     * @return the boolean value, or the default if not present
     */
    @JvmOverloads
    public fun getBoolean(name: String, default: Boolean = false): Boolean {
        val byte = getNumber(name)?.toByte() ?: return default
        return byte != 0.toByte()
    }

    /**
     * Gets the byte value with the given [name], or returns the given
     * [default] value if there is no byte value with the given [name].
     *
     * @param name the name of the tag
     * @return the byte value, or the default if not present
     */
    @JvmOverloads
    public fun getByte(name: String, default: Byte = 0): Byte = getNumber(name)?.toByte() ?: default

    /**
     * Gets the short value with the given [name], or returns the given
     * [default] value if there is no short value with the given [name].
     *
     * @param name the name of the tag
     * @return the short value, or the default if not present
     */
    @JvmOverloads
    public fun getShort(name: String, default: Short = 0): Short = getNumber(name)?.toShort() ?: default

    /**
     * Gets the integer value with the given [name], or returns the given
     * [default] value if there is no integer value with the given [name].
     *
     * @param name the name of the tag
     * @return the integer value, or the default if not present
     */
    @JvmOverloads
    public fun getInt(name: String, default: Int = 0): Int = getNumber(name)?.toInt() ?: default

    /**
     * Gets the long value with the given [name], or returns the given
     * [default] value if there is no long value with the given [name].
     *
     * @param name the name of the tag
     * @return the long value, or the default if not present
     */
    @JvmOverloads
    public fun getLong(name: String, default: Long = 0): Long = getNumber(name)?.toLong() ?: default

    /**
     * Gets the float value with the given [name], or returns the given
     * [default] value if there is no float value with the given [name].
     *
     * @param name the name of the tag
     * @return the float value, or the default if not present
     */
    @JvmOverloads
    public fun getFloat(name: String, default: Float = 0F): Float = getNumber(name)?.toFloat() ?: default

    /**
     * Gets the double value with the given [name], or returns the given
     * [default] value if there is no double value with the given [name].
     *
     * @param name the name of the tag
     * @return the double value, or the default if not present
     */
    @JvmOverloads
    public fun getDouble(name: String, default: Double = 0.0): Double = getNumber(name)?.toDouble() ?: default

    /**
     * Gets the string value with the given [name], or returns the given
     * [default] value if there is no string value with the given [name].
     *
     * @param name the name of the tag
     * @return the string value, or the default if not present
     */
    @JvmOverloads
    public fun getString(name: String, default: String = ""): String {
        if (contains(name, StringTag.ID)) return (tags[name] as StringTag).value
        return default
    }

    /**
     * Gets the UUID value with the given [name], or returns the given
     * [default] value if there is no UUID value with the given [name].
     *
     * How a UUID is defined is described by [hasUUID].
     *
     * @param name the name of the tag
     * @return the UUID value, or the default if not present
     */
    @JvmOverloads
    public fun getUUID(name: String, default: UUID? = null): UUID? = get(name)?.toUUID() ?: default

    /**
     * Gets the byte array with the given [name], or returns the given
     * [default] value if there is no byte array with the given [name].
     *
     * @param name the name of the tag
     * @return the byte array, or the default if not present
     */
    @JvmOverloads
    public fun getByteArray(name: String, default: ByteArray = ByteArray(0)): ByteArray {
        if (contains(name, ByteArrayTag.ID)) return (tags[name] as ByteArrayTag).data
        return default
    }

    /**
     * Gets the integer array with the given [name], or returns the given
     * [default] value if there is no integer array with the given [name].
     *
     * @param name the name of the tag
     * @return the integer array, or the default if not present
     */
    @JvmOverloads
    public fun getIntArray(name: String, default: IntArray = IntArray(0)): IntArray {
        if (contains(name, IntArrayTag.ID)) return (tags[name] as IntArrayTag).data
        return default
    }

    /**
     * Gets the long array with the given [name], or returns the given
     * [default] value if there is no long array with the given [name].
     *
     * @param name the name of the tag
     * @return the long array, or the default if not present
     */
    @JvmOverloads
    public fun getLongArray(name: String, default: LongArray = LongArray(0)): LongArray {
        if (contains(name, LongArrayTag.ID)) return (tags[name] as LongArrayTag).data
        return default
    }

    /**
     * Gets the list with the given [name], and checks if it contains elements
     * of the given [elementType]. If it does, it is returned, else the given
     * [default] value will be returned.
     *
     * @param name the name of the tag
     * @param elementType the type of elements expected to be stored
     * @return the list, or the default if not present
     */
    @JvmOverloads
    public fun getList(name: String, elementType: Int, default: ListTag = ListTag.empty()): ListTag {
        if (type(name) == ListTag.ID) {
            val tag = tags[name] as ListTag
            return if (!tag.isEmpty() && tag.elementType != elementType) default else tag
        }
        return default
    }

    /**
     * Gets the compound with the given [name], or returns the given [default]
     * if there is no compound with the given [name].
     *
     * @param name the name of the compound
     * @return the compound, or the default if not present
     */
    @JvmOverloads
    public fun getCompound(name: String, default: CompoundTag = empty()): CompoundTag {
        if (contains(name, ID)) return tags[name] as CompoundTag
        return default
    }

    /**
     * Sets the given [key] in this compound to the given [value] and returns
     * the resulting compound.
     *
     * If this compound is [mutable][MutableCompoundTag], the content will be
     * updated, and the returned compound will be this.
     * If this compound is [immutable][ImmutableCompoundTag], a new immutable
     * copy will be made with the given [key] set to the given [value], and
     * that copy will be returned.
     *
     * @param key the key
     * @param value the value
     * @return the resulting compound
     */
    public abstract fun put(key: String, value: Tag): CompoundTag

    /**
     * Removes the given [key] from this compound and returns the resulting
     * compound.
     *
     * If this compound is [mutable][MutableCompoundTag], the tag will be
     * removed, and the returned compound will be this.
     * If this compound is [immutable][ImmutableCompoundTag], a new immutable
     * copy will be made with the given [key] removed, and that copy will be
     * returned.
     *
     * @param key the key
     * @return the resulting compound
     */
    public abstract fun remove(key: String): CompoundTag

    /**
     * Sets the given [key] in this compound to the given boolean [value] and
     * returns the resulting compound.
     *
     * @param key the key
     * @param value the value
     * @return the resulting compound
     * @see put
     */
    public open fun putBoolean(key: String, value: Boolean): CompoundTag = put(key, ByteTag.of(value))

    /**
     * Sets the given [key] in this compound to the given byte [value] and
     * returns the resulting compound.
     *
     * @param key the key
     * @param value the value
     * @return the resulting compound
     * @see put
     */
    public open fun putByte(key: String, value: Byte): CompoundTag = put(key, ByteTag.of(value))

    /**
     * Sets the given [key] in this compound to the given short [value] and
     * returns the resulting compound.
     *
     * @param key the key
     * @param value the value
     * @return the resulting compound
     * @see put
     */
    public open fun putShort(key: String, value: Short): CompoundTag = put(key, ShortTag.of(value))

    /**
     * Sets the given [key] in this compound to the given integer [value] and
     * returns the resulting compound.
     *
     * @param key the key
     * @param value the value
     * @return the resulting compound
     * @see put
     */
    public open fun putInt(key: String, value: Int): CompoundTag = put(key, IntTag.of(value))

    /**
     * Sets the given [key] in this compound to the given long [value] and
     * returns the resulting compound.
     *
     * @param key the key
     * @param value the value
     * @return the resulting compound
     * @see put
     */
    public open fun putLong(key: String, value: Long): CompoundTag = put(key, LongTag.of(value))

    /**
     * Sets the given [key] in this compound to the given float [value] and
     * returns the resulting compound.
     *
     * @param key the key
     * @param value the value
     * @return the resulting compound
     * @see put
     */
    public open fun putFloat(key: String, value: Float): CompoundTag = put(key, FloatTag.of(value))

    /**
     * Sets the given [key] in this compound to the given double [value] and
     * returns the resulting compound.
     *
     * @param key the key
     * @param value the value
     * @return the resulting compound
     * @see put
     */
    public open fun putDouble(key: String, value: Double): CompoundTag = put(key, DoubleTag.of(value))

    /**
     * Sets the given [key] in this compound to the given string [value] and
     * returns the resulting compound.
     *
     * @param key the key
     * @param value the value
     * @return the resulting compound
     * @see put
     */
    public open fun putString(key: String, value: String): CompoundTag = put(key, StringTag.of(value))

    /**
     * Sets the given [key] in this compound to the given UUID [value] and
     * returns the resulting compound.
     *
     * How UUIDs are stored is described by [hasUUID].
     *
     * @param key the key
     * @param value the value
     * @return the resulting compound
     * @see put
     */
    public open fun putUUID(key: String, value: UUID): CompoundTag = put(key, value.toTag())

    /**
     * Sets the given [key] in this compound to the given byte array [value]
     * and returns the resulting compound.
     *
     * @param key the key
     * @param value the value
     * @return the resulting compound
     * @see put
     */
    public open fun putByteArray(key: String, value: ByteArray): CompoundTag = put(key, ByteArrayTag(value))

    /**
     * Sets the given [key] in this compound to the given integer array [value]
     * and returns the resulting compound.
     *
     * @param key the key
     * @param value the value
     * @return the resulting compound
     * @see put
     */
    public open fun putIntArray(key: String, value: IntArray): CompoundTag = put(key, IntArrayTag(value))

    /**
     * Sets the given [key] in this compound to the given long array [value]
     * and returns the resulting compound.
     *
     * @param key the key
     * @param value the value
     * @return the resulting compound
     * @see put
     */
    public open fun putLongArray(key: String, value: LongArray): CompoundTag = put(key, LongArrayTag(value))

    /**
     * Sets the given [key] in this compound to the given byte [values] and
     * returns the resulting compound.
     *
     * @param key the key
     * @param values the values
     * @return the resulting compound
     * @see put
     */
    public open fun putBytes(key: String, vararg values: Byte): CompoundTag = putByteArray(key, values)

    /**
     * Sets the given [key] in this compound to the given integer [values] and
     * returns the resulting compound.
     *
     * @param key the key
     * @param values the values
     * @return the resulting compound
     * @see put
     */
    public open fun putInts(key: String, vararg values: Int): CompoundTag = putIntArray(key, values)

    /**
     * Sets the given [key] in this compound to the given long [values] and
     * returns the resulting compound.
     *
     * @param key the key
     * @param values the values
     * @return the resulting compound
     * @see put
     */
    public open fun putLongs(key: String, vararg values: Long): CompoundTag = putLongArray(key, values)

    public open fun update(key: String, builder: CompoundTag.() -> Unit): CompoundTag = put(key, getCompound(key).apply(builder))

    public open fun update(key: String, type: Int, builder: ListTag.() -> Unit): CompoundTag = put(key, getList(key, type).apply(builder))

    /**
     * Iterates over every tag in this compound, and for every tag that is a
     * [ByteTag], applies the given [action] to the key and the value.
     *
     * @param action the action to apply to every byte entry
     */
    public inline fun forEachByte(action: (String, Byte) -> Unit) {
        for ((key, value) in this) {
            if (value !is ByteTag) continue
            action(key, value.value)
        }
    }

    /**
     * Iterates over every tag in this compound, and for every tag that is a
     * [ShortTag], applies the given [action] to the key and the value.
     *
     * @param action the action to apply to every short entry
     */
    public inline fun forEachShort(action: (String, Short) -> Unit) {
        for ((key, value) in this) {
            if (value !is ShortTag) continue
            action(key, value.value)
        }
    }

    /**
     * Iterates over every tag in this compound, and for every tag that is a
     * [IntTag], applies the given [action] to the key and the value.
     *
     * @param action the action to apply to every integer entry
     */
    public inline fun forEachInt(action: (String, Int) -> Unit) {
        for ((key, value) in this) {
            if (value !is IntTag) continue
            action(key, value.value)
        }
    }

    /**
     * Iterates over every tag in this compound, and for every tag that is a
     * [LongTag], applies the given [action] to the key and the value.
     *
     * @param action the action to apply to every long entry
     */
    public inline fun forEachLong(action: (String, Long) -> Unit) {
        for ((key, value) in this) {
            if (value !is LongTag) continue
            action(key, value.value)
        }
    }

    /**
     * Iterates over every tag in this compound, and for every tag that is a
     * [FloatTag], applies the given [action] to the key and the value.
     *
     * @param action the action to apply to every float entry
     */
    public inline fun forEachFloat(action: (String, Float) -> Unit) {
        for ((key, value) in this) {
            if (value !is FloatTag) continue
            action(key, value.value)
        }
    }

    /**
     * Iterates over every tag in this compound, and for every tag that is a
     * [DoubleTag], applies the given [action] to the key and the value.
     *
     * @param action the action to apply to every double entry
     */
    public inline fun forEachDouble(action: (String, Double) -> Unit) {
        for ((key, value) in this) {
            if (value !is DoubleTag) continue
            action(key, value.value)
        }
    }

    /**
     * Iterates over every tag in this compound, and for every tag that is a
     * [StringTag], applies the given [action] to the key and the value.
     *
     * @param action the action to apply to every string entry
     */
    public inline fun forEachString(action: (String, String) -> Unit) {
        for ((key, value) in this) {
            if (value !is StringTag) continue
            action(key, value.value)
        }
    }

    /**
     * Iterates over every tag in this compound, and for every tag that is a
     * [ByteArrayTag], applies the given [action] to the key and the value.
     *
     * @param action the action to apply to every byte array entry
     */
    public inline fun forEachByteArray(action: (String, ByteArray) -> Unit) {
        for ((key, value) in this) {
            if (value !is ByteArrayTag) continue
            action(key, value.data)
        }
    }

    /**
     * Iterates over every tag in this compound, and for every tag that is a
     * [IntArrayTag], applies the given [action] to the key and the value.
     *
     * @param action the action to apply to every integer array entry
     */
    public inline fun forEachIntArray(action: (String, IntArray) -> Unit) {
        for ((key, value) in this) {
            if (value !is IntArrayTag) continue
            action(key, value.data)
        }
    }

    /**
     * Iterates over every tag in this compound, and for every tag that is a
     * [LongArrayTag], applies the given [action] to the key and the value.
     *
     * @param action the action to apply to every long array entry
     */
    public inline fun forEachLongArray(action: (String, LongArray) -> Unit) {
        for ((key, value) in this) {
            if (value !is LongArrayTag) continue
            action(key, value.data)
        }
    }

    /**
     * Iterates over every tag in this compound, and for every tag that is a
     * [ListTag], applies the given [action] to the key and the value.
     *
     * @param action the action to apply to every list entry
     */
    public inline fun forEachList(action: (String, ListTag) -> Unit) {
        for ((key, value) in this) {
            if (value !is ListTag) continue
            action(key, value)
        }
    }

    /**
     * Iterates over every tag in this compound, and for every tag that is a
     * [CompoundTag], applies the given [action] to the key and the value.
     *
     * @param action the action to apply to every compound entry
     */
    public inline fun forEachCompound(action: (String, CompoundTag) -> Unit) {
        for ((key, value) in this) {
            if (value !is CompoundTag) continue
            action(key, value)
        }
    }

    /**
     * Converts this tag to its mutable compound equivalent.
     *
     * If this tag is already a mutable tag, this will simply return itself.
     */
    public fun mutable(): MutableCompoundTag {
        if (this is MutableCompoundTag) return this
        val newTags = if (tags is MutableMap) tags as MutableMap else tags.toMutableMap()
        return MutableCompoundTag(newTags)
    }

    /**
     * Converts this tag to its immutable compound equivalent.
     *
     * If this tag is already an immutable tag, this will simply return itself.
     */
    public fun immutable(): ImmutableCompoundTag {
        if (this is ImmutableCompoundTag) return this
        return ImmutableCompoundTag(tags)
    }

    final override fun write(output: BufferedSink) {
        WRITER.write(output, this)
    }

    final override fun <T> examine(examiner: TagExaminer<T>): Unit = examiner.examineCompound(this)

    public abstract override fun copy(): CompoundTag

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CompoundTag) return false
        return tags == other.tags
    }

    final override fun hashCode(): Int = tags.hashCode()

    final override fun toString(): String = asString()

    private fun getNumber(name: String): NumberTag? {
        if (contains(name, 99)) return tags[name] as? NumberTag
        return null
    }

    /**
     * A builder for building compound tags.
     */
    public class Builder internal constructor(private val mutable: Boolean) {

        private val tags = mutableMapOf<String, Tag>()

        /**
         * Sets the value of the given [name] to the given [value].
         *
         * @param name the name
         * @param value the value
         * @return this builder
         */
        public fun put(name: String, value: Tag): Builder = apply { tags[name] = value }

        /**
         * Sets the value of the given [name] to the given byte [value].
         *
         * @param name the name
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        @JvmName("putByte")
        public fun byte(name: String, value: Byte): Builder = apply { put(name, ByteTag.of(value)) }

        /**
         * Sets the value of the given [name] to the given boolean [value].
         *
         * @param name the name
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        @JvmName("putBoolean")
        public fun boolean(name: String, value: Boolean): Builder = apply { put(name, ByteTag.of(value)) }

        /**
         * Sets the value of the given [name] to the given short [value].
         *
         * @param name the name
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        @JvmName("putShort")
        public fun short(name: String, value: Short): Builder = apply { put(name, ShortTag.of(value)) }

        /**
         * Sets the value of the given [name] to the given integer [value].
         *
         * @param name the name
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        @JvmName("putInt")
        public fun int(name: String, value: Int): Builder = apply { put(name, IntTag.of(value)) }

        /**
         * Sets the value of the given [name] to the given long [value].
         *
         * @param name the name
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        @JvmName("putLong")
        public fun long(name: String, value: Long): Builder = apply { put(name, LongTag.of(value)) }

        /**
         * Sets the value of the given [name] to the given float [value].
         *
         * @param name the name
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        @JvmName("putFloat")
        public fun float(name: String, value: Float): Builder = apply { put(name, FloatTag.of(value)) }

        /**
         * Sets the value of the given [name] to the given double [value].
         *
         * @param name the name
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        @JvmName("putDouble")
        public fun double(name: String, value: Double): Builder = apply { put(name, DoubleTag.of(value)) }

        /**
         * Sets the value of the given [name] to the given string [value].
         *
         * @param name the name
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        @JvmName("putString")
        public fun string(name: String, value: String): Builder = apply { put(name, StringTag.of(value)) }

        /**
         * Sets the value of the given [name] to the given UUID [value].
         *
         * How UUIDs are stored is explained by [CompoundTag.hasUUID].
         *
         * @param name the name
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        @JvmName("putUUID")
        public fun uuid(name: String, value: UUID): Builder = apply { put(name, value.toTag()) }

        /**
         * Sets the value of the given [name] to the given byte array [value].
         *
         * @param name the name
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        @JvmName("putByteArray")
        public fun byteArray(name: String, value: ByteArray): Builder = apply { put(name, ByteArrayTag(value)) }

        /**
         * Sets the values of the given [name] to the given byte [values].
         *
         * @param name the name
         * @param values the values
         * @return this builder
         */
        @NBTDsl
        @JvmName("putBytes")
        public fun bytes(name: String, vararg values: Byte): Builder = apply { put(name, ByteArrayTag(values)) }

        /**
         * Sets the value of the given [name] to the given integer array
         * [value].
         *
         * @param name the name
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        @JvmName("putIntArray")
        public fun intArray(name: String, value: IntArray): Builder = apply { put(name, IntArrayTag(value)) }

        /**
         * Sets the value of the given [name] to the given integer [values].
         *
         * @param name the name
         * @param values the values
         * @return this builder
         */
        @NBTDsl
        @JvmName("putInts")
        public fun ints(name: String, vararg values: Int): Builder = apply { put(name, IntArrayTag(values)) }

        /**
         * Sets the value of the given [name] to the given long array [value].
         *
         * @param name the name
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        @JvmName("putLongArray")
        public fun longArray(name: String, value: LongArray): Builder = apply { put(name, LongArrayTag(value)) }

        /**
         * Sets the values of the given [name] to the given long [values].
         *
         * @param name the name
         * @param values the values
         * @return this builder
         */
        @NBTDsl
        @JvmName("putLongs")
        public fun longs(name: String, vararg values: Long): Builder = apply { put(name, LongArrayTag(values)) }

        /**
         * Sets the value of the given [name] to the result of building a new
         * list from the given [builder].
         *
         * @param name the name
         * @param builder the builder to build the list from
         * @return this builder
         */
        @NBTDsl
        @JvmName("putList")
        public inline fun list(name: String, builder: ListTag.Builder.() -> Unit): Builder = apply {
            put(name, ListTag.builder().apply(builder).build())
        }

        /**
         * Sets the value of the given [name] to the result of creating a new
         * list tag with the given [elementType] and [elements].
         *
         * @param name the name
         * @param elementType the type of elements that the list can store
         * @param elements the elements the list stores
         * @return this builder
         */
        @NBTDsl
        @JvmName("putList")
        public fun list(name: String, elementType: Int, vararg elements: Tag): Builder = apply {
            put(name, MutableListTag(elements.toMutableList(), elementType))
        }

        /**
         * Sets the value of the given [name] to the result of creating a new
         * list tag with the given [elementType] and [elements].
         *
         * @param name the name
         * @param elementType the type of elements that the list can store
         * @param elements the elements the list stores
         * @return this builder
         */
        @NBTDsl
        @JvmName("putList")
        public fun list(name: String, elementType: Int, elements: Collection<Tag>): Builder = apply {
            val list = if (elements is MutableList) elements else elements.toMutableList()
            put(name, MutableListTag(list, elementType))
        }

        /**
         * Sets the value of the given [name] to the result of building a new
         * compound from the given [builder].
         *
         * @param name the name
         * @param builder the builder to build the compound from
         * @return this builder
         */
        @NBTDsl
        @JvmName("putCompound")
        public inline fun compound(name: String, builder: Builder.() -> Unit): Builder = apply {
            put(name, Companion.builder().apply(builder).build())
        }

        /**
         * Creates a new compound tag containing the values set in this
         * builder.
         * Whether the resulting tag is mutable or immutable depends on what
         * was supplied to the builder when the builder functions were called.
         *
         * @return a new compound tag
         */
        public fun build(): CompoundTag = if (mutable) mutable(tags) else immutable(tags)
    }

    public companion object {

        public const val ID: Int = 10
        @JvmField
        public val TYPE: TagType = TagType("TAG_Compound")
        @JvmField
        public val READER: TagReader<CompoundTag> = object : TagReader<CompoundTag> {

            override fun read(input: BufferedSource, depth: Int): CompoundTag {
                if (depth > 512) throw RuntimeException("Depth too high! Given depth $depth is higher than maximum depth 512!")
                val tags = mutableMapOf<String, Tag>()
                var type = input.readByte().toInt()
                while (type != EndTag.ID) {
                    val length = input.readShort()
                    val name = input.readUtf8(length.toLong())
                    val tag = Types.reader(type).read(input, depth + 1)
                    tags[name] = tag
                    type = input.readByte().toInt()
                }
                return MutableCompoundTag(tags)
            }
        }
        @JvmField
        public val WRITER: TagWriter<CompoundTag> = object : TagWriter<CompoundTag> {

            override fun write(output: BufferedSink, value: CompoundTag) {
                value.tags.forEach { output.writeNamedTag(it.key, it.value) }
                output.writeByte(EndTag.ID)
            }
        }
        private val EMPTY = ImmutableCompoundTag(emptyMap())

        /**
         * Creates a new builder for building a compound tag.
         *
         * @param mutable if the resulting built tag will be mutable
         * @return a new builder
         */
        @JvmStatic
        @JvmOverloads
        public fun builder(mutable: Boolean = true): Builder = Builder(mutable)

        @JvmStatic
        @Deprecated("Not all compound tags are mutable any more.", ReplaceWith("CompoundTag.mutable"))
        public fun of(data: Map<String, Tag>): CompoundTag = mutable(data)

        /**
         * Creates a new mutable compound tag with the given [data].
         * If the [data] is not a [MutableMap], it will be converted to one
         * using [Map.toMutableMap].
         *
         * @param data the data
         * @return a new mutable compound tag
         */
        @JvmStatic
        public fun mutable(data: Map<String, Tag>): CompoundTag = MutableCompoundTag(if (data is MutableMap) data else data.toMutableMap())

        /**
         * Creates a new immutable compound tag with the given [data].
         *
         * @param data the data
         * @return a new immutable compound tag
         */
        @JvmStatic
        public fun immutable(data: Map<String, Tag>): CompoundTag = ImmutableCompoundTag(data)

        /**
         * Gets the empty compound tag.
         *
         * This is an immutable compound tag with an empty map backing it,
         * meaning no data can be modified. All attempts to write to this
         * compound will result in a new compound.
         */
        @JvmStatic
        public fun empty(): CompoundTag = EMPTY
    }
}

private fun BufferedSink.writeNamedTag(name: String, tag: Tag) {
    writeByte(tag.id)
    if (tag.id != EndTag.ID) {
        writeShort(name.utf8Size().toInt())
        writeUtf8(name)
        tag.write(this)
    }
}
