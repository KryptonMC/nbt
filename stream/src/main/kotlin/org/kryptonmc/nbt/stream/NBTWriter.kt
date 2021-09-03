/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.stream

import org.kryptonmc.nbt.ByteArrayTag
import org.kryptonmc.nbt.ByteTag
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.DoubleTag
import org.kryptonmc.nbt.FloatTag
import org.kryptonmc.nbt.IntArrayTag
import org.kryptonmc.nbt.IntTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.LongArrayTag
import org.kryptonmc.nbt.LongTag
import org.kryptonmc.nbt.ShortTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.Tag
import java.io.Closeable
import java.util.UUID

public abstract class NBTWriter : Closeable {

    public abstract fun beginByteArray(size: Int)

    public abstract fun endByteArray()

    public abstract fun beginIntArray(size: Int)

    public abstract fun endIntArray()

    public abstract fun beginLongArray(size: Int)

    public abstract fun endLongArray()

    public abstract fun beginList(elementType: Int, size: Int)

    public abstract fun endList()

    public abstract fun beginCompound()

    public abstract fun endCompound()

    public abstract fun name(name: String)

    public abstract fun value(value: Boolean)

    public abstract fun value(value: Byte)

    public abstract fun value(value: Short)

    public abstract fun value(value: Int)

    public abstract fun value(value: Long)

    public abstract fun value(value: Float)

    public abstract fun value(value: Double)

    public abstract fun value(value: String)

    public abstract fun value(value: UUID)

    public abstract fun end()

    public fun write(tag: Tag): Unit = when (tag) {
        is ByteTag -> value(tag.value)
        is ShortTag -> value(tag.value)
        is IntTag -> value(tag.value)
        is LongTag -> value(tag.value)
        is FloatTag -> value(tag.value)
        is DoubleTag -> value(tag.value)
        is StringTag -> value(tag.value)
        is ListTag -> {
            beginList(tag.elementType, tag.size)
            tag.forEach { write(it) }
            endList()
        }
        is CompoundTag -> {
            beginCompound()
            tag.forEach {
                name(it.key)
                write(it.value)
            }
            endCompound()
        }
        is ByteArrayTag -> {
            beginByteArray(tag.size)
            tag.forEach { value(it.value) }
            endByteArray()
        }
        is IntArrayTag -> {
            beginIntArray(tag.size)
            tag.forEach { value(it.value) }
            endIntArray()
        }
        is LongArrayTag -> {
            beginLongArray(tag.size)
            tag.forEach { value(it.value) }
            endLongArray()
        }
        else -> error("Don't know how to write $tag!")
    }
}
