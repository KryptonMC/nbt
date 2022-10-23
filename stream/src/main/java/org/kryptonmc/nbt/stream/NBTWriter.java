/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.stream;

import java.io.IOException;
import org.jetbrains.annotations.NotNull;
import org.kryptonmc.nbt.ByteArrayTag;
import org.kryptonmc.nbt.ByteTag;
import org.kryptonmc.nbt.CompoundTag;
import org.kryptonmc.nbt.DoubleTag;
import org.kryptonmc.nbt.FloatTag;
import org.kryptonmc.nbt.IntArrayTag;
import org.kryptonmc.nbt.IntTag;
import org.kryptonmc.nbt.ListTag;
import org.kryptonmc.nbt.LongArrayTag;
import org.kryptonmc.nbt.LongTag;
import org.kryptonmc.nbt.ShortTag;
import org.kryptonmc.nbt.StringTag;
import org.kryptonmc.nbt.Tag;

public interface NBTWriter extends AutoCloseable {

    void beginByteArray(final int size) throws IOException;

    void endByteArray() throws IOException;

    void beginIntArray(final int size) throws IOException;

    void endIntArray() throws IOException;

    void beginLongArray(final int size) throws IOException;

    void endLongArray() throws IOException;

    void beginList(final int elementType, final int size) throws IOException;

    void endList() throws IOException;

    void beginCompound() throws IOException;

    void endCompound() throws IOException;

    void name(final @NotNull String name) throws IOException;

    void value(final boolean value) throws IOException;

    void value(final byte value) throws IOException;

    void value(final short value) throws IOException;

    void value(final int value) throws IOException;

    void value(final long value) throws IOException;

    void value(final float value) throws IOException;

    void value(final double value) throws IOException;

    void value(final @NotNull String value) throws IOException;

    void end() throws IOException;

    default void write(final @NotNull Tag value) throws IOException {
        switch (value.id()) {
            case ByteTag.ID -> value(((ByteTag) value).value());
            case ShortTag.ID -> value(((ShortTag) value).value());
            case IntTag.ID -> value(((IntTag) value).value());
            case LongTag.ID -> value(((LongTag) value).value());
            case FloatTag.ID -> value(((FloatTag) value).value());
            case DoubleTag.ID -> value(((DoubleTag) value).value());
            case StringTag.ID -> value(((StringTag) value).value());
            case ListTag.ID -> {
                final ListTag list = (ListTag) value;
                beginList(list.elementType(), list.size());
                for (final var element : list) {
                    write(element);
                }
                endList();
            }
            case CompoundTag.ID -> {
                final CompoundTag compound = (CompoundTag) value;
                beginCompound();
                for (final var entry : compound.getData().entrySet()) {
                    name(entry.getKey());
                    write(entry.getValue());
                }
                endCompound();
            }
            case ByteArrayTag.ID -> {
                final ByteArrayTag byteArray = (ByteArrayTag) value;
                beginByteArray(byteArray.size());
                for (final byte element : byteArray.getData()) {
                    value(element);
                }
                endByteArray();
            }
            case IntArrayTag.ID -> {
                final IntArrayTag intArray = (IntArrayTag) value;
                beginByteArray(intArray.size());
                for (final int element : intArray.getData()) {
                    value(element);
                }
                endByteArray();
            }
            case LongArrayTag.ID -> {
                final LongArrayTag longArray = (LongArrayTag) value;
                beginByteArray(longArray.size());
                for (final long element : longArray.getData()) {
                    value(element);
                }
                endByteArray();
            }
            default -> throw new IllegalStateException("Don't know how to write " + value + "!");
        }
    }

    @Override
    void close() throws IOException;
}
