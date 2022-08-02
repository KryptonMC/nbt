/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

/**
 * A tag holding a number value.
 */
// The reason why this is an interface, and there's an asNumber function, is so we can avoid unnecessary boxing in implementations.
// This is the same reason why the default implementations for the to{NumberType} functions were removed, as they introduce unnecessary
// boxing by having to create a boxed version of the value to have it be able to be cast to a Number.
public interface NumberTag : Tag {

    public fun asNumber(): Number

    public fun toDouble(): Double

    public fun toFloat(): Float

    public fun toLong(): Long

    public fun toInt(): Int

    public fun toShort(): Short

    public fun toByte(): Byte
}
