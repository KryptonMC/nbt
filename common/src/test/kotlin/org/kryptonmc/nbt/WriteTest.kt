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
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream
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
    fun `test uncompressed unnamed`() {
        checkWrite(tag, TagCompression.NONE, "")
    }

    @Test
    fun `test uncompressed named`() {
        checkWrite(tag, TagCompression.NONE, "Test")
    }

    @Test
    fun `test gzip unnamed`() {
        checkWrite(tag, TagCompression.GZIP, "")
    }

    @Test
    fun `test gzip named`() {
        checkWrite(tag, TagCompression.GZIP, "Test")
    }

    @Test
    fun `test zlib unnamed`() {
        checkWrite(tag, TagCompression.ZLIB, "")
    }

    @Test
    fun `test zlib named`() {
        checkWrite(tag, TagCompression.ZLIB, "Test")
    }

    companion object {

        @JvmStatic
        private fun checkWrite(tag: CompoundTag, compression: TagCompression, name: String) {
            val inputStream = PipedInputStream()
            val outputStream = PipedOutputStream(inputStream)
            val output = DataOutputStream(outputStream)
            TagIO.writeNamed(output, name, tag, compression)
            val input = DataInputStream(compression.decompress(inputStream))
            assertEquals(CompoundTag.ID.toByte(), input.readByte())
            assertEquals(name, input.readUTF())
            checkContents(input)
        }

        @JvmStatic
        private fun checkContents(input: DataInputStream) {
            assertType(input, ByteTag.ID, "byte test") { assertEquals(1, input.readByte()) }
            assertType(input, ShortTag.ID, "short test") { assertEquals(25, input.readShort()) }
            assertType(input, IntTag.ID, "int test") { assertEquals(58329, input.readInt()) }
            assertType(input, LongTag.ID, "long test") { assertEquals(19848395L, input.readLong()) }

            // We check the bits here due to inconsistencies with floating point on different platforms
            assertType(input, FloatTag.ID, "float test") { assertEquals(0.4231535F.toBits(), input.readInt()) }
            assertType(input, DoubleTag.ID, "double test") { assertEquals(1.2136563264461.toBits(), input.readLong()) }

            assertType(input, StringTag.ID, "string test") { assertEquals("cauidsgahsgdg", input.readUTF()) }
            assertType(input, ByteArrayTag.ID, "byte array test") {
                assertEquals(3, input.readInt())
                assertContentEquals(input.readNBytes(3), byteArrayOf(1, 4, 2))
            }
            assertType(input, IntArrayTag.ID, "int array test") {
                assertEquals(3, input.readInt())
                assertEquals(8, input.readInt())
                assertEquals(9, input.readInt())
                assertEquals(2, input.readInt())
            }
            assertType(input, LongArrayTag.ID, "long array test") {
                assertEquals(3, input.readInt())
                assertEquals(73, input.readLong())
                assertEquals(492, input.readLong())
                assertEquals(195, input.readLong())
            }
            assertType(input, ListTag.ID, "list test") {
                assertEquals(IntTag.ID.toByte(), input.readByte())
                assertEquals(3, input.readInt())
                assertEquals(1, input.readInt())
                assertEquals(74, input.readInt())
                assertEquals(493029, input.readInt())
            }
            assertType(input, CompoundTag.ID, "compound test") {
                assertType(input, ListTag.ID, "nested list") {
                    assertEquals(LongTag.ID.toByte(), input.readByte())
                    assertEquals(3, input.readInt())
                    assertEquals(74, input.readLong())
                    assertEquals(9385, input.readLong())
                    assertEquals(25412389589235, input.readLong())
                }
                assertType(input, CompoundTag.ID, "nested compound") {
                    assertType(input, IntTag.ID, "nested int") { assertEquals(31235, input.readInt()) }
                    assertEquals(0, input.readByte())
                }
                assertEquals(0, input.readByte())
            }
            assertEquals(0, input.readByte())
        }

        @JvmStatic
        private fun assertType(input: DataInputStream, type: Int, name: String, action: () -> Unit) {
            assertEquals(type.toByte(), input.readByte())
            assertEquals(name, input.readUTF())
            action()
        }
    }
}
