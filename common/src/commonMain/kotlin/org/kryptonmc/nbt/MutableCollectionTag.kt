/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

/**
 * A collection tag that has mutable contents that can be updated.
 */
public interface MutableCollectionTag<E : Tag> : CollectionTag<E>, MutableCollection<E>, Tag {

    /**
     * Sets the tag at the given [index] to the given [tag], and returns the
     * result of the set.
     *
     * What the result will be here is defined by what implements this.
     *
     * @param index the index
     * @param tag the tag
     * @return the result of the set
     */
    public fun setTag(index: Int, tag: Tag): Boolean

    /**
     * Adds the given [tag] to this collection at the given [index], and
     * returns the result of the add.
     *
     * What the result will be here is defined by what implements this.
     *
     * @param index the index
     * @param tag the tag
     * @return the result of the add
     */
    public fun addTag(index: Int, tag: Tag): Boolean
}
