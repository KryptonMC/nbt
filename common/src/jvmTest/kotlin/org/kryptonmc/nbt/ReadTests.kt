/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

import org.kryptonmc.nbt.io.TagCompression
import org.kryptonmc.nbt.io.TagIO
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ReadTests {

    @Test
    fun `hello world`() {
        val nbt = TagIO.readNamed(getResource("hello_world.nbt"))
        assertEquals("hello world", nbt.first)
        assertIs<CompoundTag>(nbt.second)
        val tag = nbt.second as CompoundTag
        assertTrue(tag.isNotEmpty())
        assertEquals("name", tag.keys.first())
        assertIs<StringTag>(tag.values.first())
        assertEquals("Bananrama", (tag.values.first() as StringTag).value)
    }

    @Test
    fun `big test uncompressed`() = checkBigTest(TagIO.readNamed(getResource("bigtest.nbt")))

    @Test
    fun `big test gzip`() = checkBigTest(TagIO.readNamed(getResource("bigtest_gzip.nbt"), TagCompression.GZIP))
}

private fun checkBigTest(input: Pair<String, Tag>) {
    assertEquals("Level", input.first)
    assertIs<CompoundTag>(input.second)
    val tag = input.second as CompoundTag
    assertTrue(tag.isNotEmpty())
    assertTrue(tag.contains("shortTest", ShortTag.ID))
    assertEquals(Short.MAX_VALUE, tag.getShort("shortTest"))
    assertTrue(tag.contains("longTest", LongTag.ID))
    assertEquals(Long.MAX_VALUE, tag.getLong("longTest"))
    assertTrue(tag.contains("byteTest", ByteTag.ID))
    assertEquals(Byte.MAX_VALUE, tag.getByte("byteTest"))
    assertTrue(tag.contains("byteArrayTest (the first 1000 values of (n*n*255+n*7)%100, starting with n=0 (0, 62, 34, 16, 8, ...))", ByteArrayTag.ID))
    assertTrue(tag.contains("listTest (long)", ListTag.ID))
    assertTrue(tag.getList("listTest (long)", LongTag.ID).isNotEmpty())
    assertTrue(tag.contains("floatTest", FloatTag.ID))
    assertTrue(tag.contains("doubleTest", DoubleTag.ID))
    assertTrue(tag.contains("intTest", IntTag.ID))
    assertTrue(tag.contains("listTest (compound)", ListTag.ID))
    assertTrue(tag.getList("listTest (compound)", CompoundTag.ID).isNotEmpty())
    assertEquals("Compound tag #0", (tag.getList("listTest (compound)", CompoundTag.ID)[0] as CompoundTag).getString("name"))
    assertTrue(tag.contains("nested compound test", CompoundTag.ID))
}
