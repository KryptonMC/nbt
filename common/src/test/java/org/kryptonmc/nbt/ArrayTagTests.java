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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

final class ArrayTagTests {

    @Test
    void testEquality() {
        final var byteArrayOne = ByteArrayTag.of(new byte[]{1, 2});
        final var byteArrayTwo = ByteArrayTag.of(new byte[]{1, 2});
        assertEquals(byteArrayOne, byteArrayTwo);
        assertEquals(byteArrayOne.size(), byteArrayOne.copy().size());
        assertArrayEquals(byteArrayOne.getData(), byteArrayOne.copy().getData());
        assertEquals(byteArrayOne.size(), byteArrayOne.getData().length);
        final var intArrayOne = IntArrayTag.of(new int[]{3, 4});
        final var intArrayTwo = IntArrayTag.of(new int[]{3, 4});
        assertEquals(intArrayOne, intArrayTwo);
        assertEquals(intArrayOne.size(), intArrayTwo.copy().size());
        assertArrayEquals(intArrayOne.getData(), intArrayOne.copy().getData());
        assertEquals(intArrayOne.size(), intArrayOne.getData().length);
        final var longArrayOne = LongArrayTag.of(new long[]{5, 6});
        final var longArrayTwo = LongArrayTag.of(new long[]{5, 6});
        assertEquals(longArrayOne, longArrayTwo);
        assertEquals(longArrayOne.size(), longArrayTwo.copy().size());
        assertArrayEquals(longArrayOne.getData(), longArrayOne.copy().getData());
        assertEquals(longArrayOne.size(), longArrayOne.getData().length);
    }
}
