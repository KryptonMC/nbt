/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

import org.kryptonmc.nbt.util.UUID
import org.kryptonmc.nbt.util.toTag

public class MutableCompoundTag(
    override val tags: MutableMap<String, Tag> = mutableMapOf()
) : CompoundTag(tags), MutableMap<String, Tag> by tags {

    override val size: Int
        get() = tags.size
    override val entries: MutableSet<MutableMap.MutableEntry<String, Tag>>
        get() = tags.entries
    override val keys: MutableSet<String>
        get() = tags.keys
    override val values: MutableCollection<Tag>
        get() = tags.values

    public fun update(key: String, builder: MutableCompoundTag.() -> Unit): MutableCompoundTag =
        put(key, (getCompound(key) as MutableCompoundTag).apply(builder))

    public fun updateList(key: String, type: Int, builder: MutableListTag.() -> Unit): MutableCompoundTag =
        put(key, (getList(key, type) as MutableListTag).apply(builder))

    override fun copy(): MutableCompoundTag {
        val copy = tags.mapValuesTo(mutableMapOf()) { it.value.copy() }
        return MutableCompoundTag(copy)
    }

    override fun get(key: String): Tag? = tags[key]

    override fun put(key: String, value: Tag): MutableCompoundTag = apply { tags[key] = value }

    override fun putBoolean(key: String, value: Boolean): MutableCompoundTag = put(key, ByteTag.of(value))

    override fun putByte(key: String, value: Byte): MutableCompoundTag = put(key, ByteTag.of(value))

    override fun putShort(key: String, value: Short): MutableCompoundTag = put(key, ShortTag.of(value))

    override fun putInt(key: String, value: Int): MutableCompoundTag = put(key, IntTag.of(value))

    override fun putLong(key: String, value: Long): MutableCompoundTag = put(key, LongTag.of(value))

    override fun putFloat(key: String, value: Float): MutableCompoundTag = put(key, FloatTag.of(value))

    override fun putDouble(key: String, value: Double): MutableCompoundTag = put(key, DoubleTag.of(value))

    override fun putString(key: String, value: String): MutableCompoundTag = put(key, StringTag.of(value))

    override fun putUUID(key: String, value: UUID): MutableCompoundTag = put(key, value.toTag())

    override fun putByteArray(key: String, value: ByteArray): MutableCompoundTag = put(key, ByteArrayTag(value))

    override fun putIntArray(key: String, value: IntArray): MutableCompoundTag = put(key, IntArrayTag(value))

    override fun putLongArray(key: String, value: LongArray): MutableCompoundTag = put(key, LongArrayTag(value))

    override fun putBytes(key: String, vararg values: Byte): MutableCompoundTag = putByteArray(key, values)

    override fun putInts(key: String, vararg values: Int): MutableCompoundTag = putIntArray(key, values)

    override fun putLongs(key: String, vararg values: Long): MutableCompoundTag = putLongArray(key, values)

    override fun containsKey(key: String): Boolean = tags.containsKey(key)

    override fun containsValue(value: Tag): Boolean = tags.containsValue(value)

    override fun isEmpty(): Boolean = tags.isEmpty()
}
