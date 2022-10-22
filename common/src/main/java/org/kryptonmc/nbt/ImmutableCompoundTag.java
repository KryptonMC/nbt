/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt;

import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

/**
 * An immutable compound tag.
 */
public sealed interface ImmutableCompoundTag extends ScopedCompoundTag<ImmutableCompoundTag> permits ImmutableCompoundTagImpl {

    /**
     * Creates a new immutable compound tag from the given data.
     *
     * @param data the data
     * @return a new immutable compound tag
     */
    static @NotNull ImmutableCompoundTag of(final @NotNull Map<? extends String, ? extends Tag> data) {
        // Optimization: If the data is empty, we can just return the empty compound.
        if (data.isEmpty()) return (ImmutableCompoundTag) EMPTY;
        return new ImmutableCompoundTagImpl(HashTreePMap.from(data));
    }

    /**
     * Creates a new builder for building an immutable compound tag.
     *
     * @return a new builder
     */
    static @NotNull Builder builder() {
        return new ImmutableCompoundTagImpl.Builder();
    }

    @Override
    @NotNull PMap<String, Tag> getData();

    @Override
    @NotNull Builder toBuilder();

    /**
     * A builder for building immutable compound tags.
     */
    sealed interface Builder extends ScopedCompoundTag.Builder<Builder> permits ImmutableCompoundTagImpl.Builder {}
}
