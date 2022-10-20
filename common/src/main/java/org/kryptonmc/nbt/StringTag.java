/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt;

import org.jetbrains.annotations.NotNull;
import org.kryptonmc.nbt.io.TagReader;
import org.kryptonmc.nbt.io.TagWriter;

/**
 * A tag that holds a string value.
 */
public sealed interface StringTag extends Tag permits StringTagImpl {

    /**
     * The string tag representing the empty string.
     */
    @NotNull StringTag EMPTY = new StringTagImpl("");

    /**
     * The ID of this type of tag.
     *
     * <p>Used for {@link CollectionTag#elementType()} and in the serialized
     * binary form.</p>
     */
    int ID = 8;
    /**
     * The reader for reading string tags.
     */
    @NotNull TagReader<StringTag> READER = (input, depth) -> of(input.readUTF());
    /**
     * The writer for writing string tags.
     */
    @NotNull TagWriter<StringTag> WRITER = (output, value) -> output.writeUTF(value.value());
    /**
     * The tag type for this tag.
     */
    @NotNull TagType<StringTag> TYPE = new TagType<>("TAG_String", true, READER);

    /**
     * Gets a string tag that represents the given value.
     *
     * @param value the backing value
     * @return a string tag representing the value
     */
    static @NotNull StringTag of(final @NotNull String value) {
        return value.isEmpty() ? EMPTY : new StringTagImpl(value);
    }

    /**
     * Gets the underlying value of this string tag.
     *
     * @return the value
     */
    @NotNull String value();

    @Override
    @NotNull StringTag copy();
}
