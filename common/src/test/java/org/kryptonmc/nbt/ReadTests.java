/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.kryptonmc.nbt.io.NamedTag;
import org.kryptonmc.nbt.io.TagCompression;
import org.kryptonmc.nbt.io.TagIO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class ReadTests {

    @Test
    void helloWorld() throws IOException {
        final var nbt = TagIO.readNamed(getResource("hello_world.nbt"), TagCompression.NONE);
        assertEquals("hello world", nbt.name());
        final var tag = assertInstanceOf(CompoundTag.class, nbt.tag());
        assertFalse(tag.getData().isEmpty());
        assertEquals("name", tag.keySet().iterator().next());
        assertEquals("Bananrama", assertInstanceOf(StringTag.class, tag.values().iterator().next()).value());
    }

    @Test
    void bigTestUncompressed() throws IOException {
        checkBigTest(TagIO.readNamed(getResource("bigtest.nbt"), TagCompression.NONE));
    }

    @Test
    void bigTestGzip() throws IOException {
        checkBigTest(TagIO.readNamed(getResource("bigtest_gzip.nbt"), TagCompression.GZIP));
    }

    private static InputStream getResource(final String name) {
        return Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(name),
                "Could not find resource " + name + "!");
    }

    private static void checkBigTest(final NamedTag input) {
        assertEquals("Level", input.name());
        final var tag = assertInstanceOf(CompoundTag.class, input.tag());
        assertFalse(tag.getData().isEmpty());
        assertTrue(tag.contains("shortTest", ShortTag.ID));
        assertEquals(Short.MAX_VALUE, tag.getShort("shortTest"));
        assertTrue(tag.contains("longTest", LongTag.ID));
        assertEquals(Long.MAX_VALUE, tag.getLong("longTest"));
        assertTrue(tag.contains("byteTest", ByteTag.ID));
        assertEquals(Byte.MAX_VALUE, tag.getByte("byteTest"));
        final String byteArrayTestName = "byteArrayTest (the first 1000 values of (n*n*255+n*7)%100, starting with n=0 (0, 62, 34, 16, 8, ...))";
        assertTrue(tag.contains(byteArrayTestName, ByteArrayTag.ID));
        assertTrue(tag.contains("listTest (long)", ListTag.ID));
        assertFalse(tag.getList("listTest (long)", LongTag.ID).getData().isEmpty());
        assertTrue(tag.contains("floatTest", FloatTag.ID));
        assertTrue(tag.contains("doubleTest", DoubleTag.ID));
        assertTrue(tag.contains("intTest", IntTag.ID));
        assertTrue(tag.contains("listTest (compound)", ListTag.ID));
        assertFalse(tag.getList("listTest (compound)", CompoundTag.ID).getData().isEmpty());
        assertEquals("Compound tag #0", tag.getList("listTest (compound)", CompoundTag.ID).getCompound(0).getString("name"));
        assertTrue(tag.contains("nested compound test", CompoundTag.ID));
    }
}
