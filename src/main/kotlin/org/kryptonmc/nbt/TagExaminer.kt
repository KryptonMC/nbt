/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

interface TagExaminer<T> {

    fun examine(tag: Tag): T

    fun examineEnd(tag: EndTag)

    fun examineByte(tag: ByteTag)

    fun examineShort(tag: ShortTag)

    fun examineInt(tag: IntTag)

    fun examineLong(tag: LongTag)

    fun examineFloat(tag: FloatTag)

    fun examineDouble(tag: DoubleTag)

    fun examineByteArray(tag: ByteArrayTag)

    fun examineString(tag: StringTag)

    fun examineList(tag: ListTag)

    fun examineCompound(tag: CompoundTag)

    fun examineIntArray(tag: IntArrayTag)

    fun examineLongArray(tag: LongArrayTag)
}
