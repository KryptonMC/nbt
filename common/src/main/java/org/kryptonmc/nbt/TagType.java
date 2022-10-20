/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt;

import java.io.DataInput;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;
import org.kryptonmc.nbt.io.TagReader;

/**
 * A type of tag.
 *
 * @param name the name of the tag type
 * @param isValue if the tag is a value tag
 */
public record TagType<T extends Tag>(@NotNull String name, boolean isValue, @NotNull TagReader<T> reader) {

    /**
     * Reads the tag from the given input using this tag type's reader.
     *
     * @param input the input
     * @param depth the depth
     * @return the resulting tag
     * @throws IOException if an I/O error occurs
     * @see TagReader#read(DataInput, int)
     */
    public @NotNull T read(final @NotNull DataInput input, final int depth) throws IOException {
        return reader.read(input, depth);
    }
}
