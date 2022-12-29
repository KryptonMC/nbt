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
import java.io.IOException;
import org.jetbrains.annotations.NotNull;
import org.kryptonmc.nbt.visitor.StreamingTagVisitor;
import org.kryptonmc.nbt.visitor.StringTagVisitor;
import org.kryptonmc.nbt.visitor.TagVisitor;

/**
 * The base supertype for all NBT tags.
 */
public sealed interface Tag permits ScopedTag, CollectionTag, NumberTag, StringTag {

    /**
     * Gets the ID of this tag.
     *
     * @return the ID of this tag
     */
    int id();

    /**
     * Gets the type of this tag.
     *
     * @return the type of this tag
     */
    @NotNull TagType<?> type();

    /**
     * Writes this tag's contents to the given output.
     *
     * @param output the output to write to
     * @throws IOException if an IO error occurs
     */
    void write(final @NotNull DataOutput output) throws IOException;

    /**
     * Visits this tag's contents using the given visitor.
     *
     * @param visitor the visitor
     */
    void visit(final @NotNull TagVisitor visitor);

    /**
     * Visits this tag's contents using the given visitor.
     *
     * @param visitor the visitor
     * @return the result
     */
    StreamingTagVisitor.@NotNull ValueResult visit(final @NotNull StreamingTagVisitor visitor);

    /**
     * Converts this tag in to its SNBT form.
     *
     * @return the SNBT form of this tag
     */
    default @NotNull String asString() {
        return new StringTagVisitor().visit(this);
    }

    /**
     * Creates a copy of this tag and returns the result.
     *
     * @return the resulting copy
     * @implSpec If this tag is immutable, it will return itself, as there is
     * no need to make a copy of it. Otherwise, it will copy/clone all relevant
     * fields and construct a new object with the results.
     */
    @NotNull Tag copy();
}
