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
 * A tag that is collection-based, like the list tag, or collection-like, such
 * as the array tags.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public sealed interface CollectionTag<T : Tag> : Collection<T>, Tag {

    /**
     * The type of the tags that are stored by this collection tag.
     */
    public val elementType: Int

    /**
     * Adds the given [tag] to this collection at the given [index].
     *
     * @param index the index to add the tag at
     * @param tag the tag to add
     */
    public fun addTag(index: Int, tag: T)
}