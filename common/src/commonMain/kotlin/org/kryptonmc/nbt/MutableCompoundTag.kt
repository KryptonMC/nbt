/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

public class MutableCompoundTag(
    override val tags: MutableMap<String, Tag> = mutableMapOf()
) : ScopedCompoundTag<MutableCompoundTag>(), MutableMap<String, Tag> {

    override val size: Int
        get() = tags.size
    override val entries: MutableSet<MutableMap.MutableEntry<String, Tag>>
        get() = tags.entries
    override val keys: MutableSet<String>
        get() = tags.keys
    override val values: MutableCollection<Tag>
        get() = tags.values

    override fun put(key: String, value: Tag): MutableCompoundTag = apply { tags[key] = value }

    override fun remove(key: String): MutableCompoundTag = apply { tags.remove(key) }

    override fun clear() {
        tags.clear()
    }

    override fun putAll(from: Map<out String, Tag>) {
        tags.putAll(from)
    }

    override fun copy(): MutableCompoundTag {
        val copy = tags.mapValuesTo(mutableMapOf()) { it.value.copy() }
        return MutableCompoundTag(copy)
    }
}
