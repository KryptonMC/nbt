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

/**
 * A variant of [CompoundTag] that is immutable.
 *
 * All attempts to write to an immutable tag will result in a new immutable tag
 * being created with the requested changes.
 */
public class ImmutableCompoundTag(override val data: PersistentMap<String, Tag> = persistentMapOf()) : ScopedCompoundTag<ImmutableCompoundTag>() {

    override fun put(key: String, value: Tag): ImmutableCompoundTag = ImmutableCompoundTag(data.put(key, value))

    override fun remove(key: String): ImmutableCompoundTag = ImmutableCompoundTag(data.remove(key))

    override fun copy(): ImmutableCompoundTag = this // Immutable, no need to copy

    override fun toBuilder(): Builder = Builder.create(data.toMutableMap(), false)
}
