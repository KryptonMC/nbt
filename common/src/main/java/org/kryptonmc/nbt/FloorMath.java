/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt;

final class FloorMath {

    static int floor(final float value) {
        final var intValue = (int) value;
        return value < intValue ? intValue - 1 : intValue;
    }

    static int floor(final double value) {
        final var intValue = (int) value;
        return value < intValue ? intValue - 1 : intValue;
    }

    private FloorMath() {
    }
}
