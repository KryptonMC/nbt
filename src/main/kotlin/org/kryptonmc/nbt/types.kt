/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

import org.kryptonmc.nbt.io.TagReader
import java.io.DataInput

private val TYPES = arrayOf(
    EndTag.TYPE,
    ByteTag.TYPE,
    ShortTag.TYPE,
    IntTag.TYPE,
    LongTag.TYPE,
    FloatTag.TYPE,
    DoubleTag.TYPE,
    ByteArrayTag.TYPE,
    StringTag.TYPE,
    ListTag.TYPE,
    CompoundTag.TYPE,
    IntArrayTag.TYPE,
    LongArrayTag.TYPE
)

private val READERS = arrayOf(
    EndTag.READER,
    ByteTag.READER,
    ShortTag.READER,
    IntTag.READER,
    LongTag.READER,
    FloatTag.READER,
    DoubleTag.READER,
    ByteArrayTag.READER,
    StringTag.READER,
    ListTag.READER,
    CompoundTag.READER,
    IntArrayTag.READER,
    LongArrayTag.READER
)

private val INVALID_TYPE = TagType("UNKNOWN")

private fun invalidReader(type: Int) = object : TagReader<EndTag> {

    override fun read(input: DataInput, depth: Int): EndTag {
        throw IllegalArgumentException("Invalid tag ID $type!")
    }
}

public fun Int.toTagType(): TagType = if (this in TYPES.indices) TYPES[this] else INVALID_TYPE

public fun Int.toTagReader(): TagReader<*> = if (this in READERS.indices) READERS[this] else invalidReader(this)
