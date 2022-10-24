/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.io;

import java.io.DataOutput;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;
import org.kryptonmc.nbt.Tag;

/**
 * A writer for writing tags of type T.
 *
 * @param <T> the type of tags that can be written by this writer
 */
@FunctionalInterface
public interface TagWriter<T extends @NotNull Tag> {

    /**
     * Writes the given value to the given output.
     *
     * @param output the output to write to
     * @param value the value to write
     * @throws IOException if an IO error occurred
     */
    void write(final @NotNull DataOutput output, final @NotNull T value) throws IOException;
}
