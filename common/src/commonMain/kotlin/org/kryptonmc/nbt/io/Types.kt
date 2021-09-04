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

    @JvmStatic
    public fun of(type: Int): TagType = TYPES[type]

    @JvmStatic
    public fun reader(type: Int): TagReader<*> = READERS[type]
}
