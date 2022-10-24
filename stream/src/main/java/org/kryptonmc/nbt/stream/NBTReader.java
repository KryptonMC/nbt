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
import org.kryptonmc.nbt.EndTag;
import org.kryptonmc.nbt.FloatTag;
import org.kryptonmc.nbt.ImmutableCompoundTag;
import org.kryptonmc.nbt.ImmutableListTag;
import org.kryptonmc.nbt.IntArrayTag;
import org.kryptonmc.nbt.IntTag;
import org.kryptonmc.nbt.ListTag;
import org.kryptonmc.nbt.LongArrayTag;
import org.kryptonmc.nbt.LongTag;
import org.kryptonmc.nbt.ShortTag;
import org.kryptonmc.nbt.StringTag;
import org.kryptonmc.nbt.Tag;

public interface NBTReader extends AutoCloseable {

    byte peekType() throws IOException;

    int beginByteArray() throws IOException;

    void endByteArray() throws IOException;

    int beginIntArray() throws IOException;

    void endIntArray() throws IOException;

    int beginLongArray() throws IOException;

    void endLongArray() throws IOException;

    int beginList(final int elementType) throws IOException;

    void endList() throws IOException;

    void beginCompound() throws IOException;

    void endCompound() throws IOException;

    boolean hasNext() throws IOException;

    byte nextType() throws IOException;

    @NotNull String nextName() throws IOException;

    byte nextByte() throws IOException;

    short nextShort() throws IOException;

    int nextInt() throws IOException;

    long nextLong() throws IOException;

    float nextFloat() throws IOException;

    double nextDouble() throws IOException;

    @NotNull String nextString() throws IOException;

    void nextEnd() throws IOException;

    default @NotNull Tag read() throws IOException {
        final int nextType = peekType();
        return switch (nextType) {
            case ByteArrayTag.ID -> {
                final byte[] bytes = new byte[beginByteArray()];
                for (int i = 0; hasNext(); i++) {
                    bytes[i] = nextByte();
                }
                endByteArray();
                yield ByteArrayTag.of(bytes);
            }
            case IntArrayTag.ID -> {
                final int[] ints = new int[beginIntArray()];
                for (int i = 0; hasNext(); i++) {
                    ints[i] = nextInt();
                }
                endIntArray();
                yield IntArrayTag.of(ints);
            }
            case LongArrayTag.ID -> {
                final long[] longs = new long[beginLongArray()];
                for (int i = 0; hasNext(); i++) {
                    longs[i] = nextLong();
                }
                endLongArray();
                yield LongArrayTag.of(longs);
            }
            case ListTag.ID -> {
                final int elementType = nextType();
                final ListTag.Builder builder = ImmutableListTag.builder(elementType);
                for (int i = 0, size = beginList(elementType); i < size; i++) {
                    builder.add(read());
                }
                endList();
                yield builder.build();
            }
            case CompoundTag.ID -> {
                beginCompound();
                final CompoundTag.Builder builder = ImmutableCompoundTag.builder();
                for (int type = nextType(); type != EndTag.ID; type = nextType()) {
                    builder.put(nextName(), read());
                }
                yield builder.build();
            }
            case ByteTag.ID -> ByteTag.of(nextByte());
            case ShortTag.ID -> ShortTag.of(nextShort());
            case IntTag.ID -> IntTag.of(nextInt());
            case LongTag.ID -> LongTag.of(nextLong());
            case FloatTag.ID -> FloatTag.of(nextFloat());
            case DoubleTag.ID -> DoubleTag.of(nextDouble());
            case StringTag.ID -> StringTag.of(nextString());
            default -> throw new IllegalStateException("Unknown tag type: " + nextType);
        };
    }

    @Override
    void close() throws IOException;
}
