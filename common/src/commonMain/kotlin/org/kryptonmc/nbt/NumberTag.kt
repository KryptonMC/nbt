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
public abstract class NumberTag protected constructor(public open val value: Number) : Tag {

    public open fun toDouble(): Double = value.toDouble()

    public open fun toFloat(): Float = value.toFloat()

    public open fun toLong(): Long = value.toLong()

    public open fun toInt(): Int = value.toInt()

    public open fun toShort(): Short = value.toShort()

    public open fun toByte(): Byte = value.toByte()
}
