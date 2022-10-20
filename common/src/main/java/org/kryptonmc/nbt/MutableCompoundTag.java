/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt;

import java.util.LinkedHashMap;
import java.util.Map;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A mutable compound tag.
 */
public sealed interface MutableCompoundTag extends ScopedCompoundTag<MutableCompoundTag> permits MutableCompoundTagImpl {

    /**
     * Creates a new mutable compound tag from the given data.
     *
     * @param data the data
     * @return a new mutable compound tag
     */
    @Contract(value = "_ -> new", pure = true)
    static @NotNull MutableCompoundTag of(final @NotNull Map<? extends String, ? extends Tag> data) {
        return new MutableCompoundTagImpl(new LinkedHashMap<>(data));
    }

    /**
     * Creates a new builder for building a mutable compound tag.
     *
     * @return a new builder
     */
    @Contract(value = "-> new", pure = true)
    static @NotNull Builder builder() {
        return new MutableCompoundTagImpl.Builder();
    }

    /**
     * Clears this mutable compound tag, erasing all elements stored in it.
     */
    void clear();

    @Override
    @NotNull Builder toBuilder();

    /**
     * A builder for building mutable compound tags.
     */
    sealed interface Builder extends ScopedCompoundTag.Builder<Builder> permits MutableCompoundTagImpl.Builder {}
}
