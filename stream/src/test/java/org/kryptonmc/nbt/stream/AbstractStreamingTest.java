/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.stream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

abstract class AbstractStreamingTest {

    protected static void doWriteAndRead(final IOConsumer<BinaryNBTWriter> write, final IOConsumer<DataInputStream> read) {
        final var output = new ByteArrayOutputStream();
        final var writer = new BinaryNBTWriter(new DataOutputStream(output));
        try {
            write.accept(writer);
            read.accept(new DataInputStream(new ByteArrayInputStream(output.toByteArray())));
        } catch (final IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    protected static void writeAndRead(final IOConsumer<BinaryNBTWriter> write, final IOConsumer<BinaryNBTReader> read) {
        doWriteAndRead(write, input -> read.accept(new BinaryNBTReader(input)));
    }

    @FunctionalInterface
    protected interface IOConsumer<T> {

        void accept(final T value) throws IOException;
    }
}
