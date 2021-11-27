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
 * A tag that holds a collection of values of the given type [E].
 *
 * @param E the held tag type
 */
public interface CollectionTag<E : Tag> : Collection<E>, Tag {

    /**
     * The ID of the tags that can be held by this collection tag.
     */
    public val elementType: Int
}
