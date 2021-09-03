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

    const val BYTE_ARRAY = 1
    const val INT_ARRAY = 2
    const val LONG_ARRAY = 3
    const val LIST = 4
    const val COMPOUND = 5
    val COLLECTION = BYTE_ARRAY..LIST
}
