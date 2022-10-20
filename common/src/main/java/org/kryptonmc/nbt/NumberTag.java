/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt;

import org.jetbrains.annotations.NotNull;

/**
 * A tag holding a number value.
 */
// The reason why this is an interface, and there's an asNumber function, is so we can avoid unnecessary boxing in implementations.
// This is the same reason why the default implementations for the to{NumberType} functions were removed, as they introduce unnecessary
// boxing by having to create a boxed version of the value to have it be able to be cast to a Number.
public sealed interface NumberTag extends Tag permits ByteTag, ShortTag, IntTag, LongTag, FloatTag, DoubleTag {

    /**
     * Gets the value of this number tag as a {@link Number}.
     *
     * @return the number value
     */
    @NotNull Number asNumber();

    /**
     * Gets the value of this number tag as a {@code double}.
     *
     * @return the double value
     */
    double toDouble();

    /**
     * Gets the value of this number tag as a {@code float}.
     *
     * @return the float value
     */
    float toFloat();

    /**
     * Gets the value of this number tag as a {@code long}.
     *
     * @return the long value
     */
    long toLong();

    /**
     * Gets the value of this number tag as an {@code int}.
     *
     * @return the int value
     */
    int toInt();

    /**
     * Gets the value of this number tag as a {@code short}.
     *
     * @return the short value
     */
    short toShort();

    /**
     * Gets the value of this number tag as a {@code byte}.
     *
     * @return the byte value
     */
    byte toByte();
}
