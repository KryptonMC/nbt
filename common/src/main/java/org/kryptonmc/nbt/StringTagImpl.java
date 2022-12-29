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
import java.io.IOException;
import org.jetbrains.annotations.NotNull;
import org.kryptonmc.nbt.visitor.StreamingTagVisitor;
import org.kryptonmc.nbt.visitor.TagVisitor;

record StringTagImpl(@NotNull String value) implements StringTag {

    static void skipString(final @NotNull DataInput input) throws IOException {
        input.skipBytes(input.readUnsignedShort());
    }

    static TagType<StringTag> createType() {
        return new TagType.VariableSize<>() {
            @Override
            public @NotNull String name() {
                return "STRING";
            }

            @Override
            public @NotNull String prettyName() {
                return "TAG_String";
            }

            @Override
            public boolean isValue() {
                return true;
            }

            @Override
            public @NotNull StringTag load(final @NotNull DataInput input, final int depth) throws IOException {
                return StringTag.of(input.readUTF());
            }

            @Override
            public StreamingTagVisitor.@NotNull ValueResult parse(final @NotNull DataInput input,
                                                                  final @NotNull StreamingTagVisitor visitor) throws IOException {
                return visitor.visit(input.readUTF());
            }

            @Override
            public void skip(final @NotNull DataInput input) throws IOException {
                skipString(input);
            }
        };
    }

    @Override
    public int id() {
        return ID;
    }

    @Override
    public @NotNull TagType<@NotNull StringTag> type() {
        return TYPE;
    }

    @Override
    public void write(final @NotNull DataOutput output) throws IOException {
        output.writeUTF(value);
    }

    @Override
    public void visit(final @NotNull TagVisitor visitor) {
        visitor.visitString(this);
    }

    @Override
    public StreamingTagVisitor.@NotNull ValueResult visit(final @NotNull StreamingTagVisitor visitor) {
        return visitor.visit(value);
    }

    @Override
    public @NotNull StringTag copy() {
        return this;
    }

    @Override
    public @NotNull String asString() {
        return value;
    }
}
