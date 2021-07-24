/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

public interface TagExaminer<T> {

    public fun examine(tag: Tag): T

    public fun examineEnd(tag: EndTag)

    public fun examineByte(tag: ByteTag)

    public fun examineShort(tag: ShortTag)

    public fun examineInt(tag: IntTag)

    public fun examineLong(tag: LongTag)

    public fun examineFloat(tag: FloatTag)

    public fun examineDouble(tag: DoubleTag)

    public fun examineByteArray(tag: ByteArrayTag)

    public fun examineString(tag: StringTag)

    public fun examineList(tag: ListTag)

    public fun examineCompound(tag: CompoundTag)

    public fun examineIntArray(tag: IntArrayTag)

    public fun examineLongArray(tag: LongArrayTag)
}
