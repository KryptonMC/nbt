/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.io

import org.kryptonmc.nbt.ByteArrayTag
import org.kryptonmc.nbt.ByteTag
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.DoubleTag
import org.kryptonmc.nbt.EndTag
import org.kryptonmc.nbt.FloatTag
import org.kryptonmc.nbt.IntArrayTag
import org.kryptonmc.nbt.IntTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.LongArrayTag
import org.kryptonmc.nbt.LongTag
import org.kryptonmc.nbt.ShortTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.TagType
import kotlin.jvm.JvmStatic

/**
 * A utility used to retrieve tag types for all the tags from their IDs.
 */
public object Types {

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

    /**
     * Gets the tag type for the given tag of the given [type].
     *
     * @param type the integer type ID
     * @return the tag type for the type
     */
    @JvmStatic
    public fun of(type: Int): TagType = TYPES[type]

    /**
     * Gets the tag reader for the given tag of the given [type].
     *
     * @param type the integer type ID
     * @return the tag reader for the type
     */
    @JvmStatic
    public fun reader(type: Int): TagReader<*> = READERS[type]
}
