/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * A mutable list tag.
 */
public sealed interface MutableListTag extends ScopedListTag<MutableListTag> permits MutableListTagImpl {

    /**
     * Creates a new mutable list tag from the given data with the given
     * element type.
     *
     * @param data the data
     * @param elementType the element type
     * @return a new mutable list tag
     */
    static @NotNull MutableListTag of(final @NotNull List<? extends @NotNull Tag> data, final int elementType) {
        return new MutableListTagImpl(new ArrayList<>(data), elementType);
    }

    /**
     * Creates a new empty mutable list tag.
     *
     * @return a new empty mutable list tag
     */
    static @NotNull MutableListTag empty() {
        return new MutableListTagImpl(new ArrayList<>(), EndTag.ID);
    }

    /**
     * Creates a new builder for building a mutable list tag with the given
     * element type.
     *
     * @param elementType the element type
     * @return a new builder
     */
    static @NotNull Builder builder(final int elementType) {
        return new MutableListTagImpl.Builder(elementType);
    }

    /**
     * Creates a new builder for building a mutable list tag.
     *
     * <p>The element type will be determined as elements are added.</p>
     *
     * @return a new builder
     */
    static @NotNull Builder builder() {
        return builder(EndTag.ID);
    }

    /**
     * Clears this mutable list tag, erasing all elements stored in it.
     */
    void clear();

    @Override
    @NotNull Builder toBuilder();

    /**
     * A builder for building mutable list tags.
     */
    sealed interface Builder extends ScopedListTag.Builder<Builder> permits MutableListTagImpl.Builder {}
}
