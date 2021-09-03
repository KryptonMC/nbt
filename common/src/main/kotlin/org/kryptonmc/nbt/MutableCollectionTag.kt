/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

public interface MutableCollectionTag<E : Tag> : CollectionTag<E>, MutableCollection<E>, Tag {

    public fun setTag(index: Int, tag: Tag): Boolean

    public fun addTag(index: Int, tag: Tag): Boolean
}
