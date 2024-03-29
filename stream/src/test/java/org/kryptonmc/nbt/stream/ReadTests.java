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
import org.kryptonmc.nbt.CompoundTag;
import org.kryptonmc.nbt.LongTag;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class ReadTests extends AbstractStreamingTest {

    @Test
    void bananrama() {
        writeAndRead(writer -> {
            writer.name("hello world");
            writer.beginCompound();
            writer.name("name");
            writer.value("Bananrama");
            writer.endCompound();
        }, reader -> {
            assertEquals("hello world", reader.nextName());
            reader.beginCompound();
            assertEquals("name", reader.nextName());
            assertEquals("Bananrama", reader.nextString());
            reader.endCompound();
        });
    }

    @Test
    void bigTest() {
        writeAndRead(writer -> {
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
            writer.value(0.4931287132182315D);
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
        }, reader -> {
            assertEquals("Level", reader.nextName());
            reader.beginCompound();
            assertEquals("shortTest", reader.nextName());
            assertEquals(Short.MAX_VALUE, reader.nextShort());
            assertEquals("longTest", reader.nextName());
            assertEquals(Long.MAX_VALUE, reader.nextLong());
            assertEquals("byteTest", reader.nextName());
            assertEquals(Byte.MAX_VALUE, reader.nextByte());
            assertEquals("byteArrayTest (the first 1000 values of (n*n*255+n*7)%100, starting with n=0 (0, 62, 34, 16, 8, ...))", reader.nextName());
            assertEquals(1000, reader.beginByteArray());
            for (int i = 0; i < 1000; i++) {
                assertEquals((i * i * 255 + i * 7) % 100, reader.nextByte());
            }
            reader.endByteArray();
            assertEquals("listTest (long)", reader.nextName());
            assertEquals(5, reader.beginList(LongTag.ID));
            for (long i = 11L; i <= 15L; i++) {
                assertEquals(i, reader.nextLong());
            }
            reader.endList();
            assertEquals("floatTest", reader.nextName());
            assertEquals(0.49823147F, reader.nextFloat());
            assertEquals("doubleTest", reader.nextName());
            assertEquals(0.4931287132182315D, reader.nextDouble());
            assertEquals("intTest", reader.nextName());
            assertEquals(Integer.MAX_VALUE, reader.nextInt());
            assertEquals("listTest (compound)", reader.nextName());
            assertEquals(2, reader.beginList(CompoundTag.ID));
            reader.beginCompound();
            assertEquals("created-on", reader.nextName());
            assertEquals(1264099775885L, reader.nextLong());
            assertEquals("name", reader.nextName());
            assertEquals("Compound tag #0", reader.nextString());
            reader.endCompound();
            reader.beginCompound();
            assertEquals("created-on", reader.nextName());
            assertEquals(1264099775885L, reader.nextLong());
            assertEquals("name", reader.nextName());
            assertEquals("Compound tag #1", reader.nextString());
            reader.endCompound();
            reader.endList();
            assertEquals("nested compound test", reader.nextName());
            reader.beginCompound();
            assertEquals("egg", reader.nextName());
            reader.beginCompound();
            assertEquals("name", reader.nextName());
            assertEquals("Eggbert", reader.nextString());
            assertEquals("value", reader.nextName());
            assertEquals(0.5F, reader.nextFloat());
            reader.endCompound();
            assertEquals("ham", reader.nextName());
            reader.beginCompound();
            assertEquals("name", reader.nextName());
            assertEquals("Hampus", reader.nextString());
            assertEquals("value", reader.nextName());
            assertEquals(0.75F, reader.nextFloat());
            reader.endCompound();
            reader.endCompound();
            assertEquals("stringTest", reader.nextName());
            assertEquals("HELLO WORLD THIS IS A TEST STRING ÅÄÖ!", reader.nextString());
            reader.endCompound();
        });
    }
}
