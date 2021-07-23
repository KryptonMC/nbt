package org.kryptonmc.nbt

import org.kryptonmc.nbt.io.TagReader
import java.io.DataInput

private val TYPES = arrayOf(
    EndTag.type,
    ByteTag.TYPE,
    ShortTag.TYPE,
    IntTag.TYPE,
    LongTag.TYPE,
    FloatTag.TYPE,
    DoubleTag.TYPE,
    ByteArrayTag.TYPE,
    StringTag.TYPE,
    ListTag.TYPE,
    TagType("TAG_Compound"), // TODO: Replace with CompoundTag.TYPE
    IntArrayTag.TYPE,
    LongArrayTag.TYPE
)

private val READERS = arrayOf(
    EndTag.reader,
    ByteTag.READER,
    ShortTag.READER,
    IntTag.READER,
    LongTag.READER,
    FloatTag.READER,
    DoubleTag.READER,
    ByteArrayTag.READER,
    StringTag.READER,
    ListTag.READER,
    EndTag.reader, // TODO: Replace with CompoundTag.READER
    IntArrayTag.READER,
    LongArrayTag.READER
)

private val INVALID_TYPE = TagType("UNKNOWN")

private fun invalidReader(type: Int) = object : TagReader<EndTag> {

    override fun read(input: DataInput, depth: Int): EndTag {
        throw IllegalArgumentException("Invalid tag ID $type!")
    }
}

@JvmSynthetic
internal fun Int.toTagType(): TagType = if (this in TYPES.indices) TYPES[this] else INVALID_TYPE

@JvmSynthetic
internal fun Int.toTagReader(): TagReader<*> = if (this in READERS.indices) READERS[this] else invalidReader(this)
