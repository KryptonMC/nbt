package org.kryptonmc.nbt

import org.kryptonmc.nbt.util.UUID
import org.kryptonmc.nbt.util.toTag

public sealed class ScopedCompoundTag<T : CompoundTag> : CompoundTag() {

    abstract override fun put(key: String, value: Tag): T

    final override fun putBoolean(key: String, value: Boolean): T = put(key, ByteTag.of(value))

    final override fun putByte(key: String, value: Byte): T = put(key, ByteTag.of(value))

    final override fun putShort(key: String, value: Short): T = put(key, ShortTag.of(value))

    final override fun putInt(key: String, value: Int): T = put(key, IntTag.of(value))

    final override fun putLong(key: String, value: Long): T = put(key, LongTag.of(value))

    final override fun putFloat(key: String, value: Float): T = put(key, FloatTag.of(value))

    final override fun putDouble(key: String, value: Double): T = put(key, DoubleTag.of(value))

    final override fun putString(key: String, value: String): T = put(key, StringTag.of(value))

    final override fun putUUID(key: String, value: UUID): T = put(key, value.toTag())

    final override fun putByteArray(key: String, value: ByteArray): T = put(key, ByteArrayTag(value))

    final override fun putIntArray(key: String, value: IntArray): T = put(key, IntArrayTag(value))

    final override fun putLongArray(key: String, value: LongArray): T = put(key, LongArrayTag(value))

    final override fun putBytes(key: String, vararg values: Byte): T = putByteArray(key, values)

    final override fun putInts(key: String, vararg values: Int): T = putIntArray(key, values)

    final override fun putLongs(key: String, vararg values: Long): T = putLongArray(key, values)

    final override fun update(key: String, builder: CompoundTag.() -> Unit): T = put(key, getCompound(key).apply(builder))

    final override fun update(key: String, type: Int, builder: ListTag.() -> Unit): T = put(key, getList(key, type).apply(builder))
}
