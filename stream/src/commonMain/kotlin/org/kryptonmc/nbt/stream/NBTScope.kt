/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.stream

internal object NBTScope {

    const val BYTE_ARRAY: Int = 1
    const val INT_ARRAY: Int = 2
    const val LONG_ARRAY: Int = 3
    const val LIST: Int = 4
    const val COMPOUND: Int = 5
    val COLLECTION: IntRange = BYTE_ARRAY..LIST
}
