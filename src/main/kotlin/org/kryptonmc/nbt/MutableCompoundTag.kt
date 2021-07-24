/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

import org.kryptonmc.nbt.util.toTag
import java.util.UUID

public class MutableCompoundTag(override val tags: MutableMap<String, Tag> = mutableMapOf()) : CompoundTag(tags), MutableMap<String, Tag> by tags {

    public fun putByte(key: String, value: Byte): MutableCompoundTag = apply { put(key, ByteTag.of(value)) }

    public fun putShort(key: String, value: Short): MutableCompoundTag = apply { put(key, ShortTag.of(value)) }

    public fun putInt(key: String, value: Int): MutableCompoundTag = apply { put(key, IntTag.of(value)) }

    public fun putLong(key: String, value: Long): MutableCompoundTag = apply { put(key, LongTag.of(value)) }

    public fun putFloat(key: String, value: Float): MutableCompoundTag = apply { put(key, FloatTag.of(value)) }

    public fun putDouble(key: String, value: Double): MutableCompoundTag = apply { put(key, DoubleTag.of(value)) }

    public fun putString(key: String, value: String): MutableCompoundTag = apply { put(key, StringTag.of(value)) }

    public fun putUUID(key: String, value: UUID): MutableCompoundTag = apply { put(key, value.toTag()) }

    public fun putByteArray(key: String, value: ByteArray): MutableCompoundTag = apply { put(key, ByteArrayTag(value)) }

    public fun putIntArray(key: String, value: IntArray): MutableCompoundTag = apply { put(key, IntArrayTag(value)) }

    public fun putLongArray(key: String, value: LongArray): MutableCompoundTag = apply { put(key, LongArrayTag(value)) }

    public fun putBoolean(key: String, value: Boolean): MutableCompoundTag = apply { put(key, ByteTag.of(value)) }

    public fun update(key: String, builder: MutableCompoundTag.() -> Unit): MutableCompoundTag = apply {
        val tag = getCompound(key)
        if (tag is MutableCompoundTag) put(key, tag.apply(builder)) else put(key, tag.mutable().apply(builder))
    }

    public fun updateList(key: String, type: Int, builder: ListTag.() -> Unit): MutableCompoundTag = put(key, getList(key, type).apply(builder))

    public fun immutable(): CompoundTag = CompoundTag(tags)

    override fun copy(): MutableCompoundTag {
        val copy = tags.mapValuesTo(mutableMapOf()) { it.value.copy() }
        return MutableCompoundTag(copy)
    }

    override fun put(key: String, value: Tag): MutableCompoundTag = apply { put(key, value) }

    override fun get(key: String): Tag? = tags[key]

    override fun containsKey(key: String): Boolean = tags.containsKey(key)

    override fun containsValue(value: Tag): Boolean = tags.containsValue(value)

    override fun isEmpty(): Boolean = tags.isEmpty()

    override val size: Int
        get() = tags.size
    override val entries: MutableSet<MutableMap.MutableEntry<String, Tag>>
        get() = tags.entries
    override val keys: MutableSet<String>
        get() = tags.keys
    override val values: MutableCollection<Tag>
        get() = tags.values
}
