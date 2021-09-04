/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.stream

import okio.Closeable
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

public abstract class NBTReader : Closeable {

    public abstract fun peekType(): Byte

    public abstract fun beginByteArray(): Int

    public abstract fun endByteArray()

    public abstract fun beginIntArray(): Int

    public abstract fun endIntArray()

    public abstract fun beginLongArray(): Int

    public abstract fun endLongArray()

    public abstract fun beginList(elementType: Int): Int

    public abstract fun endList()

    public abstract fun beginCompound()

    public abstract fun endCompound()

    public abstract fun hasNext(): Boolean

    public abstract fun nextType(): Byte

    public abstract fun nextName(): String

    public abstract fun nextByte(): Byte

    public abstract fun nextShort(): Short

    public abstract fun nextInt(): Int

    public abstract fun nextLong(): Long

    public abstract fun nextFloat(): Float

    public abstract fun nextDouble(): Double

    public abstract fun nextString(): String

    public abstract fun nextEnd()

    public fun read(): Tag = when (peekType().toInt()) {
        ByteArrayTag.ID -> {
            val size = beginByteArray()
            val bytes = ByteArray(size)
            var i = 0
            while (hasNext()) {
                bytes[i] = nextByte()
                ++i
            }
            endByteArray()
            ByteArrayTag(bytes)
        }
        IntArrayTag.ID -> {
            val size = beginIntArray()
            val ints = IntArray(size)
            var i = 0
            while (hasNext()) {
                ints[i] = nextInt()
                ++i
            }
            endIntArray()
            IntArrayTag(ints)
        }
        LongArrayTag.ID -> {
            val size = beginLongArray()
            val longs = LongArray(size)
            var i = 0
            while (hasNext()) {
                longs[i] = nextLong()
                ++i
            }
            endLongArray()
            LongArrayTag(longs)
        }
        ListTag.ID -> {
            val type = nextType().toInt()
            val size = beginList(type)
            if (type == 0 && size > 0) {
                throw RuntimeException("Missing required type for non-empty list tag!")
            }
            val list = ListTag.builder(type)
            while (hasNext()) {
                list.add(read())
            }
            endList()
            list.build()
        }
        CompoundTag.ID -> {
            var type = nextType().toInt()
            val compound = CompoundTag.builder()
            while (type != 0) {
                val name = nextName()
                val value = read()
                compound.put(name, value)
                type = nextType().toInt()
            }
            compound.build()
        }
        ByteTag.ID -> ByteTag.of(nextByte())
        ShortTag.ID -> ShortTag.of(nextShort())
        IntTag.ID -> IntTag.of(nextInt())
        LongTag.ID -> LongTag.of(nextLong())
        FloatTag.ID -> FloatTag.of(nextFloat())
        DoubleTag.ID -> DoubleTag.of(nextDouble())
        StringTag.ID -> StringTag.of(nextString())
        else -> error("Expected a value, but was ${peekType()}!")
    }
}
