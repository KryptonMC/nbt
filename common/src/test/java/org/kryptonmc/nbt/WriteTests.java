/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import org.junit.jupiter.api.Test;
import org.kryptonmc.nbt.io.TagCompression;
import org.kryptonmc.nbt.io.TagIO;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

final class WriteTests {

    private static final CompoundTag TAG = ImmutableCompoundTag.builder()
            .putByte("byte test", (byte) 1)
            .putShort("short test", (short) 25)
            .putInt("int test", 58329)
            .putLong("long test", 19848395)
            .putFloat("float test", 0.4231535F)
            .putDouble("double test", 1.2136563264461)
            .putString("string test", "cauidsgahsgdg")
            .putBytes("byte array test", (byte) 1, (byte) 4, (byte) 2)
            .putInts("int array test", 8, 9, 2)
            .putLongs("long array test", 73, 492, 195)
            .putList("list test", IntTag.ID, IntTag.of(1), IntTag.of(74), IntTag.of(493029))
            .putCompound("compound test", builder -> {
                builder.putList("nested list", LongTag.ID, LongTag.of(74), LongTag.of(9385), LongTag.of(25412389589235L));
                builder.putCompound("nested compound", builder1 -> builder1.putInt("nested int", 31235));
            })
            .build();

    @Test
    void testUncompressedUnnamed() throws IOException {
        checkWrite(TagCompression.NONE, "");
    }

    @Test
    void testUncompressedNamed() throws IOException {
        checkWrite(TagCompression.NONE, "Test");
    }

    @Test
    void testGzipUnnamed() throws IOException {
        checkWrite(TagCompression.GZIP, "");
    }

    @Test
    void testGzipNamed() throws IOException {
        checkWrite(TagCompression.GZIP, "Test");
    }

    @Test
    void testZlibUnnamed() throws IOException {
        checkWrite(TagCompression.ZLIB, "");
    }

    @Test
    void testZlibNamed() throws IOException {
        checkWrite(TagCompression.ZLIB, "Test");
    }

    private static void checkWrite(final TagCompression compression, final String name) throws IOException {
        final var inputStream = new PipedInputStream();
        final var outputStream = new PipedOutputStream(inputStream);
        final var output = new DataOutputStream(outputStream);
        TagIO.writeNamed(output, name, TAG, compression);
        final var input = new DataInputStream(compression.decompress(inputStream));
        assertEquals(CompoundTag.ID, input.readByte());
        assertEquals(name, input.readUTF());
        checkContents(input);
    }

    private static void checkContents(final DataInputStream input) throws IOException {
        assertType(input, ByteTag.ID, "byte test", () -> assertEquals(1, input.readByte()));
        assertType(input, ShortTag.ID, "short test", () -> assertEquals(25, input.readShort()));
        assertType(input, IntTag.ID, "int test", () -> assertEquals(58329, input.readInt()));
        assertType(input, LongTag.ID, "long test", () -> assertEquals(19848395L, input.readLong()));

        // We check the bits here due to inconsistencies with floating point on different platforms
        assertType(input, FloatTag.ID, "float test", () -> assertEquals(Float.floatToIntBits(0.4231535F), input.readInt()));
        assertType(input, DoubleTag.ID, "double test", () -> assertEquals(Double.doubleToLongBits(1.2136563264461), input.readLong()));

        assertType(input, StringTag.ID, "string test", () -> assertEquals("cauidsgahsgdg", input.readUTF()));
        assertType(input, ByteArrayTag.ID, "byte array test", () -> {
            assertEquals(3, input.readInt());
            assertArrayEquals(input.readNBytes(3), new byte[]{1, 4, 2});
        });
        assertType(input, IntArrayTag.ID, "int array test", () -> {
            assertEquals(3, input.readInt());
            assertEquals(8, input.readInt());
            assertEquals(9, input.readInt());
            assertEquals(2, input.readInt());
        });
        assertType(input, LongArrayTag.ID, "long array test", () -> {
            assertEquals(3, input.readInt());
            assertEquals(73, input.readLong());
            assertEquals(492, input.readLong());
            assertEquals(195, input.readLong());
        });
        assertType(input, ListTag.ID, "list test", () -> {
            assertEquals(IntTag.ID, input.readByte());
            assertEquals(3, input.readInt());
            assertEquals(1, input.readInt());
            assertEquals(74, input.readInt());
            assertEquals(493029, input.readInt());
        });
        assertType(input, CompoundTag.ID, "compound test", () -> {
            assertType(input, ListTag.ID, "nested list", () -> {
                assertEquals(LongTag.ID, input.readByte());
                assertEquals(3, input.readInt());
                assertEquals(74, input.readLong());
                assertEquals(9385, input.readLong());
                assertEquals(25412389589235L, input.readLong());
            });
            assertType(input, CompoundTag.ID, "nested compound", () -> {
                assertType(input, IntTag.ID, "nested int", () -> assertEquals(31235, input.readInt()));
                assertEquals(0, input.readByte());
            });
            assertEquals(0, input.readByte());
        });
        assertEquals(0, input.readByte());
    }

    private static void assertType(final DataInputStream input, final int type, final String name, final IORunnable action) throws IOException {
        assertEquals(type, input.readByte());
        assertEquals(name, input.readUTF());
        action.run();
    }

    @FunctionalInterface
    private interface IORunnable {

        void run() throws IOException;
    }
}
