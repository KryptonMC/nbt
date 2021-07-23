package org.kryptonmc.nbt

import org.kryptonmc.nbt.io.TagIO
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class UncompressedReadTest {

    @Test
    fun `hello world`() {
        val path = Path.of(Thread.currentThread().contextClassLoader.getResource("hello_world.nbt")!!.toURI())
        val nbt = TagIO.readNamed(path)
        assertEquals("hello world", nbt.first)
        assertIs<CompoundTag>(nbt.second)
        val tag = nbt.second as CompoundTag
        assert(tag.isNotEmpty())
        assertEquals("name", tag.keys.first())
        assertIs<StringTag>(tag.values.first())
        assertEquals("Bananrama", (tag.values.first() as StringTag).value)
    }

    @Test
    fun `big test`() {
        val path = Path.of(Thread.currentThread().contextClassLoader.getResource("bigtest.nbt")!!.toURI())
        val nbt = TagIO.readNamed(path)
        assertEquals("Level", nbt.first)
        assertIs<CompoundTag>(nbt.second)
        val tag = nbt.second as CompoundTag
        assert(tag.isNotEmpty())
        assert(tag.contains("shortTest", ShortTag.ID))
        assertEquals(Short.MAX_VALUE, tag.getShort("shortTest"))
        assert(tag.contains("longTest", LongTag.ID))
        assertEquals(Long.MAX_VALUE, tag.getLong("longTest"))
        assert(tag.contains("byteTest", ByteTag.ID))
        assertEquals(Byte.MAX_VALUE, tag.getByte("byteTest"))
        assert(tag.contains("byteArrayTest (the first 1000 values of (n*n*255+n*7)%100, starting with n=0 (0, 62, 34, 16, 8, ...))", ByteArrayTag.ID))
        assert(tag.contains("listTest (long)", ListTag.ID))
        assert(tag.getList("listTest (long)", LongTag.ID).isNotEmpty())
        assert(tag.contains("floatTest", FloatTag.ID))
        assert(tag.contains("doubleTest", DoubleTag.ID))
        assert(tag.contains("intTest", IntTag.ID))
        assert(tag.contains("listTest (compound)", ListTag.ID))
        assert(tag.getList("listTest (compound)", CompoundTag.ID).isNotEmpty())
        assertEquals("Compound tag #0", (tag.getList("listTest (compound)", CompoundTag.ID)[0] as CompoundTag).getString("name"))
        assert(tag.contains("nested compound test", CompoundTag.ID))
    }
}
