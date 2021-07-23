/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

abstract class CollectionTag<E : Tag>(open val elementType: Int) : AbstractMutableList<E>(), Tag {

    abstract fun setTag(index: Int, tag: Tag): Boolean

    abstract fun addTag(index: Int, tag: Tag): Boolean
}
