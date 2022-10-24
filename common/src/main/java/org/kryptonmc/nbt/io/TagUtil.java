/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.kryptonmc.nbt.CompoundTag;
import org.kryptonmc.nbt.EndTag;
import org.kryptonmc.nbt.Tag;
import org.kryptonmc.nbt.util.Types;

final class TagUtil {

    private static final NamedTag END_NAMED = new NamedTag("", EndTag.INSTANCE);

    static Tag readUnnamedTag(final InputStream inputStream) throws IOException {
        final var input = ensureDataInput(inputStream);
        final var type = input.read();
        if (type == EndTag.ID) return EndTag.INSTANCE;
        input.readUTF();
        return Types.of(type).read(input, 0);
    }

    static NamedTag readNamedTag(final InputStream inputStream) throws IOException {
        final var input = ensureDataInput(inputStream);
        final var type = input.read();
        if (type == EndTag.ID) return END_NAMED;
        final var name = input.readUTF();
        final var tag = Types.of(type).read(input, 0);
        return new NamedTag(name, tag);
    }

    static void writeNamedTag(final OutputStream outputStream, final String name, final Tag value) throws IOException {
        final var output = outputStream instanceof final DataOutputStream data ? data : new DataOutputStream(outputStream);
        output.writeByte(value.id());
        if (value.id() == EndTag.ID) return;
        output.writeUTF(name);
        value.write(output);
    }

    static CompoundTag ensureCompound(final Tag tag) throws IOException {
        if (tag instanceof final CompoundTag compound) return compound;
        throw new IOException("Root tag must be an unnamed compound!");
    }

    private static DataInputStream ensureDataInput(final InputStream input) {
        return input instanceof final DataInputStream data ? data : new DataInputStream(input);
    }

    private TagUtil() {
    }
}
