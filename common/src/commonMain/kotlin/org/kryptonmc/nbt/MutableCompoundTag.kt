/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

public class MutableCompoundTag(override val data: MutableMap<String, Tag>) : ScopedCompoundTag<MutableCompoundTag>() {

    public constructor() : this(mutableMapOf())

    override val keys: MutableSet<String>
        get() = data.keys
    override val values: MutableCollection<Tag>
        get() = data.values

    public fun clear() {
        data.clear()
    }

    override fun put(key: String, value: Tag): MutableCompoundTag = apply { data[key] = value }

    override fun remove(key: String): MutableCompoundTag = apply { data.remove(key) }

    override fun copy(): MutableCompoundTag = MutableCompoundTag(data.mapValuesTo(mutableMapOf()) { it.value.copy() })

    override fun toBuilder(): Builder = Builder.create(data, true)
}
