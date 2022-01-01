package org.kryptonmc.nbt

public sealed class ScopedListTag<T : ListTag> : ListTag() {

    abstract override fun add(tag: Tag): T

    abstract override fun removeAt(index: Int): T

    abstract override fun remove(tag: Tag): T

    abstract override fun set(index: Int, tag: Tag): T

    final override fun setBoolean(index: Int, value: Boolean): T = set(index, ByteTag.of(value))

    final override fun setByte(index: Int, value: Byte): T = set(index, ByteTag.of(value))

    final override fun setShort(index: Int, value: Short): T = set(index, ShortTag.of(value))

    final override fun setInt(index: Int, value: Int): T = set(index, IntTag.of(value))

    final override fun setLong(index: Int, value: Long): T = set(index, LongTag.of(value))

    final override fun setFloat(index: Int, value: Float): T = set(index, FloatTag.of(value))

    final override fun setDouble(index: Int, value: Double): T = set(index, DoubleTag.of(value))

    final override fun setString(index: Int, value: String): T = set(index, StringTag.of(value))

    final override fun setByteArray(index: Int, value: ByteArray): T = set(index, ByteArrayTag(value))

    final override fun setIntArray(index: Int, value: IntArray): T = set(index, IntArrayTag(value))

    final override fun setLongArray(index: Int, value: LongArray): T = set(index, LongArrayTag(value))

    final override fun setBytes(index: Int, vararg values: Byte): T = setByteArray(index, values)

    final override fun setInts(index: Int, vararg values: Int): T = setIntArray(index, values)

    final override fun setLongs(index: Int, vararg values: Long): T = setLongArray(index, values)
}
