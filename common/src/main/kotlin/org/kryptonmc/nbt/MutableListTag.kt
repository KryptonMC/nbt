/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

import org.kryptonmc.nbt.io.Types

public class MutableListTag(public override val data: MutableList<Tag>, elementType: Int) : ScopedListTag<MutableListTag>() {

    override var elementType: Int = elementType
        private set

    override val isMutable: Boolean
        get() = true

    public constructor(elementType: Int) : this(mutableListOf(), elementType)

    public constructor() : this(EndTag.ID)

    override fun set(index: Int, tag: Tag): MutableListTag = apply {
        if (updateType(tag)) {
            data[index] = tag
            return@apply
        }
        throw UnsupportedOperationException("Cannot add tag of type ${tag.id} to list of type $elementType!")
    }

    override fun add(tag: Tag): MutableListTag = apply { genericAdd(tag) { data.add(tag) } }

    override fun addTag(index: Int, tag: Tag) {
        genericAdd(tag) { data.add(index, tag) }
    }

    private inline fun genericAdd(tag: Tag, adder: () -> Unit) {
        if (tag.id == 0) throw UnsupportedOperationException("Cannot add end tag to list!")
        if (updateType(tag)) {
            adder()
            return
        }
        throw UnsupportedOperationException("Cannot add tag of type ${tag.id} to list of type $elementType!")
    }

    override fun removeAt(index: Int): MutableListTag = apply {
        data.removeAt(index)
        if (data.isEmpty()) elementType = EndTag.ID
    }

    override fun remove(tag: Tag): MutableListTag = apply {
        data.remove(tag)
        if (data.isEmpty()) elementType = EndTag.ID
    }

    public fun clear() {
        data.clear()
        elementType = EndTag.ID
    }

    override fun copy(): MutableListTag {
        val newData = if (Types.of(elementType).isValue) data else data.mapTo(mutableListOf(), Tag::copy)
        return MutableListTag(newData, elementType)
    }

    override fun toBuilder(): Builder = Builder.mutable(data, elementType)

    private fun updateType(tag: Tag): Boolean {
        if (elementType == 0) {
            elementType = tag.id
            return true
        }
        return elementType == tag.id
    }
}
