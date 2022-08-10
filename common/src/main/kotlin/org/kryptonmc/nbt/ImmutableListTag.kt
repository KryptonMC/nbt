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
public class ImmutableListTag(override val data: PersistentList<Tag>, override val elementType: Int) : ScopedListTag<ImmutableListTag>() {

    override val isImmutable: Boolean
        get() = true

    public constructor(elementType: Int) : this(persistentListOf(), elementType)

    public constructor() : this(EndTag.ID)

    override fun add(tag: Tag): ImmutableListTag {
        if (tag.id == 0) throw UnsupportedOperationException("Cannot add tag of type ${tag.id} to list of type $elementType!")
        if (elementType == 0) return ImmutableListTag(data.add(tag), tag.id)
        if (elementType == tag.id) return ImmutableListTag(data.add(tag), tag.id)
        throw UnsupportedOperationException("Cannot add tag of type ${tag.id} to list of type $elementType!")
    }

    override fun addTag(index: Int, tag: Tag) {
        // Do nothing, this tag is immutable, and this function is designed for mutable tags.
    }

    override fun addTag(tag: Tag) {
        // Do nothing, this tag is immutable, and this function is designed for mutable tags.
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

    override fun toBuilder(): Builder = Builder.immutable(data, elementType)
}
