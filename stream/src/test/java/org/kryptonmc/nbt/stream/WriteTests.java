/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.stream;

import org.junit.jupiter.api.Test;
import org.kryptonmc.nbt.ByteArrayTag;
import org.kryptonmc.nbt.ByteTag;
import org.kryptonmc.nbt.CompoundTag;
import org.kryptonmc.nbt.DoubleTag;
import org.kryptonmc.nbt.EndTag;
import org.kryptonmc.nbt.FloatTag;
import org.kryptonmc.nbt.IntTag;
import org.kryptonmc.nbt.ListTag;
import org.kryptonmc.nbt.LongTag;
import org.kryptonmc.nbt.ShortTag;
import org.kryptonmc.nbt.StringTag;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class WriteTests extends AbstractStreamingTest {

    @Test
    void bananrama() {
        doWriteAndRead(writer -> {
            writer.name("hello world");
            writer.beginCompound();
            writer.name("name");
            writer.value("Bananrama");
            writer.endCompound();
            writer.end();
        }, input -> {
            assertEquals(CompoundTag.ID, input.readByte());
            assertEquals("hello world", input.readUTF());
            assertEquals(StringTag.ID, input.readByte());
            assertEquals("name", input.readUTF());
            assertEquals("Bananrama", input.readUTF());
            assertEquals(EndTag.ID, input.readByte());
        });
    }

    @Test
    void bigTest() {
        doWriteAndRead(writer -> {
            writer.name("Level");
            writer.beginCompound();
            writer.name("shortTest");
            writer.value(Short.MAX_VALUE);
            writer.name("longTest");
            writer.value(Long.MAX_VALUE);
            writer.name("byteTest");
            writer.value(Byte.MAX_VALUE);
            writer.name("byteArrayTest (the first 1000 values of (n*n*255+n*7)%100, starting with n=0 (0, 62, 34, 16, 8, ...))");
            {
                writer.beginByteArray(1000);
                for (int i = 0; i < 1000; i++) {
                    writer.value((byte) ((i * i * 255 + i * 7) % 100));
                }
                writer.endByteArray();
            }
            writer.name("listTest (long)");
            {
                writer.beginList(LongTag.ID, 5);
                for (long i = 11L; i <= 15L; i++) {
                    writer.value(i);
                }
                writer.endList();
            }
            writer.name("floatTest");
            writer.value(0.49823147F);
            writer.name("doubleTest");
            writer.value(0.4931287132182315);
            writer.name("intTest");
            writer.value(Integer.MAX_VALUE);
            writer.name("listTest (compound)");
            {
                writer.beginList(CompoundTag.ID, 2);
                {
                    writer.beginCompound();
                    writer.name("created-on");
                    writer.value(1264099775885L);
                    writer.name("name");
                    writer.value("Compound tag #0");
                    writer.endCompound();
                }
                {
                    writer.beginCompound();
                    writer.name("created-on");
                    writer.value(1264099775885L);
                    writer.name("name");
                    writer.value("Compound tag #1");
                    writer.endCompound();
                }
                writer.endList();
            }
            writer.name("nested compound test");
            writer.beginCompound();
            {
                writer.name("egg");
                {
                    writer.beginCompound();
                    writer.name("name");
                    writer.value("Eggbert");
                    writer.name("value");
                    writer.value(0.5F);
                    writer.endCompound();
                }
                writer.name("ham");
                {
                    writer.beginCompound();
                    writer.name("name");
                    writer.value("Hampus");
                    writer.name("value");
                    writer.value(0.75F);
                    writer.endCompound();
                }
            }
            writer.endCompound();
            writer.name("stringTest");
            writer.value("HELLO WORLD THIS IS A TEST STRING ÅÄÖ!");
            writer.endCompound();
        }, input -> {
            assertEquals(CompoundTag.ID, input.readByte());
            assertEquals("Level", input.readUTF());

            assertEquals(ShortTag.ID, input.readByte());
            assertEquals("shortTest", input.readUTF());
            assertEquals(Short.MAX_VALUE, input.readShort());

            assertEquals(LongTag.ID, input.readByte());
            assertEquals("longTest", input.readUTF());
            assertEquals(Long.MAX_VALUE, input.readLong());

            assertEquals(ByteTag.ID, input.readByte());
            assertEquals("byteTest", input.readUTF());
            assertEquals(Byte.MAX_VALUE, input.readByte());

            assertEquals(ByteArrayTag.ID, input.readByte());
            assertEquals("byteArrayTest (the first 1000 values of (n*n*255+n*7)%100, starting with n=0 (0, 62, 34, 16, 8, ...))", input.readUTF());
            assertEquals(1000, input.readInt());
            for (int i = 0; i < 1000; i++) {
                assertEquals((i * i * 255 + i * 7) % 100, input.readByte());
            }

            assertEquals(ListTag.ID, input.readByte());
            assertEquals("listTest (long)", input.readUTF());
            assertEquals(LongTag.ID, input.readByte());
            assertEquals(5, input.readInt());
            for (long i = 11L; i <= 15L; i++) {
                assertEquals(i, input.readLong());
            }

            assertEquals(FloatTag.ID, input.readByte());
            assertEquals("floatTest", input.readUTF());
            assertEquals(0.49823147F, input.readFloat());

            assertEquals(DoubleTag.ID, input.readByte());
            assertEquals("doubleTest", input.readUTF());
            assertEquals(0.4931287132182315, input.readDouble());

            assertEquals(IntTag.ID, input.readByte());
            assertEquals("intTest", input.readUTF());
            assertEquals(Integer.MAX_VALUE, input.readInt());

            assertEquals(ListTag.ID, input.readByte());
            assertEquals("listTest (compound)", input.readUTF());

            assertEquals(CompoundTag.ID, input.readByte());
            assertEquals(2, input.readInt());

            assertEquals(LongTag.ID, input.readByte());
            assertEquals("created-on", input.readUTF());
            assertEquals(1264099775885L, input.readLong());
            assertEquals(StringTag.ID, input.readByte());
            assertEquals("name", input.readUTF());
            assertEquals("Compound tag #0", input.readUTF());
            assertEquals(EndTag.ID, input.readByte());

            assertEquals(LongTag.ID, input.readByte());
            assertEquals("created-on", input.readUTF());
            assertEquals(1264099775885L, input.readLong());
            assertEquals(StringTag.ID, input.readByte());
            assertEquals("name", input.readUTF());
            assertEquals("Compound tag #1", input.readUTF());
            assertEquals(EndTag.ID, input.readByte());

            assertEquals(CompoundTag.ID, input.readByte());
            assertEquals("nested compound test", input.readUTF());
            assertEquals(CompoundTag.ID, input.readByte());

            assertEquals("egg", input.readUTF());
            assertEquals(StringTag.ID, input.readByte());
            assertEquals("name", input.readUTF());
            assertEquals("Eggbert", input.readUTF());
            assertEquals(FloatTag.ID, input.readByte());
            assertEquals("value", input.readUTF());
            assertEquals(0.5F, input.readFloat());
            assertEquals(EndTag.ID, input.readByte());

            assertEquals(CompoundTag.ID, input.readByte());
            assertEquals("ham", input.readUTF());
            assertEquals(StringTag.ID, input.readByte());
            assertEquals("name", input.readUTF());
            assertEquals("Hampus", input.readUTF());
            assertEquals(FloatTag.ID, input.readByte());
            assertEquals("value", input.readUTF());
            assertEquals(0.75F, input.readFloat());
            assertEquals(EndTag.ID, input.readByte());

            assertEquals(EndTag.ID, input.readByte());
            assertEquals(StringTag.ID, input.readByte());
            assertEquals("stringTest", input.readUTF());
            assertEquals("HELLO WORLD THIS IS A TEST STRING ÅÄÖ!", input.readUTF());
            assertEquals(EndTag.ID, input.readByte());
            assertEquals(0, input.available());
        });
    }
}
