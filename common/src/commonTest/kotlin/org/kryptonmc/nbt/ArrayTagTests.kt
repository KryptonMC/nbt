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
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ArrayTagTests {

    @Test
    @JsName("testEquality")
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
}
