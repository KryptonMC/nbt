/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ArrayTagTests {

    @Test
    fun `test set tag`() {
        val byteArray = ByteArrayTag(ByteArray(2))
        assertFalse(byteArray.setTag(0, StringTag.EMPTY))
        assertTrue(byteArray.setTag(0, ByteTag.ZERO))
        val intArray = IntArrayTag(IntArray(2))
        assertFalse(intArray.setTag(0, StringTag.EMPTY))
        assertTrue(intArray.setTag(0, IntTag.ZERO))
        val longArray = LongArrayTag(LongArray(2))
        assertFalse(longArray.setTag(0, StringTag.EMPTY))
        assertTrue(longArray.setTag(0, LongTag.ZERO))
    }

    @Test
    fun `test add tag`() {
        val byteArray = ByteArrayTag(ByteArray(2))
        assertFalse(byteArray.addTag(0, StringTag.EMPTY))
        assertTrue(byteArray.addTag(0, ByteTag.ZERO))
        val intArray = IntArrayTag(IntArray(2))
        assertFalse(intArray.addTag(0, StringTag.EMPTY))
        assertTrue(intArray.addTag(0, IntTag.ZERO))
        val longArray = LongArrayTag(LongArray(2))
        assertFalse(longArray.addTag(0, StringTag.EMPTY))
        assertTrue(longArray.addTag(0, LongTag.ZERO))
    }

    @Test
    fun `test equality`() {
        val byteArrayOne = ByteArrayTag(byteArrayOf(1, 2))
        val byteArrayTwo = ByteArrayTag(byteArrayOf(1, 2))
        assertEquals(byteArrayOne, byteArrayTwo)
        assertEquals(byteArrayOne.size, byteArrayOne.copy().size)
        assertTrue(byteArrayOne.data.contentEquals(byteArrayOne.copy().data))
        assertEquals(byteArrayOne.size, byteArrayOne.data.size)
        val intArrayOne = IntArrayTag(intArrayOf(3, 4))
        val intArrayTwo = IntArrayTag(intArrayOf(3, 4))
        assertEquals(intArrayOne, intArrayTwo)
        assertEquals(intArrayOne.size, intArrayTwo.copy().size)
        assertTrue(intArrayOne.data.contentEquals(intArrayOne.copy().data))
        assertEquals(intArrayOne.size, intArrayOne.data.size)
        val longArrayOne = LongArrayTag(longArrayOf(5, 6))
        val longArrayTwo = LongArrayTag(longArrayOf(5, 6))
        assertEquals(longArrayOne, longArrayTwo)
        assertEquals(longArrayOne.size, longArrayTwo.copy().size)
        assertTrue(longArrayOne.data.contentEquals(longArrayOne.copy().data))
        assertEquals(longArrayOne.size, longArrayOne.data.size)
    }

    @Test
    fun `check element types`() {
        assertEquals(ByteTag.ID, ByteArrayTag(ByteArray(0)).elementType)
        assertEquals(IntTag.ID, IntArrayTag(IntArray(0)).elementType)
        assertEquals(LongTag.ID, LongArrayTag(LongArray(0)).elementType)
    }
}
