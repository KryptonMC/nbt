/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

import java.util.AbstractList

public abstract class AbstractCollectionTag<T : Tag> : AbstractList<T>(), CollectionTag<T> {

    final override fun addTag(index: Int, tag: T) {
        add(index, tag)
    }
}