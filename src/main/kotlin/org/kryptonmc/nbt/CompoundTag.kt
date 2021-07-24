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
import org.kryptonmc.nbt.io.TagWriter
import org.kryptonmc.nbt.util.toTag
import org.kryptonmc.nbt.util.toUUID
import java.io.DataInput
import java.io.DataOutput
import java.util.UUID

@Suppress("UNCHECKED_CAST")
public open class CompoundTag(public open val tags: Map<String, Tag> = mapOf()) : Tag, Map<String, Tag> by tags {

    override val id: Int = ID
    override val type: TagType = TYPE

    public fun type(name: String): Int = tags[name]?.id ?: 0

    public fun contains(name: String, typeId: Int): Boolean {
        val type = type(name)
        if (type == typeId) return true
        if (typeId != 99) return false
        return type == ByteTag.ID || type == ShortTag.ID || type == IntTag.ID || type == LongTag.ID || type == FloatTag.ID || type == DoubleTag.ID
    }

    public fun hasUUID(name: String): Boolean = tags[name]?.let { it.type === IntArrayTag.TYPE && (it as IntArrayTag).data.size == 4 } ?: false

    public open fun getByte(name: String): Byte = getNumber(name)?.toByte() ?: 0

    public open fun getShort(name: String): Short = getNumber(name)?.toShort() ?: 0

    public open fun getInt(name: String): Int = getNumber(name)?.toInt() ?: 0

    public open fun getLong(name: String): Long = getNumber(name)?.toLong() ?: 0

    public open fun getFloat(name: String): Float = getNumber(name)?.toFloat() ?: 0F

    public open fun getDouble(name: String): Double = getNumber(name)?.toDouble() ?: 0.0

    public open fun getString(name: String): String = try {
        if (contains(name, StringTag.ID)) tags[name]!!.asString() else ""
    } catch (exception: ClassCastException) {
        ""
    }

    public open fun getUUID(name: String): UUID? = get(name)?.toUUID()

    public open fun getByteArray(name: String): ByteArray = if (contains(name, ByteArrayTag.ID)) (tags[name] as ByteArrayTag).data else ByteArray(0)

    public open fun getIntArray(name: String): IntArray = if (contains(name, IntArrayTag.ID)) (tags[name] as IntArrayTag).data else IntArray(0)

    public open fun getLongArray(name: String): LongArray = if (contains(name, LongArrayTag.ID)) (tags[name] as LongArrayTag).data else LongArray(0)

    public open fun getList(name: String, elementType: Int): ListTag = if (type(name) == ListTag.ID) {
        val tag = (tags[name] as ListTag)
        if (!tag.isEmpty() && tag.elementType != elementType) ListTag() else tag
    } else ListTag()

    public open fun getCompound(name: String): CompoundTag = if (contains(name, ID)) tags[name] as CompoundTag else CompoundTag()

    public open fun getBoolean(name: String): Boolean = getByte(name) != 0.toByte()

    public inline fun forEachByte(action: (String, Byte) -> Unit) {
        for ((key, value) in this) {
            if (value !is ByteTag) continue
            action(key, value.value)
        }
    }

    public inline fun forEachShort(action: (String, Short) -> Unit) {
        for ((key, value) in this) {
            if (value !is ShortTag) continue
            action(key, value.value)
        }
    }

    public inline fun forEachInt(action: (String, Int) -> Unit) {
        for ((key, value) in this) {
            if (value !is IntTag) continue
            action(key, value.value)
        }
    }

    public inline fun forEachLong(action: (String, Long) -> Unit) {
        for ((key, value) in this) {
            if (value !is LongTag) continue
            action(key, value.value)
        }
    }

    public inline fun forEach(action: (String, Float) -> Unit) {
        for ((key, value) in this) {
            if (value !is FloatTag) continue
            action(key, value.value)
        }
    }

    public inline fun forEachDouble(action: (String, Double) -> Unit) {
        for ((key, value) in this) {
            if (value !is DoubleTag) continue
            action(key, value.value)
        }
    }

    public inline fun forEachString(action: (String, String) -> Unit) {
        for ((key, value) in this) {
            if (value !is StringTag) continue
            action(key, value.value)
        }
    }

    public inline fun forEachByteArray(action: (String, ByteArray) -> Unit) {
        for ((key, value) in this) {
            if (value !is ByteArrayTag) continue
            action(key, value.data)
        }
    }

    public inline fun forEachIntArray(action: (String, IntArray) -> Unit) {
        for ((key, value) in this) {
            if (value !is IntArrayTag) continue
            action(key, value.data)
        }
    }

    public inline fun forEachLongArray(action: (String, LongArray) -> Unit) {
        for ((key, value) in this) {
            if (value !is LongArrayTag) continue
            action(key, value.data)
        }
    }

    public inline fun forEachList(action: (String, ListTag) -> Unit) {
        for ((key, value) in this) {
            if (value !is ListTag) continue
            action(key, value)
        }
    }

    public inline fun forEachCompound(action: (String, CompoundTag) -> Unit) {
        for ((key, value) in this) {
            if (value !is CompoundTag) continue
            action(key, value)
        }
    }

    public fun mutable(): MutableCompoundTag = MutableCompoundTag(tags.toMutableMap())

    override fun write(output: DataOutput): Unit = WRITER.write(output, this)

