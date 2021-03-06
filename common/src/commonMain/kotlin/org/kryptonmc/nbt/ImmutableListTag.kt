/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

/**
 * A variant of [ListTag] that is immutable.
 *
 * All attempts to write to an immutable tag will result in a new immutable tag
 * being created with the requested changes.
 */
public class ImmutableListTag(
    override val data: PersistentList<Tag> = persistentListOf(),
    override val elementType: Int = 0
) : ScopedListTag<ImmutableListTag>() {

    override fun add(tag: Tag): ImmutableListTag {
        if (tag.id == 0) throw UnsupportedOperationException("Cannot add tag of type ${tag.id} to list of type $elementType!")
        if (elementType == 0) return ImmutableListTag(data.add(tag), tag.id)
        if (elementType == tag.id) return ImmutableListTag(data.add(tag), tag.id)
        throw UnsupportedOperationException("Cannot add tag of type ${tag.id} to list of type $elementType!")
    }

    override fun removeAt(index: Int): ImmutableListTag {
        val result = data.removeAt(index)
        val newType = if (result.isEmpty()) EndTag.ID else elementType
        return ImmutableListTag(result, newType)
    }

    override fun remove(tag: Tag): ImmutableListTag {
        val result = data.remove(tag)
        val newType = if (result.isEmpty()) EndTag.ID else elementType
        return ImmutableListTag(result, newType)
    }

    override fun set(index: Int, tag: Tag): ImmutableListTag = ImmutableListTag(data.set(index, tag), elementType)

    override fun copy(): ListTag = this // Immutable, no need to copy
}
