/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.stream

import okio.Buffer
import okio.use
import okio.utf8Size
import org.kryptonmc.nbt.ByteArrayTag
import org.kryptonmc.nbt.ByteTag
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.DoubleTag
import org.kryptonmc.nbt.EndTag
import org.kryptonmc.nbt.FloatTag
import org.kryptonmc.nbt.IntTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.LongTag
import org.kryptonmc.nbt.ShortTag
import org.kryptonmc.nbt.StringTag
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WriteTests {

    @Test
    fun bananrama() {
        val buffer = Buffer()
        BinaryNBTWriter(buffer).use { writer ->
            writer.name("hello world")
            writer.beginCompound()
            writer.name("name")
            writer.value("Bananrama")
            writer.endCompound()
            writer.end()
        }

        // Check contents
        assertEquals(CompoundTag.ID.toByte(), buffer.readByte())
        assertEquals("hello world".length.toShort(), buffer.readShort())
        assertEquals("hello world", buffer.readUtf8("hello world".length.toLong()))
        assertEquals(StringTag.ID.toByte(), buffer.readByte())
        assertEquals("name".length.toShort(), buffer.readShort())
        assertEquals("name", buffer.readUtf8("name".length.toLong()))
        assertEquals("Bananrama".length.toShort(), buffer.readShort())
        assertEquals("Bananrama", buffer.readUtf8("Bananrama".length.toLong()))
        assertEquals(EndTag.ID.toByte(), buffer.readByte())
    }

    @Test
    @JsName("bigTest")
    fun `big test`() {
        val buffer = Buffer()
        BinaryNBTWriter(buffer).use { writer ->
            writer.name("Level")
            writer.beginCompound()
            writer.name("shortTest")
            writer.value(Short.MAX_VALUE)
            writer.name("longTest")
            writer.value(Long.MAX_VALUE)
            writer.name("byteTest")
            writer.value(Byte.MAX_VALUE)
            writer.name("byteArrayTest (the first 1000 values of (n*n*255+n*7)%100, starting with n=0 (0, 62, 34, 16, 8, ...))")
            run {
                writer.beginByteArray(1000)
                for (i in 0 until 1000) {
                    val value = (i * i * 255 + i * 7) % 100
                    writer.value(value.toByte())
                }
                writer.endByteArray()
            }
            writer.name("listTest (long)")
            run {
                writer.beginList(LongTag.ID, 5)
                for (i in 11L..15L) {
                    writer.value(i)
                }
                writer.endList()
            }
            writer.name("floatTest")
            writer.value(0.49823147F)
            writer.name("doubleTest")
            writer.value(0.4931287132182315)
            writer.name("intTest")
            writer.value(Int.MAX_VALUE)
            writer.name("listTest (compound)")
            run {
                writer.beginList(CompoundTag.ID, 2)
                run {
                    writer.beginCompound()
                    writer.name("created-on")
                    writer.value(1264099775885L)
                    writer.name("name")
                    writer.value("Compound tag #0")
                    writer.endCompound()
                }
                run {
                    writer.beginCompound()
                    writer.name("created-on")
                    writer.value(1264099775885L)
                    writer.name("name")
                    writer.value("Compound tag #1")
                    writer.endCompound()
                }
                writer.endList()
            }
            writer.name("nested compound test")
            writer.beginCompound()
            run {
                writer.name("egg")
                run {
                    writer.beginCompound()
                    writer.name("name")
                    writer.value("Eggbert")
                    writer.name("value")
                    writer.value(0.5F)
                    writer.endCompound()
                }
                writer.name("ham")
                run {
                    writer.beginCompound()
                    writer.name("name")
                    writer.value("Hampus")
                    writer.name("value")
                    writer.value(0.75F)
                    writer.endCompound()
                }
            }
            writer.endCompound()
            writer.name("stringTest")
            writer.value("HELLO WORLD THIS IS A TEST STRING ÅÄÖ!")
            writer.endCompound()
        }

        // Check written
        assertEquals(CompoundTag.ID.toByte(), buffer.readByte())
        assertEquals("Level".length.toShort(), buffer.readShort())
        assertEquals("Level", buffer.readUtf8("Level".length.toLong()))

        assertEquals(ShortTag.ID.toByte(), buffer.readByte())
        assertEquals("shortTest".length.toShort(), buffer.readShort())
        assertEquals("shortTest", buffer.readUtf8("shortTest".length.toLong()))
        assertEquals(Short.MAX_VALUE, buffer.readShort())

        assertEquals(LongTag.ID.toByte(), buffer.readByte())
        assertEquals("longTest".length.toShort(), buffer.readShort())
        assertEquals("longTest", buffer.readUtf8("longTest".length.toLong()))
        assertEquals(Long.MAX_VALUE, buffer.readLong())

        assertEquals(ByteTag.ID.toByte(), buffer.readByte())
        assertEquals("byteTest".length.toShort(), buffer.readShort())
        assertEquals("byteTest", buffer.readUtf8("byteTest".length.toLong()))
        assertEquals(Byte.MAX_VALUE, buffer.readByte())

        assertEquals(ByteArrayTag.ID.toByte(), buffer.readByte())
        val byteArrayName = "byteArrayTest (the first 1000 values of (n*n*255+n*7)%100, starting with n=0 (0, 62, 34, 16, 8, ...))"
        assertEquals(byteArrayName.length.toShort(), buffer.readShort())
        assertEquals(byteArrayName, buffer.readUtf8(byteArrayName.length.toLong()))
        assertEquals(1000, buffer.readInt())
        for (i in 0 until 1000) {
            val expected = (i * i * 255 + i * 7) % 100
            assertEquals(expected.toByte(), buffer.readByte())
        }

        assertEquals(ListTag.ID.toByte(), buffer.readByte())
        assertEquals("listTest (long)".length.toShort(), buffer.readShort())
        assertEquals("listTest (long)", buffer.readUtf8("listTest (long)".length.toLong()))
        assertEquals(LongTag.ID.toByte(), buffer.readByte())
        assertEquals(5, buffer.readInt())
        for (i in 11L..15L) {
            assertEquals(i, buffer.readLong())
        }

        assertEquals(FloatTag.ID.toByte(), buffer.readByte())
        assertEquals("floatTest".length.toShort(), buffer.readShort())
        assertEquals("floatTest", buffer.readUtf8("floatTest".length.toLong()))
        assertEquals(0.49823147F, Float.fromBits(buffer.readInt()))

        assertEquals(DoubleTag.ID.toByte(), buffer.readByte())
        assertEquals("doubleTest".length.toShort(), buffer.readShort())
        assertEquals("doubleTest", buffer.readUtf8("doubleTest".length.toLong()))
        assertEquals(0.4931287132182315, Double.fromBits(buffer.readLong()))

        assertEquals(IntTag.ID.toByte(), buffer.readByte())
        assertEquals("intTest".length.toShort(), buffer.readShort())
        assertEquals("intTest", buffer.readUtf8("intTest".length.toLong()))
        assertEquals(Int.MAX_VALUE, buffer.readInt())

        assertEquals(ListTag.ID.toByte(), buffer.readByte())
        assertEquals("listTest (compound)".length.toShort(), buffer.readShort())
        assertEquals("listTest (compound)", buffer.readUtf8("listTest (compound)".length.toLong()))

        assertEquals(CompoundTag.ID.toByte(), buffer.readByte())
        assertEquals(2, buffer.readInt())

        assertEquals(LongTag.ID.toByte(), buffer.readByte())
        assertEquals("created-on".length.toShort(), buffer.readShort())
        assertEquals("created-on", buffer.readUtf8("created-on".length.toLong()))
        assertEquals(1264099775885L, buffer.readLong())
        assertEquals(StringTag.ID.toByte(), buffer.readByte())
        assertEquals("name".length.toShort(), buffer.readShort())
        assertEquals("name", buffer.readUtf8("name".length.toLong()))
        assertEquals("Compound tag #0".length.toShort(), buffer.readShort())
        assertEquals("Compound tag #0", buffer.readUtf8("Compound tag #0".length.toLong()))
        assertEquals(EndTag.ID.toByte(), buffer.readByte())

        assertEquals(LongTag.ID.toByte(), buffer.readByte())
        assertEquals("created-on".length.toShort(), buffer.readShort())
        assertEquals("created-on", buffer.readUtf8("created-on".length.toLong()))
        assertEquals(1264099775885L, buffer.readLong())
        assertEquals(StringTag.ID.toByte(), buffer.readByte())
        assertEquals("name".length.toShort(), buffer.readShort())
        assertEquals("name", buffer.readUtf8("name".length.toLong()))
        assertEquals("Compound tag #1".length.toShort(), buffer.readShort())
        assertEquals("Compound tag #1", buffer.readUtf8("Compound tag #1".length.toLong()))
        assertEquals(EndTag.ID.toByte(), buffer.readByte())

        assertEquals(CompoundTag.ID.toByte(), buffer.readByte())
        assertEquals("nested compound test".length.toShort(), buffer.readShort())
        assertEquals("nested compound test", buffer.readUtf8("nested compound test".length.toLong()))
        assertEquals(CompoundTag.ID.toByte(), buffer.readByte())

        assertEquals("egg".length.toShort(), buffer.readShort())
        assertEquals("egg", buffer.readUtf8("egg".length.toLong()))
        assertEquals(StringTag.ID.toByte(), buffer.readByte())
        assertEquals("name".length.toShort(), buffer.readShort())
        assertEquals("name", buffer.readUtf8("name".length.toLong()))
        assertEquals("Eggbert".length.toShort(), buffer.readShort())
        assertEquals("Eggbert", buffer.readUtf8("Eggbert".length.toLong()))
        assertEquals(FloatTag.ID.toByte(), buffer.readByte())
        assertEquals("value".length.toShort(), buffer.readShort())
        assertEquals("value", buffer.readUtf8("value".length.toLong()))
        assertEquals(0.5F, Float.fromBits(buffer.readInt()))
        assertEquals(EndTag.ID.toByte(), buffer.readByte())

        assertEquals(CompoundTag.ID.toByte(), buffer.readByte())
        assertEquals("ham".length.toShort(), buffer.readShort())
        assertEquals("ham", buffer.readUtf8("ham".length.toLong()))
        assertEquals(StringTag.ID.toByte(), buffer.readByte())
        assertEquals("name".length.toShort(), buffer.readShort())
        assertEquals("name", buffer.readUtf8("name".length.toLong()))
        assertEquals("Hampus".length.toShort(), buffer.readShort())
        assertEquals("Hampus", buffer.readUtf8("Hampus".length.toLong()))
        assertEquals(FloatTag.ID.toByte(), buffer.readByte())
        assertEquals("value".length.toShort(), buffer.readShort())
        assertEquals("value", buffer.readUtf8("value".length.toLong()))
        assertEquals(0.75F, Float.fromBits(buffer.readInt()))
        assertEquals(EndTag.ID.toByte(), buffer.readByte())

        assertEquals(EndTag.ID.toByte(), buffer.readByte())
        assertEquals(StringTag.ID.toByte(), buffer.readByte())
        assertEquals("stringTest".length.toShort(), buffer.readShort())
        assertEquals("stringTest", buffer.readUtf8("stringTest".length.toLong()))
        val string = "HELLO WORLD THIS IS A TEST STRING ÅÄÖ!"
        assertEquals(string.utf8Size().toShort(), buffer.readShort())
        assertEquals(string, buffer.readUtf8(string.utf8Size()))
        assertEquals(EndTag.ID.toByte(), buffer.readByte())
        assertTrue(buffer.exhausted())
    }
}
