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
import kotlin.js.JsName
import kotlin.jvm.JvmStatic
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
    @JsName("testUncompressedUnnamed")
    fun `test uncompressed unnamed`() {
        checkWrite(tag, TagCompression.NONE, "")
    }

    @Test
    @JsName("testUncompressedNamed")
    fun `test uncompressed named`() {
        checkWrite(tag, TagCompression.NONE, "Test")
    }

    @Test
    @JsName("testGzipUnnamed")
    fun `test gzip unnamed`() {
        checkWrite(tag, TagCompression.GZIP, "")
    }

    @Test
    @JsName("testGzipNamed")
    fun `test gzip named`() {
        checkWrite(tag, TagCompression.GZIP, "Test")
    }

    @Test
    @JsName("testZlibUnnamed")
    fun `test zlib unnamed`() {
        checkWrite(tag, TagCompression.ZLIB, "")
    }

    @Test
    @JsName("testZlibNamed")
    fun `test zlib named`() {
        checkWrite(tag, TagCompression.ZLIB, "Test")
    }

    companion object {

        @JvmStatic
        private fun checkWrite(tag: CompoundTag, compression: TagCompression, name: String) {
            val buffer = Buffer()
            TagIO.writeNamed(buffer, name, tag, compression)
            val input = compression.decompress(buffer)
            assertEquals(CompoundTag.ID.toByte(), input.readByte())
            assertEquals(name.utf8Size().toShort(), input.readShort())
            assertEquals(name, input.readUtf8(name.utf8Size()))
            checkContents(input)
        }

        @JvmStatic
        private fun checkContents(source: BufferedSource) {
            assertType(source, ByteTag.ID, "byte test") { assertEquals(1, source.readByte()) }
            assertType(source, ShortTag.ID, "short test") { assertEquals(25, source.readShort()) }
            assertType(source, IntTag.ID, "int test") { assertEquals(58329, source.readInt()) }
            assertType(source, LongTag.ID, "long test") { assertEquals(19848395L, source.readLong()) }

            // We check the bits here due to inconsistencies with floating point on different platforms
            assertType(source, FloatTag.ID, "float test") { assertEquals(0.4231535F.toBits(), source.readInt()) }
            assertType(source, DoubleTag.ID, "double test") { assertEquals(1.2136563264461.toBits(), source.readLong()) }

            assertType(source, StringTag.ID, "string test") { assertEquals("cauidsgahsgdg", source.readUtf8(source.readShort().toLong())) }
            assertType(source, ByteArrayTag.ID, "byte array test") {
                assertEquals(3, source.readInt())
                assertContentEquals(source.readByteArray(3), byteArrayOf(1, 4, 2))
            }
            assertType(source, IntArrayTag.ID, "int array test") {
                assertEquals(3, source.readInt())
                assertEquals(8, source.readInt())
                assertEquals(9, source.readInt())
                assertEquals(2, source.readInt())
            }
            assertType(source, LongArrayTag.ID, "long array test") {
                assertEquals(3, source.readInt())
                assertEquals(73, source.readLong())
                assertEquals(492, source.readLong())
                assertEquals(195, source.readLong())
            }
            assertType(source, ListTag.ID, "list test") {
                assertEquals(IntTag.ID.toByte(), source.readByte())
                assertEquals(3, source.readInt())
                assertEquals(1, source.readInt())
                assertEquals(74, source.readInt())
                assertEquals(493029, source.readInt())
            }
            assertType(source, CompoundTag.ID, "compound test") {
                assertType(source, ListTag.ID, "nested list") {
                    assertEquals(LongTag.ID.toByte(), source.readByte())
                    assertEquals(3, source.readInt())
                    assertEquals(74, source.readLong())
                    assertEquals(9385, source.readLong())
                    assertEquals(25412389589235, source.readLong())
                }
                assertType(source, CompoundTag.ID, "nested compound") {
                    assertType(source, IntTag.ID, "nested int") { assertEquals(31235, source.readInt()) }
                    assertEquals(0, source.readByte())
                }
                assertEquals(0, source.readByte())
            }
            assertEquals(0, source.readByte())
        }

        @JvmStatic
        private fun assertType(source: BufferedSource, type: Int, name: String, action: () -> Unit) {
            assertEquals(type.toByte(), source.readByte())
            assertEquals(name, source.readUtf8(source.readShort().toLong()))
            action()
        }
    }
}
