/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class ExaminationTests {

    @Test
    @JsName("examineNumbers")
    fun `examine numbers`() {
        assertEquals("END", StringTagExaminer().examine(EndTag))
        assertEquals("30b", StringTagExaminer().examine(ByteTag.of(30)))
        assertEquals("48s", StringTagExaminer().examine(ShortTag.of(48)))
        assertEquals("1893", StringTagExaminer().examine(IntTag.of(1893)))
        assertEquals("179257325L", StringTagExaminer().examine(LongTag.of(179257325)))
        assertEquals("89.483f", StringTagExaminer().examine(FloatTag.of(89.483F)))
        assertEquals("7832.94753256432d", StringTagExaminer().examine(DoubleTag.of(7832.94753256432)))
    }

    @Test
    @JsName("examineArrays")
    fun `examine arrays`() {
        assertEquals("[B;1B,8B,3B,25B]", StringTagExaminer().examine(ByteArrayTag(byteArrayOf(1, 8, 3, 25))))
        assertEquals("[I;5,93,28,47]", StringTagExaminer().examine(IntArrayTag(intArrayOf(5, 93, 28, 47))))
        assertEquals("[L;84,983,42,93]", StringTagExaminer().examine(LongArrayTag(longArrayOf(84, 983, 42, 93))))
    }

    @Test
    @JsName("examineStrings")
    fun `examine strings`() {
        assertEquals("\"hello world\"", StringTagExaminer().examine(StringTag.of("hello world")))
        assertEquals("\"hello_world\"", StringTagExaminer().examine(StringTag.of("hello_world")))
        assertEquals("'hello\"world'", StringTagExaminer().examine(StringTag.of("hello\"world")))
    }

    @Test
    @JsName("examineList")
    fun `examine list`() {
        assertEquals("[\"hello_world\",\"goodbye_world\"]", StringTagExaminer().examine(ListTag.immutable(
            listOf(StringTag.of("hello_world"), StringTag.of("goodbye_world")),
            StringTag.ID
        )))
    }

    @Test
    @JsName("examineCompound")
    fun `examine compound`() {
        assertEquals("{goodbye_world:8L,hello_world:1b}", StringTagExaminer().examine(CompoundTag.immutable(
            mapOf("hello_world" to ByteTag.of(1), "goodbye_world" to LongTag.of(8))
        )))
    }
}
