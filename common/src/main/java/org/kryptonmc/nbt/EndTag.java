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
import java.io.DataOutput;
import org.jetbrains.annotations.NotNull;
import org.kryptonmc.nbt.visitor.StreamingTagVisitor;
import org.kryptonmc.nbt.visitor.TagVisitor;

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
     * The tag type for this tag.
     */
    public static final @NotNull TagType<@NotNull EndTag> TYPE = new TagType<>() {
        @Override
        public @NotNull String name() {
            return "END";
        }

        @Override
        public @NotNull String prettyName() {
            return "TAG_End";
        }

        @Override
        public boolean isValue() {
            return true;
        }

        @Override
        public @NotNull EndTag load(final @NotNull DataInput input, final int depth) {
            return EndTag.INSTANCE;
        }

        @Override
        public StreamingTagVisitor.@NotNull ValueResult parse(final @NotNull DataInput input, final @NotNull StreamingTagVisitor visitor) {
            return visitor.visitEnd();
        }

        @Override
        public void skip(final @NotNull DataInput input, final int bytes) {
            // Will never be anything to skip.
        }

        @Override
        public void skip(final @NotNull DataInput input) {
            // Will never be anything to skip.

        }
    };

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
    public void visit(final @NotNull TagVisitor visitor) {
        visitor.visitEnd(this);
    }

    @Override
    public StreamingTagVisitor.@NotNull ValueResult visit(final @NotNull StreamingTagVisitor visitor) {
        return visitor.visitEnd();
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
