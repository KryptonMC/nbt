/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.util

// Very simple, very fast floor functions that deal with negative numbers
@JvmSynthetic
internal fun Double.floor(): Int {
    val value = toInt()
    return if (this < value) value - 1 else value
}

@JvmSynthetic
internal fun Float.floor(): Int {
    val value = toInt()
    return if (this < value) value - 1 else value
}
