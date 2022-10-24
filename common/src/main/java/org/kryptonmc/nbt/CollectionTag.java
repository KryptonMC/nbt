/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt;

import java.util.Iterator;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

/**
 * A tag that holds other types of tags.
 *
 * @param <T> the type of tag held by this collection tag
 */
public sealed interface CollectionTag<T extends @NotNull Tag> extends Iterable<@NotNull T>, Tag permits ListCollectionTag, ListTag {

    /**
     * Gets the type of the tags that are stored by this collection tag.
     *
     * @return the type of the stored elements
     */
    int elementType();

    /**
     * Gets the number of elements in this collection tag.
     *
     * @return the size of this collection tag
     */
    int size();

    /**
     * Checks if this collection tag contains no elements.
     *
     * @return true if this collection tag contains no elements, false
     *         otherwise
     */
    boolean isEmpty();

    /**
     * Attempts to add the given tag to this collection at the given index.
     *
     * @param index the index to add the tag at
     * @param tag the tag to add
     * @return whether the tag was added
     * @apiNote This is an optional method that is not supported by all
     * collection tags. Specifically, all immutable collections will always
     * return false.
     */
    boolean tryAdd(final int index, final @NotNull T tag);

    /**
     * Attempts to add the given tag to this collection.
     *
     * @param tag the tag to add
     * @return whether the tag was added
     * @apiNote This is an optional method that is not supported by all
     * collection tags. Specifically, all immutable collections will always
     * return false.
     */
    boolean tryAdd(final @NotNull T tag);

    @Override
    @NotNull Iterator<@NotNull T> iterator();

    /**
     * Creates a new stream of the elements in this collection.
     *
     * @return a stream
     */
    @NotNull Stream<@NotNull T> stream();
}
