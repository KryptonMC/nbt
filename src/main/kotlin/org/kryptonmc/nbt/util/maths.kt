/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.util

fun Float.floor(): Int {
    val value = toInt()
    return if (this < value) value - 1 else value
}

fun Double.floor(): Int {
    val value = toInt()
    return if (this < value) value - 1 else value
}
