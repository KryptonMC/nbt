/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentMap
import org.kryptonmc.nbt.io.TagReader
import org.kryptonmc.nbt.io.TagWriter
import org.kryptonmc.nbt.io.Types
import org.kryptonmc.nbt.util.toTag
import org.kryptonmc.nbt.util.toUUID
import java.io.DataOutput
import java.util.UUID
import java.util.function.BiConsumer
import java.util.function.Consumer

/**
 * A tag that holds a map of keys to tags.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public sealed class CompoundTag : Tag {

    /**
     * The backing map held by this compound tag.
     */
    public abstract val data: Map<String, Tag>

    /**
     * If this compound tag is mutable, meaning writes will modify the internal
     * state.
     */
    public open val isMutable: Boolean
        get() = false

    /**
     * If this compound tag is immutable, meaning writes will create copies of
     * the object with requested changes applied.
     */
    public open val isImmutable: Boolean
        get() = false

    final override val id: Int
        get() = ID
    final override val type: TagType
        get() = TYPE

    public val size: Int
        @JvmName("size") get() = data.size
    public open val keys: Set<String>
        @JvmName("keySet") get() = data.keys
    public open val values: Collection<Tag>
        @JvmName("values") get() = data.values

    public fun type(name: String): Int {
        val tag = data[name] ?: return 0
        return tag.id
    }

    public fun contains(name: String): Boolean = data.containsKey(name)

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
        return type == ByteTag.ID || type == ShortTag.ID || type == IntTag.ID || type == LongTag.ID || type == FloatTag.ID || type == DoubleTag.ID
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
        val value = data[name] ?: return false
        return value.id == IntArrayTag.ID && (value as IntArrayTag).data.size == 4
    }

    /**
     * Gets the tag with the given [key] from this compound, or returns null
     * if there is no tag with the given name in this compound.
     *
     * @param key the key of the tag
     * @return the tag, or null if not present
     */
    public fun get(key: String): Tag? = data[key]

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
        if (contains(name, StringTag.ID)) return (data[name] as StringTag).value
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
    public fun getByteArray(name: String, default: ByteArray = ByteArrayTag.EMPTY_DATA): ByteArray {
        if (contains(name, ByteArrayTag.ID)) return (data[name] as ByteArrayTag).data
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
    public fun getIntArray(name: String, default: IntArray = IntArrayTag.EMPTY_DATA): IntArray {
        if (contains(name, IntArrayTag.ID)) return (data[name] as IntArrayTag).data
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
    public fun getLongArray(name: String, default: LongArray = LongArrayTag.EMPTY_DATA): LongArray {
        if (contains(name, LongArrayTag.ID)) return (data[name] as LongArrayTag).data
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
            val tag = data[name] as ListTag
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
        if (contains(name, ID)) return data[name] as CompoundTag
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
    public abstract fun putBoolean(key: String, value: Boolean): CompoundTag

    /**
     * Sets the given [key] in this compound to the given byte [value] and
     * returns the resulting compound.
     *
     * @param key the key
     * @param value the value
     * @return the resulting compound
     * @see put
     */
    public abstract fun putByte(key: String, value: Byte): CompoundTag

    /**
     * Sets the given [key] in this compound to the given short [value] and
     * returns the resulting compound.
     *
     * @param key the key
     * @param value the value
     * @return the resulting compound
     * @see put
     */
    public abstract fun putShort(key: String, value: Short): CompoundTag

    /**
     * Sets the given [key] in this compound to the given integer [value] and
     * returns the resulting compound.
     *
     * @param key the key
     * @param value the value
     * @return the resulting compound
     * @see put
     */
    public abstract fun putInt(key: String, value: Int): CompoundTag

    /**
     * Sets the given [key] in this compound to the given long [value] and
     * returns the resulting compound.
     *
     * @param key the key
     * @param value the value
     * @return the resulting compound
     * @see put
     */
    public abstract fun putLong(key: String, value: Long): CompoundTag

    /**
     * Sets the given [key] in this compound to the given float [value] and
     * returns the resulting compound.
     *
     * @param key the key
     * @param value the value
     * @return the resulting compound
     * @see put
     */
    public abstract fun putFloat(key: String, value: Float): CompoundTag

    /**
     * Sets the given [key] in this compound to the given double [value] and
     * returns the resulting compound.
     *
     * @param key the key
     * @param value the value
     * @return the resulting compound
     * @see put
     */
    public abstract fun putDouble(key: String, value: Double): CompoundTag

    /**
     * Sets the given [key] in this compound to the given string [value] and
     * returns the resulting compound.
     *
     * @param key the key
     * @param value the value
     * @return the resulting compound
     * @see put
     */
    public abstract fun putString(key: String, value: String): CompoundTag

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
    public abstract fun putUUID(key: String, value: UUID): CompoundTag

    /**
     * Sets the given [key] in this compound to the given byte array [value]
     * and returns the resulting compound.
     *
     * @param key the key
     * @param value the value
     * @return the resulting compound
     * @see put
     */
    public abstract fun putByteArray(key: String, value: ByteArray): CompoundTag

    /**
     * Sets the given [key] in this compound to the given integer array [value]
     * and returns the resulting compound.
     *
     * @param key the key
     * @param value the value
     * @return the resulting compound
     * @see put
     */
    public abstract fun putIntArray(key: String, value: IntArray): CompoundTag

    /**
     * Sets the given [key] in this compound to the given long array [value]
     * and returns the resulting compound.
     *
     * @param key the key
     * @param value the value
     * @return the resulting compound
     * @see put
     */
    public abstract fun putLongArray(key: String, value: LongArray): CompoundTag

    /**
     * Sets the given [key] in this compound to the given byte [values] and
     * returns the resulting compound.
     *
     * @param key the key
     * @param values the values
     * @return the resulting compound
     * @see put
     */
    public abstract fun putBytes(key: String, vararg values: Byte): CompoundTag

    /**
     * Sets the given [key] in this compound to the given integer [values] and
     * returns the resulting compound.
     *
     * @param key the key
     * @param values the values
     * @return the resulting compound
     * @see put
     */
    public abstract fun putInts(key: String, vararg values: Int): CompoundTag

    /**
     * Sets the given [key] in this compound to the given long [values] and
     * returns the resulting compound.
     *
     * @param key the key
     * @param values the values
     * @return the resulting compound
     * @see put
     */
    public abstract fun putLongs(key: String, vararg values: Long): CompoundTag

    @JvmSynthetic
    public abstract fun update(key: String, builder: CompoundTag.() -> Unit): CompoundTag

    public fun update(key: String, builder: Consumer<CompoundTag>): CompoundTag = update(key, builder::accept)

    @JvmSynthetic
    public abstract fun update(key: String, type: Int, builder: ListTag.() -> Unit): CompoundTag

    public fun update(key: String, type: Int, builder: Consumer<ListTag>): CompoundTag = update(key, type, builder::accept)

    /**
     * Iterates over every tag in this compound, and for every tag that is a
     * [ByteTag], applies the given [action] to the key and the value.
     *
     * @param action the action to apply to every byte entry
     */
    @JvmSynthetic
    public inline fun forEachByte(action: (String, Byte) -> Unit) {
        for ((key, value) in data) {
            if (value !is ByteTag) continue
            action(key, value.value)
        }
    }

    public fun forEachByte(action: BiConsumer<String, Byte>) {
        forEachByte(action::accept)
    }

    /**
     * Iterates over every tag in this compound, and for every tag that is a
     * [ShortTag], applies the given [action] to the key and the value.
     *
     * @param action the action to apply to every short entry
     */
    @JvmSynthetic
    public inline fun forEachShort(action: (String, Short) -> Unit) {
        for ((key, value) in data) {
            if (value !is ShortTag) continue
            action(key, value.value)
        }
    }

    public fun forEachShort(action: BiConsumer<String, Short>) {
        forEachShort(action::accept)
    }

    /**
     * Iterates over every tag in this compound, and for every tag that is a
     * [IntTag], applies the given [action] to the key and the value.
     *
     * @param action the action to apply to every integer entry
     */
    @JvmSynthetic
    public inline fun forEachInt(action: (String, Int) -> Unit) {
        for ((key, value) in data) {
            if (value !is IntTag) continue
            action(key, value.value)
        }
    }

    public fun forEachInt(action: BiConsumer<String, Int>) {
        forEachInt(action::accept)
    }

    /**
     * Iterates over every tag in this compound, and for every tag that is a
     * [LongTag], applies the given [action] to the key and the value.
     *
     * @param action the action to apply to every long entry
     */
    @JvmSynthetic
    public inline fun forEachLong(action: (String, Long) -> Unit) {
        for ((key, value) in data) {
            if (value !is LongTag) continue
            action(key, value.value)
        }
    }

    public fun forEachLong(action: BiConsumer<String, Long>) {
        forEachLong(action::accept)
    }

    /**
     * Iterates over every tag in this compound, and for every tag that is a
     * [FloatTag], applies the given [action] to the key and the value.
     *
     * @param action the action to apply to every float entry
     */
    @JvmSynthetic
    public inline fun forEachFloat(action: (String, Float) -> Unit) {
        for ((key, value) in data) {
            if (value !is FloatTag) continue
            action(key, value.value)
        }
    }

    public fun forEachFloat(action: BiConsumer<String, Float>) {
        forEachFloat(action::accept)
    }

    /**
     * Iterates over every tag in this compound, and for every tag that is a
     * [DoubleTag], applies the given [action] to the key and the value.
     *
     * @param action the action to apply to every double entry
     */
    @JvmSynthetic
    public inline fun forEachDouble(action: (String, Double) -> Unit) {
        for ((key, value) in data) {
            if (value !is DoubleTag) continue
            action(key, value.value)
        }
    }

    public fun forEachDouble(action: BiConsumer<String, Double>) {
        forEachDouble(action::accept)
    }

    /**
     * Iterates over every tag in this compound, and for every tag that is a
     * [StringTag], applies the given [action] to the key and the value.
     *
     * @param action the action to apply to every string entry
     */
    @JvmSynthetic
    public inline fun forEachString(action: (String, String) -> Unit) {
        for ((key, value) in data) {
            if (value !is StringTag) continue
            action(key, value.value)
        }
    }

    public fun forEachString(action: BiConsumer<String, String>) {
        forEachString(action::accept)
    }

    /**
     * Iterates over every tag in this compound, and for every tag that is a
     * [ByteArrayTag], applies the given [action] to the key and the value.
     *
     * @param action the action to apply to every byte array entry
     */
    @JvmSynthetic
    public inline fun forEachByteArray(action: (String, ByteArray) -> Unit) {
        for ((key, value) in data) {
            if (value !is ByteArrayTag) continue
            action(key, value.data)
        }
    }

    public fun forEachByteArray(action: BiConsumer<String, ByteArray>) {
        forEachByteArray(action::accept)
    }

    /**
     * Iterates over every tag in this compound, and for every tag that is a
     * [IntArrayTag], applies the given [action] to the key and the value.
     *
     * @param action the action to apply to every integer array entry
     */
    @JvmSynthetic
    public inline fun forEachIntArray(action: (String, IntArray) -> Unit) {
        for ((key, value) in data) {
            if (value !is IntArrayTag) continue
            action(key, value.data)
        }
    }

    public fun forEachIntArray(action: BiConsumer<String, IntArray>) {
        forEachIntArray(action::accept)
    }

    /**
     * Iterates over every tag in this compound, and for every tag that is a
     * [LongArrayTag], applies the given [action] to the key and the value.
     *
     * @param action the action to apply to every long array entry
     */
    @JvmSynthetic
    public inline fun forEachLongArray(action: (String, LongArray) -> Unit) {
        for ((key, value) in data) {
            if (value !is LongArrayTag) continue
            action(key, value.data)
        }
    }

    public fun forEachLongArray(action: BiConsumer<String, LongArray>) {
        forEachLongArray(action::accept)
    }

    /**
     * Iterates over every tag in this compound, and for every tag that is a
     * [ListTag], applies the given [action] to the key and the value.
     *
     * @param action the action to apply to every list entry
     */
    @JvmSynthetic
    public inline fun forEachList(action: (String, ListTag) -> Unit) {
        for ((key, value) in data) {
            if (value !is ListTag) continue
            action(key, value)
        }
    }

    public fun forEachList(action: BiConsumer<String, ListTag>) {
        forEachList(action::accept)
    }

    /**
     * Iterates over every tag in this compound, and for every tag that is a
     * [CompoundTag], applies the given [action] to the key and the value.
     *
     * @param action the action to apply to every compound entry
     */
    @JvmSynthetic
    public inline fun forEachCompound(action: (String, CompoundTag) -> Unit) {
        for ((key, value) in data) {
            if (value !is CompoundTag) continue
            action(key, value)
        }
    }

    public fun forEachCompound(action: BiConsumer<String, CompoundTag>) {
        forEachCompound(action::accept)
    }

    /**
     * Converts this tag to its mutable compound equivalent.
     *
     * If this tag is already a mutable tag, this will simply return itself.
     */
    public fun mutable(): MutableCompoundTag {
        if (this is MutableCompoundTag) return this
        val newTags = if (data is MutableMap) data as MutableMap else HashMap(data)
        return MutableCompoundTag(newTags)
    }

    /**
     * Converts this tag to its immutable compound equivalent.
     *
     * If this tag is already an immutable tag, this will simply return itself.
     */
    public fun immutable(): ImmutableCompoundTag {
        if (this is ImmutableCompoundTag) return this
        return ImmutableCompoundTag(data.toPersistentMap())
    }

    public fun isEmpty(): Boolean = data.isEmpty()

    final override fun write(output: DataOutput) {
        WRITER.write(output, this)
    }

    final override fun <T> examine(examiner: TagExaminer<T>) {
        examiner.examineCompound(this)
    }

    public abstract override fun copy(): CompoundTag

    public abstract fun toBuilder(): Builder

    final override fun equals(other: Any?): Boolean = this === other || (other is CompoundTag && data == other.data)

    final override fun hashCode(): Int = data.hashCode()

    final override fun toString(): String = asString()

    private fun getNumber(name: String): NumberTag? {
        if (contains(name, 99)) return data[name] as? NumberTag
        return null
    }

    /**
     * A builder for building compound tags.
     */
    public sealed class Builder(protected open val data: MutableMap<String, Tag>) {

        /**
         * Sets the value of the given [name] to the given [value].
         *
         * @param name the name
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        public fun put(name: String, value: Tag): Builder = apply { data[name] = value }

        /**
         * Sets the value of the given [name] to the given byte [value].
         *
         * @param name the name
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        @JvmName("putByte")
        public fun byte(name: String, value: Byte): Builder = put(name, ByteTag.of(value))

        /**
         * Sets the value of the given [name] to the given boolean [value].
         *
         * @param name the name
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        @JvmName("putBoolean")
        public fun boolean(name: String, value: Boolean): Builder = put(name, ByteTag.of(value))

        /**
         * Sets the value of the given [name] to the given short [value].
         *
         * @param name the name
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        @JvmName("putShort")
        public fun short(name: String, value: Short): Builder = put(name, ShortTag.of(value))

        /**
         * Sets the value of the given [name] to the given integer [value].
         *
         * @param name the name
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        @JvmName("putInt")
        public fun int(name: String, value: Int): Builder = put(name, IntTag.of(value))

        /**
         * Sets the value of the given [name] to the given long [value].
         *
         * @param name the name
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        @JvmName("putLong")
        public fun long(name: String, value: Long): Builder = put(name, LongTag.of(value))

        /**
         * Sets the value of the given [name] to the given float [value].
         *
         * @param name the name
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        @JvmName("putFloat")
        public fun float(name: String, value: Float): Builder = put(name, FloatTag.of(value))

        /**
         * Sets the value of the given [name] to the given double [value].
         *
         * @param name the name
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        @JvmName("putDouble")
        public fun double(name: String, value: Double): Builder = put(name, DoubleTag.of(value))

        /**
         * Sets the value of the given [name] to the given string [value].
         *
         * @param name the name
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        @JvmName("putString")
        public fun string(name: String, value: String): Builder = put(name, StringTag.of(value))

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
        public fun uuid(name: String, value: UUID): Builder = put(name, value.toTag())

        /**
         * Sets the value of the given [name] to the given byte array [value].
         *
         * @param name the name
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        @JvmName("putByteArray")
        public fun byteArray(name: String, value: ByteArray): Builder = put(name, ByteArrayTag(value))

        /**
         * Sets the values of the given [name] to the given byte [values].
         *
         * @param name the name
         * @param values the values
         * @return this builder
         */
        @NBTDsl
        @JvmName("putBytes")
        public fun bytes(name: String, vararg values: Byte): Builder = put(name, ByteArrayTag(values))

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
        public fun intArray(name: String, value: IntArray): Builder = put(name, IntArrayTag(value))

        /**
         * Sets the value of the given [name] to the given integer [values].
         *
         * @param name the name
         * @param values the values
         * @return this builder
         */
        @NBTDsl
        @JvmName("putInts")
        public fun ints(name: String, vararg values: Int): Builder = put(name, IntArrayTag(values))

        /**
         * Sets the value of the given [name] to the given long array [value].
         *
         * @param name the name
         * @param value the value
         * @return this builder
         */
        @NBTDsl
        @JvmName("putLongArray")
        public fun longArray(name: String, value: LongArray): Builder = put(name, LongArrayTag(value))

        /**
         * Sets the values of the given [name] to the given long [values].
         *
         * @param name the name
         * @param values the values
         * @return this builder
         */
        @NBTDsl
        @JvmName("putLongs")
        public fun longs(name: String, vararg values: Long): Builder = put(name, LongArrayTag(values))

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
        @JvmSynthetic
        public inline fun list(name: String, builder: ListTag.Builder.() -> Unit): Builder =
            put(name, ListTag.immutableBuilder().apply(builder).build())

        public fun putList(name: String, builder: Consumer<ListTag.Builder>): Builder = list(name, builder::accept)

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
        public fun list(name: String, elementType: Int, vararg elements: Tag): Builder =
            put(name, ImmutableListTag(elements.asIterable().toPersistentList(), elementType))

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
        public fun list(name: String, elementType: Int, elements: Collection<Tag>): Builder =
            put(name, ImmutableListTag(elements.toPersistentList(), elementType))

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
        @JvmSynthetic
        public inline fun compound(name: String, builder: Builder.() -> Unit): Builder = put(name, immutableBuilder().apply(builder).build())

        public fun putCompound(name: String, builder: Consumer<Builder>): Builder = compound(name, builder::accept)

        /**
         * Removes the value for the given [name].
         *
         * @param name the name
         * @return this builder
         */
        @NBTDsl
        public fun remove(name: String): Builder = apply { data.remove(name) }

        /**
         * Adds all the values from the [other] builder to this builder.
         *
         * This is useful for avoiding the need to create a new tag to merge
         * two tags that are in the process of being built.
         *
         * @param other the other builder to add the tags from
         * @return this builder
         */
        @NBTDsl
        public fun from(other: Builder): Builder = apply { data.putAll(other.data) }

        /**
         * Adds all the values from the [other] tag to this builder.
         *
         * This is a handy shortcut that is equivalent to looping over the
         * other's data and calling `put` on this builder.
         *
         * @param other the other tag to add the tags from
         * @return this builder
         */
        @NBTDsl
        public fun from(other: CompoundTag): Builder = apply { data.putAll(other.data) }

        /**
         * Creates a new compound tag containing the values set in this
         * builder.
         * Whether the resulting tag is mutable or immutable depends on what
         * was supplied to the builder when the builder functions were called.
         *
         * @return a new compound tag
         */
        public abstract fun build(): CompoundTag

        public companion object {

            @JvmStatic
            @JvmSynthetic
            internal fun mutable(tags: MutableMap<String, Tag>): Builder = MutableBuilder(tags)

            @JvmStatic
            @JvmSynthetic
            internal fun immutable(tags: PersistentMap<String, Tag>): Builder = ImmutableBuilder(tags.builder())
        }
    }

    private class ImmutableBuilder(override val data: PersistentMap.Builder<String, Tag>) : Builder(data) {

        override fun build(): CompoundTag = ImmutableCompoundTag(data.build())
    }

    private class MutableBuilder(tags: MutableMap<String, Tag>) : Builder(tags) {

        override fun build(): CompoundTag = MutableCompoundTag(data)
    }

    public companion object {

        public const val ID: Int = 10
        @JvmField
        public val TYPE: TagType = TagType("TAG_Compound")
        @JvmField
        public val READER: TagReader<CompoundTag> = TagReader { input, depth ->
            if (depth > 512) throw RuntimeException("Depth too high! Given depth $depth is higher than maximum depth 512!")
            val tags = mutableMapOf<String, Tag>()
            var type = input.readByte().toInt()
            while (type != EndTag.ID) {
                val name = input.readUTF()
                val tag = Types.reader(type).read(input, depth + 1)
                tags[name] = tag
                type = input.readByte().toInt()
            }
            MutableCompoundTag(tags)
        }
        @JvmField
        public val WRITER: TagWriter<CompoundTag> = TagWriter { output, value ->
            value.data.forEach { (name, tag) ->
                output.writeByte(tag.id)
                if (tag.id == EndTag.ID) return@forEach
                output.writeUTF(name)
                tag.write(output)
            }
            output.writeByte(EndTag.ID)
        }
        private val EMPTY = ImmutableCompoundTag(persistentMapOf())

        /**
         * Creates a new builder for building an immutable compound tag.
         *
         * @return a new immutable builder
         */
        @JvmStatic
        public fun immutableBuilder(): Builder = Builder.immutable(persistentMapOf())

        /**
         * Creates a new builder for building a mutable compound tag.
         *
         * @return a new builder
         */
        @JvmStatic
        public fun mutableBuilder(): Builder = Builder.mutable(mutableMapOf())

        /**
         * Creates a new mutable compound tag with the given [data].
         * If the [data] is not a [MutableMap], it will be converted to one.
         *
         * @param data the data
         * @return a new mutable compound tag
         */
        @JvmStatic
        public fun mutable(data: Map<String, Tag>): CompoundTag {
            // We don't use toMutableMap here because we don't want to clone the map if it's already mutable, which, for whatever reason,
            // toMutableMap always does.
            val newData = if (data is MutableMap) data else HashMap(data)
            return MutableCompoundTag(newData)
        }

        /**
         * Creates a new immutable compound tag with the given [data].
         *
         * @param data the data
         * @return a new immutable compound tag
         */
        @JvmStatic
        public fun immutable(data: Map<String, Tag>): CompoundTag = ImmutableCompoundTag(data.toPersistentMap())

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
