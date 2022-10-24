/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.pcollections.TreePVector;

/**
 * An immutable list tag.
 */
public sealed interface ImmutableListTag extends ScopedListTag<ImmutableListTag> permits ImmutableListTagImpl {

    /**
     * Creates a new immutable list tag from the given data with the given
     * element type.
     *
     * @param data the data
     * @param elementType the element type
     * @return a new immutable list tag
     */
    static @NotNull ImmutableListTag of(final @NotNull List<? extends @NotNull Tag> data, final int elementType) {
        // Optimization: For empty data, the element type is always EndTag, so we can just return the empty list.
        if (data.isEmpty()) return EMPTY;
        return new ImmutableListTagImpl(TreePVector.from(data), elementType);
    }

    /**
     * Creates a new builder for building an immutable list tag with the given
     * element type.
     *
     * @param elementType the element type
     * @return a new builder
     */
    static @NotNull Builder builder(final int elementType) {
        return new ImmutableListTagImpl.Builder(elementType);
    }

    /**
     * Creates a new builder for building an immutable list tag.
     *
     * <p>The element type will be determined as elements are added.</p>
     *
     * @return a new builder
     */
    static @NotNull Builder builder() {
        return builder(EndTag.ID);
    }

    @Override
    @NotNull Builder toBuilder();

    /**
     * A builder for building immutable list tags.
     */
    sealed interface Builder extends ScopedListTag.Builder<Builder> permits ImmutableListTagImpl.Builder {}
}
