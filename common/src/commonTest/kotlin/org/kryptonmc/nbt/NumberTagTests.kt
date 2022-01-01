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

class NumberTagTests {

    @Test
    @JsName("checkNumberConversions")
    fun `check number conversions`() {
        val double = DoubleTag.of(57.5)
        assertEquals(double.toDouble(), double.value)
        assertEquals(double.toFloat(), double.value.toFloat())
        assertEquals(double.toLong(), double.value.toInt().toLong())
        assertEquals(double.toInt(), double.value.toInt())
        assertEquals(double.toShort(), (double.value.toInt() and '\uFFFF'.code).toShort())
        assertEquals(double.toByte(), (double.value.toInt() and 255).toByte())
        val float = FloatTag.of(83.7F)
        assertEquals(float.toInt(), float.value.toInt())
        assertEquals(float.toShort(), (float.value.toInt() and 65535).toShort())
        assertEquals(float.toByte(), (float.value.toInt() and 255).toByte())
    }

    @Test
    @JsName("checkInitializations")
    fun `check initializations`() {
        assertEquals(1, ByteTag.of(true).value)
        assertEquals(0, ByteTag.of(false).value)
        assertEquals(57, IntTag.of(57).value)
        assertEquals(128, ShortTag.of(128).value)
    }
}
