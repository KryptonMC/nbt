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
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.kryptonmc.nbt.io.TagReader;

/**
 * A type of tag.
 */
public final class TagType<T extends @NotNull Tag> {

    private final String name;
    private final boolean isValue;
    private final Supplier<TagReader<T>> readerSupplier;

    /**
     * Creates a new tag type.
     *
     * @param name the name of the tag type
     * @param isValue if the tag is a value tag
     * @param readerSupplier a supplier of a tag reader for this tag type
     */
    public TagType(final @NotNull String name, final boolean isValue, final @NotNull Supplier<TagReader<T>> readerSupplier) {
        this.name = name;
        this.isValue = isValue;
        this.readerSupplier = readerSupplier;
    }

    /**
     * Creates a new tag type.
     *
     * @param name the name of the tag
     * @param isValue if the tag is a value tag
     * @param reader the reader for the tag
     */
    public TagType(final @NotNull String name, final boolean isValue, final @NotNull TagReader<T> reader) {
        this(name, isValue, () -> reader);
    }

    /**
     * Gets the name of this tag type.
     *
     * @return the name
     */
    public @NotNull String name() {
        return name;
    }

    /**
     * Checks if this tag type is for a value tag.
     *
     * @return true if this is a value tag type, false otherwise
     */
    public boolean isValue() {
        return isValue;
    }

    /**
     * Gets the reader for reading tags of this type.
     *
     * @return the reader
     */
    public @NotNull TagReader<T> reader() {
        return readerSupplier.get();
    }

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
        return reader().read(input, depth);
    }
}
