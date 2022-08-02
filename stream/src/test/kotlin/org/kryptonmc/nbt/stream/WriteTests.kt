/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.stream

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
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import kotlin.test.Test
import kotlin.test.assertEquals

class WriteTests {

    @Test
    fun bananrama() {
        val output = ByteArrayOutputStream()
        BinaryNBTWriter(DataOutputStream(output)).use { writer ->
            writer.name("hello world")
            writer.beginCompound()
            writer.name("name")
            writer.value("Bananrama")
            writer.endCompound()
            writer.end()
        }

        // Check contents
        val input = DataInputStream(ByteArrayInputStream(output.toByteArray()))
        assertEquals(CompoundTag.ID.toByte(), input.readByte())
        assertEquals("hello world", input.readUTF())
        assertEquals(StringTag.ID.toByte(), input.readByte())
        assertEquals("name", input.readUTF())
        assertEquals("Bananrama", input.readUTF())
        assertEquals(EndTag.ID.toByte(), input.readByte())
    }

    @Test
    fun `big test`() {
        val output = ByteArrayOutputStream()
        BinaryNBTWriter(DataOutputStream(output)).use { writer ->
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
        val input = DataInputStream(ByteArrayInputStream(output.toByteArray()))
        assertEquals(CompoundTag.ID.toByte(), input.readByte())
        assertEquals("Level", input.readUTF())

        assertEquals(ShortTag.ID.toByte(), input.readByte())
        assertEquals("shortTest", input.readUTF())
        assertEquals(Short.MAX_VALUE, input.readShort())

        assertEquals(LongTag.ID.toByte(), input.readByte())
        assertEquals("longTest", input.readUTF())
        assertEquals(Long.MAX_VALUE, input.readLong())

        assertEquals(ByteTag.ID.toByte(), input.readByte())
        assertEquals("byteTest", input.readUTF())
        assertEquals(Byte.MAX_VALUE, input.readByte())

        assertEquals(ByteArrayTag.ID.toByte(), input.readByte())
        assertEquals("byteArrayTest (the first 1000 values of (n*n*255+n*7)%100, starting with n=0 (0, 62, 34, 16, 8, ...))", input.readUTF())
        assertEquals(1000, input.readInt())
        for (i in 0 until 1000) {
            val expected = (i * i * 255 + i * 7) % 100
            assertEquals(expected.toByte(), input.readByte())
        }

        assertEquals(ListTag.ID.toByte(), input.readByte())
        assertEquals("listTest (long)", input.readUTF())
        assertEquals(LongTag.ID.toByte(), input.readByte())
        assertEquals(5, input.readInt())
        for (i in 11L..15L) {
            assertEquals(i, input.readLong())
        }

        assertEquals(FloatTag.ID.toByte(), input.readByte())
        assertEquals("floatTest", input.readUTF())
        assertEquals(0.49823147F, input.readFloat())

        assertEquals(DoubleTag.ID.toByte(), input.readByte())
        assertEquals("doubleTest", input.readUTF())
        assertEquals(0.4931287132182315, input.readDouble())

        assertEquals(IntTag.ID.toByte(), input.readByte())
        assertEquals("intTest", input.readUTF())
        assertEquals(Int.MAX_VALUE, input.readInt())

        assertEquals(ListTag.ID.toByte(), input.readByte())
        assertEquals("listTest (compound)", input.readUTF())

        assertEquals(CompoundTag.ID.toByte(), input.readByte())
        assertEquals(2, input.readInt())

        assertEquals(LongTag.ID.toByte(), input.readByte())
        assertEquals("created-on", input.readUTF())
        assertEquals(1264099775885L, input.readLong())
        assertEquals(StringTag.ID.toByte(), input.readByte())
        assertEquals("name", input.readUTF())
        assertEquals("Compound tag #0", input.readUTF())
        assertEquals(EndTag.ID.toByte(), input.readByte())

        assertEquals(LongTag.ID.toByte(), input.readByte())
        assertEquals("created-on", input.readUTF())
        assertEquals(1264099775885L, input.readLong())
        assertEquals(StringTag.ID.toByte(), input.readByte())
        assertEquals("name", input.readUTF())
        assertEquals("Compound tag #1", input.readUTF())
        assertEquals(EndTag.ID.toByte(), input.readByte())

        assertEquals(CompoundTag.ID.toByte(), input.readByte())
        assertEquals("nested compound test", input.readUTF())
        assertEquals(CompoundTag.ID.toByte(), input.readByte())

        assertEquals("egg", input.readUTF())
        assertEquals(StringTag.ID.toByte(), input.readByte())
        assertEquals("name", input.readUTF())
        assertEquals("Eggbert", input.readUTF())
        assertEquals(FloatTag.ID.toByte(), input.readByte())
        assertEquals("value", input.readUTF())
        assertEquals(0.5F, input.readFloat())
        assertEquals(EndTag.ID.toByte(), input.readByte())

        assertEquals(CompoundTag.ID.toByte(), input.readByte())
        assertEquals("ham", input.readUTF())
        assertEquals(StringTag.ID.toByte(), input.readByte())
        assertEquals("name", input.readUTF())
        assertEquals("Hampus", input.readUTF())
        assertEquals(FloatTag.ID.toByte(), input.readByte())
        assertEquals("value", input.readUTF())
        assertEquals(0.75F, input.readFloat())
        assertEquals(EndTag.ID.toByte(), input.readByte())

        assertEquals(EndTag.ID.toByte(), input.readByte())
        assertEquals(StringTag.ID.toByte(), input.readByte())
        assertEquals("stringTest", input.readUTF())
        assertEquals("HELLO WORLD THIS IS A TEST STRING ÅÄÖ!", input.readUTF())
        assertEquals(EndTag.ID.toByte(), input.readByte())
        assertEquals(0, input.available())
    }
}
