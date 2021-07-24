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
public class CompoundTag(private val tags: MutableMap<String, Tag> = mutableMapOf()) : Tag, MutableMap<String, Tag> by tags {

    override val id: Int = ID
    override val type: TagType = TYPE
    override val reader: TagReader<CompoundTag> = READER
    override val writer: TagWriter<Tag> = WRITER as TagWriter<Tag>

    public fun type(name: String): Int = tags[name]?.id ?: 0

    public fun contains(name: String, typeId: Int): Boolean {
        val type = type(name)
        if (type == typeId) return true
        if (typeId != 99) return false
        return type == ByteTag.ID || type == ShortTag.ID || type == IntTag.ID || type == LongTag.ID || type == FloatTag.ID || type == DoubleTag.ID
    }

    public fun hasUUID(name: String): Boolean = tags[name]?.let { it.type === IntArrayTag.TYPE && (it as IntArrayTag).data.size == 4 } ?: false

    public fun getByte(name: String): Byte = getNumber(name)?.toByte() ?: 0

    public fun putByte(name: String, value: Byte): CompoundTag = apply { put(name, ByteTag.of(value)) }

    public fun getShort(name: String): Short = getNumber(name)?.toShort() ?: 0

    public fun putShort(name: String, value: Short): CompoundTag = apply { put(name, ShortTag.of(value)) }

    public fun getInt(name: String): Int = getNumber(name)?.toInt() ?: 0

    public fun putInt(name: String, value: Int): CompoundTag = apply { put(name, IntTag.of(value)) }

    public fun getLong(name: String): Long = getNumber(name)?.toLong() ?: 0

    public fun putLong(name: String, value: Long): CompoundTag = apply { put(name, LongTag.of(value)) }

    public fun getFloat(name: String): Float = getNumber(name)?.toFloat() ?: 0F

    public fun putFloat(name: String, value: Float): CompoundTag = apply { put(name, FloatTag.of(value)) }

    public fun getDouble(name: String): Double = getNumber(name)?.toDouble() ?: 0.0

    public fun putDouble(name: String, value: Double): CompoundTag = apply { put(name, DoubleTag.of(value)) }

    public fun getString(name: String): String = try {
        if (contains(name, StringTag.ID)) tags[name]!!.asString() else ""
    } catch (exception: ClassCastException) {
        ""
    }

    public fun putString(name: String, value: String): CompoundTag = apply { put(name, StringTag.of(value)) }

    public fun getUUID(name: String): UUID? = get(name)?.toUUID()

    public fun putUUID(name: String, value: UUID): CompoundTag = apply { put(name, value.toTag()) }

    public fun getByteArray(name: String): ByteArray = if (contains(name, ByteArrayTag.ID)) (tags[name] as ByteArrayTag).data else ByteArray(0)

    public fun putByteArray(name: String, value: ByteArray): CompoundTag = apply { put(name, ByteArrayTag(value)) }

    public fun getIntArray(name: String): IntArray = if (contains(name, IntArrayTag.ID)) (tags[name] as IntArrayTag).data else IntArray(0)

    public fun putIntArray(name: String, value: IntArray): CompoundTag = apply { put(name, IntArrayTag(value)) }

    public fun getLongArray(name: String): LongArray = if (contains(name, LongArrayTag.ID)) (tags[name] as LongArrayTag).data else LongArray(0)

    public fun putLongArray(name: String, value: LongArray): CompoundTag = apply { put(name, LongArrayTag(value)) }

    public fun getCompound(name: String): CompoundTag = if (contains(name, ID)) tags[name] as CompoundTag else CompoundTag()

    public fun getList(name: String, elementType: Int): ListTag = if (type(name) == ListTag.ID) {
        val tag = (tags[name] as ListTag)
        if (!tag.isEmpty() && tag.elementType != elementType) ListTag() else tag
    } else ListTag()

    public fun getBoolean(name: String): Boolean = getByte(name) != 0.toByte()

    public fun putBoolean(name: String, value: Boolean): CompoundTag = apply { put(name, ByteTag.of(value)) }

    public fun merge(other: CompoundTag): CompoundTag = apply {
        other.forEach { (key, tag) ->
            if (tag.id != ID) {
                put(key, tag.copy())
                return@forEach
            }
            if (contains(key, ID)) getCompound(key).merge(tag as CompoundTag) else put(key, tag.copy())
        }
    }

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
    }
}

private fun DataOutput.writeNamedTag(name: String, tag: Tag) {
    writeByte(tag.id)
    if (tag.id != 0) {
        writeUTF(name)
        tag.writer.write(this, tag)
    }
}
