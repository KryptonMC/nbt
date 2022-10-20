/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class NumberTagTests {

    @Test
    void checkNumberConversions() {
        final var doubleTag = DoubleTag.of(57.5);
        assertEquals(doubleTag.toDouble(), doubleTag.value());
        assertEquals(doubleTag.toFloat(), (float) doubleTag.value());
        assertEquals(doubleTag.toLong(), (long) Math.floor(doubleTag.value()));
        assertEquals(doubleTag.toInt(), FloorMath.floor(doubleTag.value()));
        assertEquals(doubleTag.toShort(), (short) (FloorMath.floor(doubleTag.value()) & 0xFFFF));
        assertEquals(doubleTag.toByte(), (byte) (FloorMath.floor(doubleTag.value()) & 0xFF));
        final var floatTag = FloatTag.of(83.7F);
        assertEquals(floatTag.toLong(), (long) floatTag.value());
        assertEquals(floatTag.toInt(), FloorMath.floor(floatTag.value()));
        assertEquals(floatTag.toShort(), (short) (FloorMath.floor(floatTag.value()) & 0xFFFF));
        assertEquals(floatTag.toByte(), (byte) (FloorMath.floor(floatTag.value()) & 0xFF));
    }

    @Test
    void checkInitializations() {
        assertEquals(1, ByteTag.of(true).value());
        assertEquals(0, ByteTag.of(false).value());
        assertEquals(57, IntTag.of(57).value());
        assertEquals(128, ShortTag.of((short) 128).value());
    }
}
