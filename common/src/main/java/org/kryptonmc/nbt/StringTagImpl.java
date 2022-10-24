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

record StringTagImpl(@NotNull String value) implements StringTag {

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
        WRITER.write(output, this);
    }

    @Override
    public <T> void visit(final @NotNull TagVisitor<T> visitor) {
        visitor.visitString(this);
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
