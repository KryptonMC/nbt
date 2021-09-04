/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

import okio.Buffer
import okio.BufferedSource
import okio.utf8Size
import org.kryptonmc.nbt.io.TagCompression
import org.kryptonmc.nbt.io.TagIO
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class WriteTest {

    private val tag = compound {
        byte("byte test", 1)
        short("short test", 25)
        int("int test", 58329)
        long("long test", 19848395)
        float("float test", 0.4231535F)
        double("double test", 1.2136563264461)
        string("string test", "cauidsgahsgdg")
        bytes("byte array test", 1, 4, 2)
        ints("int array test", 8, 9, 2)
        longs("long array test", 73, 492, 195)
        list("list test", IntTag.ID, IntTag.of(1), IntTag.of(74), IntTag.of(493029))
        compound("compound test") {
            list("nested list", LongTag.ID, LongTag.of(74), LongTag.of(9385), LongTag.of(25412389589235))
            compound("nested compound") {
                int("nested int", 31235)
            }
        }
    }

    @Test
    fun `test uncompressed unnamed write`() = checkWrite(TagCompression.NONE, "")

    @Test
    fun `test uncompressed named write`() = checkWrite(TagCompression.NONE, "Test")

    @Test
    fun `test gzip unnamed write`() = checkWrite(TagCompression.GZIP, "")

    @Test
    fun `test gzip named write`() = checkWrite(TagCompression.GZIP, "Test")

    @Test
    fun `test zlib unnamed write`() = checkWrite(TagCompression.ZLIB, "")

    @Test
    fun `test zlib named write`() = checkWrite(TagCompression.ZLIB, "Test")

    private fun checkWrite(compression: TagCompression, name: String) {
        val buffer = Buffer()
        TagIO.writeNamed(buffer, name, tag, compression)
        val input = compression.decompress(buffer)
        assertEquals(CompoundTag.ID.toByte(), input.readByte())
        assertEquals(name.utf8Size().toShort(), input.readShort())
        assertEquals(name, input.readUtf8(name.utf8Size()))
        input.checkContents()
    }
}

private fun BufferedSource.checkContents() {
    assertType(ByteTag.ID, "byte test") { assertEquals(1, readByte()) }
    assertType(ShortTag.ID, "short test") { assertEquals(25, readShort()) }
    assertType(IntTag.ID, "int test") { assertEquals(58329, readInt()) }
    assertType(LongTag.ID, "long test") { assertEquals(19848395L, readLong()) }
    assertType(FloatTag.ID, "float test") { assertEquals(0.4231535F, Float.fromBits(readInt())) }
    assertType(DoubleTag.ID, "double test") { assertEquals(1.2136563264461, Double.fromBits(readLong())) }
    assertType(StringTag.ID, "string test") { assertEquals("cauidsgahsgdg", readUtf8(readShort().toLong())) }
    assertType(ByteArrayTag.ID, "byte array test") {
        assertEquals(3, readInt())
        assertContentEquals(readByteArray(3), byteArrayOf(1, 4, 2))
    }
    assertType(IntArrayTag.ID, "int array test") {
        assertEquals(3, readInt())
        assertEquals(8, readInt())
        assertEquals(9, readInt())
        assertEquals(2, readInt())
    }
    assertType(LongArrayTag.ID, "long array test") {
        assertEquals(3, readInt())
        assertEquals(73, readLong())
        assertEquals(492, readLong())
        assertEquals(195, readLong())
    }
    assertType(ListTag.ID, "list test") {
        assertEquals(IntTag.ID.toByte(), readByte())
        assertEquals(3, readInt())
        assertEquals(1, readInt())
        assertEquals(74, readInt())
        assertEquals(493029, readInt())
    }
    assertType(CompoundTag.ID, "compound test") {
        assertType(ListTag.ID, "nested list") {
            assertEquals(LongTag.ID.toByte(), readByte())
            assertEquals(3, readInt())
            assertEquals(74, readLong())
            assertEquals(9385, readLong())
            assertEquals(25412389589235, readLong())
        }
        assertType(CompoundTag.ID, "nested compound") {
            assertType(IntTag.ID, "nested int") { assertEquals(31235, readInt()) }
            assertEquals(0, readByte())
        }
        assertEquals(0, readByte())
    }
    assertEquals(0, readByte())
}

private fun BufferedSource.assertType(type: Int, name: String, action: () -> Unit) {
    assertEquals(type.toByte(), readByte())
    assertEquals(name, readUtf8(readShort().toLong()))
    action()
}
