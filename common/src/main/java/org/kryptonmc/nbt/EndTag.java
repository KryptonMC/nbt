/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt;

import java.io.DataOutput;
import org.jetbrains.annotations.NotNull;
import org.kryptonmc.nbt.io.TagReader;
import org.kryptonmc.nbt.io.TagWriter;

/**
 * The tag representing the end of a compound tag.
 */
public final class EndTag implements ScopedTag<@NotNull EndTag> {

    /**
     * The singleton instance that represents an end tag.
     */
    public static final @NotNull EndTag INSTANCE = new EndTag();

    /**
     * The ID of this type of tag.
     *
     * <p>Used for {@link CollectionTag#elementType()} and in the serialized
     * binary form.</p>
     */
    public static final int ID = 0;
    /**
     * The reader for reading end tags.
     */
    public static final @NotNull TagReader<@NotNull EndTag> READER = (input, depth) -> INSTANCE;
    /**
     * The writer for writing end tags.
     */
    public static final @NotNull TagWriter<@NotNull EndTag> WRITER = (output, value) -> {};
    /**
     * The tag type for this tag.
     */
    public static final @NotNull TagType<@NotNull EndTag> TYPE = new TagType<>("TAG_End", true, () -> READER);

    private EndTag() {
    }

    @Override
    public int id() {
        return ID;
    }

    @Override
    public @NotNull TagType<@NotNull EndTag> type() {
        return TYPE;
    }

    @Override
    public void write(final @NotNull DataOutput output) {
        // Nothing to write for end tags
    }

    @Override
    public <T> void visit(final @NotNull TagVisitor<@NotNull T> visitor) {
        visitor.visitEnd(this);
    }

    @Override
    public @NotNull EndTag copy() {
        return this;
    }

    @Override
    public String toString() {
        return "EndTag";
    }
}
