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

public class MutableListTag(
    public override val data: MutableList<Tag> = mutableListOf(),
    elementType: Int = 0
) : ListTag(data, elementType), MutableCollectionTag<Tag>, MutableList<Tag> by data {

    override val size: Int
        get() = data.size

    /**
     * Sets the value at the given [index] to the given byte [value].
     *
     * @param index the index
     * @param value the value
     */
    public fun setByte(index: Int, value: Byte) {
        set(index, ByteTag.of(value))
    }

    /**
     * Sets the value at the given [index] to the given short [value].
     *
     * @param index the index
     * @param value the value
     */
    public fun setShort(index: Int, value: Short) {
        set(index, ShortTag.of(value))
    }

    /**
     * Sets the value at the given [index] to the given integer [value].
     *
     * @param index the index
     * @param value the value
     */
    public fun setInt(index: Int, value: Int) {
        set(index, IntTag.of(value))
    }

    /**
     * Sets the value at the given [index] to the given long [value].
     *
     * @param index the index
     * @param value the value
     */
    public fun setLong(index: Int, value: Long) {
        set(index, LongTag.of(value))
    }

    /**
     * Sets the value at the given [index] to the given float [value].
     *
     * @param index the index
     * @param value the value
     */
    public fun setFloat(index: Int, value: Float) {
        set(index, FloatTag.of(value))
    }

    /**
     * Sets the value at the given [index] to the given double [value].
     *
     * @param index the index
     * @param value the value
     */
    public fun setDouble(index: Int, value: Double) {
        set(index, DoubleTag.of(value))
    }

    /**
     * Sets the value at the given [index] to the given string [value].
     *
     * @param index the index
     * @param value the value
     */
    public fun setString(index: Int, value: String) {
        set(index, StringTag.of(value))
    }

    /**
     * Sets the value at the given [index] to the given byte array [value].
     *
     * @param index the index
     * @param value the value
     */
    public fun setByteArray(index: Int, value: ByteArray) {
        set(index, ByteArrayTag(value))
    }

    /**
     * Sets the value at the given [index] to the given integer array [value].
     *
     * @param index the index
     * @param value the value
     */
    public fun setIntArray(index: Int, value: IntArray) {
        set(index, IntArrayTag(value))
    }

    /**
     * Sets the value at the given [index] to the given long array [value].
     *
     * @param index the index
     * @param value the value
     */
    public fun setLongArray(index: Int, value: LongArray) {
        set(index, LongArrayTag(value))
    }

    override fun set(index: Int, element: Tag): Tag {
        val oldValue = data[index]
        if (!setTag(index, element)) throw UnsupportedOperationException("Cannot add tag of type ${element.id} to list of type $elementType!")
        return oldValue
    }

    override fun add(index: Int, element: Tag) {
        if (updateType(element)) {
            data.add(index, element)
            return
        }
        throw UnsupportedOperationException("Cannot add tag of type ${element.id} to list of type $elementType!")
    }

    override fun add(element: Tag): Boolean {
        if (updateType(element)) {
            data.add(element)
            return true
        }
        return false
    }

    override fun remove(element: Tag): Boolean {
        val result = data.remove(element)
        if (data.isEmpty()) elementType = EndTag.ID
        return result
    }

    override fun removeAt(index: Int): Tag {
        val oldValue = data.removeAt(index)
        if (data.isEmpty()) elementType = EndTag.ID
        return oldValue
    }

    override fun contains(element: Tag): Boolean = data.contains(element)

    override fun containsAll(elements: Collection<Tag>): Boolean = data.containsAll(elements)

    override fun indexOf(element: Tag): Int = data.indexOf(element)

    override fun isEmpty(): Boolean = data.isEmpty()

    override fun lastIndexOf(element: Tag): Int = data.lastIndexOf(element)

    override fun iterator(): MutableIterator<Tag> = data.iterator()

    override fun listIterator(): MutableListIterator<Tag> = data.listIterator()

    override fun listIterator(index: Int): MutableListIterator<Tag> = data.listIterator(index)

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<Tag> = data.subList(fromIndex, toIndex)

    override fun clear() {
        data.clear()
        elementType = EndTag.ID
    }

    override fun copy(): MutableListTag {
        val iterable = if (Types.of(elementType).isValue) data else data.map { it.copy() }
        val list = ArrayList(iterable)
        return MutableListTag(list, elementType)
    }

    override fun setTag(index: Int, tag: Tag): Boolean {
        if (updateType(tag)) {
            data[index] = tag
            return true
        }
        return false
    }

    override fun addTag(index: Int, tag: Tag): Boolean {
        if (updateType(tag)) {
            data.add(index, tag)
            return true
        }
        return false
    }

    private fun updateType(tag: Tag): Boolean {
        if (tag.id == 0) return false
        if (elementType == 0) {
            elementType = tag.id
            return true
        }
        return elementType == tag.id
    }
}
