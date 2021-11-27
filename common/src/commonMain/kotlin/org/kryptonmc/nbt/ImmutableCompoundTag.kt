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

/**
 * A variant of [CompoundTag] that is immutable.
 *
 * All attempts to write to an immutable tag will result in a new immutable tag
 * being created with the requested changes.
 */
public class ImmutableCompoundTag(tags: Map<String, Tag> = emptyMap()) : CompoundTag(tags) {

    override fun put(key: String, value: Tag): ImmutableCompoundTag = ImmutableCompoundTag(tags.plus(key to value))

    override fun remove(key: String): ImmutableCompoundTag {
        val result = if (tags is MutableMap) tags.apply { remove(key) } else tags.toMutableMap().apply { remove(key) }
        return ImmutableCompoundTag(result)
    }

    override fun putBoolean(key: String, value: Boolean): ImmutableCompoundTag = put(key, ByteTag.of(value))

    override fun putByte(key: String, value: Byte): ImmutableCompoundTag = put(key, ByteTag.of(value))

    override fun putShort(key: String, value: Short): ImmutableCompoundTag = put(key, ShortTag.of(value))

    override fun putInt(key: String, value: Int): ImmutableCompoundTag = put(key, IntTag.of(value))

    override fun putLong(key: String, value: Long): ImmutableCompoundTag = put(key, LongTag.of(value))

    override fun putFloat(key: String, value: Float): ImmutableCompoundTag = put(key, FloatTag.of(value))

    override fun putDouble(key: String, value: Double): ImmutableCompoundTag = put(key, DoubleTag.of(value))

    override fun putString(key: String, value: String): ImmutableCompoundTag = put(key, StringTag.of(value))

    override fun putUUID(key: String, value: UUID): ImmutableCompoundTag = put(key, value.toTag())

    override fun putByteArray(key: String, value: ByteArray): ImmutableCompoundTag = put(key, ByteArrayTag(value))

    override fun putIntArray(key: String, value: IntArray): ImmutableCompoundTag = put(key, IntArrayTag(value))

    override fun putLongArray(key: String, value: LongArray): ImmutableCompoundTag = put(key, LongArrayTag(value))

    override fun putBytes(key: String, vararg values: Byte): ImmutableCompoundTag = putByteArray(key, values)

    override fun putInts(key: String, vararg values: Int): ImmutableCompoundTag = putIntArray(key, values)

    override fun putLongs(key: String, vararg values: Long): ImmutableCompoundTag = putLongArray(key, values)

    override fun copy(): ImmutableCompoundTag = this // Immutable, no need to copy
}
