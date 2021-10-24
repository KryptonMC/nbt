/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

public class ImmutableListTag(data: List<Tag> = emptyList(), elementType: Int = 0) : ListTag(data, elementType) {

    override val size: Int
        get() = data.size

    override fun get(index: Int): Tag = data[index]

    override fun copy(): ListTag = this // Immutable, no need to copy
}