    override fun <T> examine(examiner: TagExaminer<T>): Unit = examiner.examineCompound(this)

    override fun copy(): CompoundTag {
        val copy = tags.mapValuesTo(mutableMapOf()) { it.value.copy() }
        return CompoundTag(copy)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return tags == (other as CompoundTag).tags
    }

    override fun hashCode(): Int = tags.hashCode()

    override fun toString(): String = asString()

    private fun getNumber(name: String): NumberTag? = try {
        if (contains(name, 99)) tags[name] as NumberTag else null
    } catch (exception: ClassCastException) {
        null
    }

    public class Builder internal constructor() {

        private val tags = mutableMapOf<String, Tag>()

        public fun put(name: String, value: Tag): Builder = apply { tags[name] = value }

        @NBTDsl
        @JvmName("putByte")
        public fun byte(name: String, value: Byte): Builder = apply { put(name, ByteTag.of(value)) }

        @NBTDsl
        @JvmName("putBoolean")
        public fun boolean(name: String, value: Boolean): Builder = apply { put(name, ByteTag.of(value)) }

        @NBTDsl
        @JvmName("putShort")
        public fun short(name: String, value: Short): Builder = apply { put(name, ShortTag.of(value)) }

        @NBTDsl
        @JvmName("putInt")
        public fun int(name: String, value: Int): Builder = apply { put(name, IntTag.of(value)) }

        @NBTDsl
        @JvmName("putLong")
        public fun long(name: String, value: Long): Builder = apply { put(name, LongTag.of(value)) }

        @NBTDsl
        @JvmName("putFloat")
        public fun float(name: String, value: Float): Builder = apply { put(name, FloatTag.of(value)) }

        @NBTDsl
        @JvmName("putDouble")
        public fun double(name: String, value: Double): Builder = apply { put(name, DoubleTag.of(value)) }

        @NBTDsl
        @JvmName("putString")
        public fun string(name: String, value: String): Builder = apply { put(name, StringTag.of(value)) }

        @NBTDsl
        @JvmName("putUUID")
        public fun uuid(name: String, value: UUID): Builder = apply { put(name, value.toTag()) }

        @NBTDsl
        @JvmName("putByteArray")
        public fun byteArray(name: String, value: ByteArray): Builder = apply { put(name, ByteArrayTag(value)) }

        @NBTDsl
        @JvmName("putBytes")
        public fun bytes(name: String, vararg values: Byte): Builder = apply { put(name, ByteArrayTag(values)) }

        @NBTDsl
        @JvmName("putIntArray")
        public fun intArray(name: String, value: IntArray): Builder = apply { put(name, IntArrayTag(value)) }

        @NBTDsl
        @JvmName("putInts")
        public fun ints(name: String, vararg values: Int): Builder = apply { put(name, IntArrayTag(values)) }

        @NBTDsl
        @JvmName("putLongArray")
        public fun longArray(name: String, value: LongArray): Builder = apply { put(name, LongArrayTag(value)) }

        @NBTDsl
        @JvmName("putLongs")
        public fun longs(name: String, vararg values: Long): Builder = apply { put(name, LongArrayTag(values)) }

        @NBTDsl
        @JvmName("putList")
        public inline fun list(name: String, builder: ListTag.() -> Unit): Builder = apply { put(name, ListTag().apply(builder)) }

        @NBTDsl
        @JvmName("putList")
        public fun list(name: String, elementType: Int, vararg elements: Tag): Builder = apply { put(name, ListTag(elements.toMutableList(), elementType)) }

        @NBTDsl
        @JvmName("putList")
        public fun list(name: String, elementType: Int, elements: Collection<Tag>): Builder = apply {
            if (elements is MutableList) put(name, ListTag(elements, elementType)) else put(name, ListTag(elements.toMutableList(), elementType))
        }

        @NBTDsl
        @JvmName("putCompound")
        public inline fun compound(name: String, builder: Builder.() -> Unit): Builder = apply { put(name, CompoundTag.builder().apply(builder).build()) }

        public fun build(): CompoundTag = CompoundTag(tags.toMap())
    }

    public companion object {

        public const val ID: Int = 10
        public val TYPE: TagType = TagType("TAG_Compound")
        public val READER: TagReader<CompoundTag> = object : TagReader<CompoundTag> {

            override fun read(input: DataInput, depth: Int): CompoundTag {
                if (depth > 512) throw RuntimeException("Depth too high! Given depth $depth is higher than maximum depth 512!")
                val tags = mutableMapOf<String, Tag>()
                var type = input.readByte().toInt()
                while (type != 0) {
                    val name = input.readUTF()
                    val tag = type.toTagReader().read(input, depth + 1)
                    tags[name] = tag
                    type = input.readByte().toInt()
                }
                return CompoundTag(tags)
            }
        }
        public val WRITER: TagWriter<CompoundTag> = object : TagWriter<CompoundTag> {

            override fun write(output: DataOutput, tag: CompoundTag) {
                tag.tags.forEach { output.writeNamedTag(it.key, it.value) }
                output.writeByte(0)
            }
        }

        public fun builder(): Builder = Builder()
    }
}

private fun DataOutput.writeNamedTag(name: String, tag: Tag) {
    writeByte(tag.id)
    if (tag.id != 0) {
        writeUTF(name)
        tag.write(this)
    }
}
