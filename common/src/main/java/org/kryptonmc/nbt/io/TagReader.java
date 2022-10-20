/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.io;

import java.io.DataInput;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;
import org.kryptonmc.nbt.Tag;

/**
 * A reader for reading tags of type T.
 *
 * @param <T> the type of tags that can be read by this reader
 */
@FunctionalInterface
public interface TagReader<T extends Tag> {

    /**
     * Reads the tag from the given input.
     *
     * @param input the input to read from
     * @param depth the depth, used for keeping track of recursive reading
     * @return the resulting read tag
     * @throws IOException if an IO error occurred
     */
    @NotNull T read(final @NotNull DataInput input, final int depth) throws IOException;
}
