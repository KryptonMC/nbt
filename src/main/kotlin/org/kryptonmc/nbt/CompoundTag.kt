package org.kryptonmc.nbt

import org.kryptonmc.nbt.io.TagReader
import org.kryptonmc.nbt.io.TagWriter
import org.kryptonmc.nbt.util.toTag
import org.kryptonmc.nbt.util.toUUID
import java.io.DataInput
import java.io.DataOutput
import java.util.UUID

@Suppress("UNCHECKED_CAST")
class CompoundTag(private val tags: MutableMap<String, Tag> = mutableMapOf()) : Tag, MutableMap<String, Tag> by tags {

    override val id = ID
    override val type = TYPE
    override val reader = READER
    override val writer = WRITER as TagWriter<Tag>

    fun type(name: String): Int = tags[name]?.id ?: 0

    fun contains(name: String, typeId: Int): Boolean {
        val type = type(name)
        if (type == typeId) return true
        if (typeId != 99) return false
        return type == ByteTag.ID || type == ShortTag.ID || type == IntTag.ID || type == LongTag.ID || type == FloatTag.ID || type == DoubleTag.ID
    }

    fun hasUUID(name: String) = tags[name]?.let { it.type === IntArrayTag.TYPE && (it as IntArrayTag).data.size == 4 } ?: false

    fun getByte(name: String) = getNumber(name)?.toByte() ?: 0

    fun putByte(name: String, value: Byte) = put(name, ByteTag.of(value))

    fun getShort(name: String) = getNumber(name)?.toShort() ?: 0

    fun putShort(name: String, value: Short) = put(name, ShortTag.of(value))

    fun getInt(name: String) = getNumber(name)?.toInt() ?: 0

    fun putInt(name: String, value: Int) = put(name, IntTag.of(value))

    fun getLong(name: String) = getNumber(name)?.toLong() ?: 0

    fun putLong(name: String, value: Long) = put(name, LongTag.of(value))

    fun getFloat(name: String) = getNumber(name)?.toFloat() ?: 0

    fun putFloat(name: String, value: Float) = put(name, FloatTag.of(value))

    fun getDouble(name: String) = getNumber(name)?.toDouble() ?: 0

    fun putDouble(name: String, value: Double) = put(name, DoubleTag.of(value))

    fun getString(name: String) = try {
        if (contains(name, StringTag.ID)) tags[name]!!.asString() else ""
    } catch (exception: ClassCastException) {
        ""
    }

    fun putString(name: String, value: String) = put(name, StringTag.of(value))

    fun getUUID(name: String) = get(name)!!.toUUID()

    fun putUUID(name: String, value: UUID) = put(name, value.toTag())

    fun getByteArray(name: String) = if (contains(name, ByteArrayTag.ID)) (tags[name] as ByteArrayTag).data else ByteArray(0)

    fun putByteArray(name: String, value: ByteArray) = put(name, ByteArrayTag(value))

    fun getIntArray(name: String) = if (contains(name, IntArrayTag.ID)) (tags[name] as IntArrayTag).data else IntArray(0)

    fun putIntArray(name: String, value: IntArray) = put(name, IntArrayTag(value))

    fun getLongArray(name: String) = if (contains(name, LongArrayTag.ID)) (tags[name] as LongArrayTag).data else LongArray(0)

    fun putLongArray(name: String, value: LongArray) = put(name, LongArrayTag(value))

    fun getCompound(name: String) = if (contains(name, ID)) tags[name] as CompoundTag else CompoundTag()

    fun getList(name: String, elementType: Int) = if (type(name) == ListTag.ID) {
        val tag = (tags[name] as ListTag)
        if (!tag.isEmpty() && tag.elementType != elementType) ListTag() else tag
    } else ListTag()

    fun getBoolean(name: String) = getByte(name) != 0.toByte()

    fun putBoolean(name: String, value: Boolean) = put(name, ByteTag.of(value))

    fun merge(other: CompoundTag): CompoundTag = apply {
        tags.forEach { (key, tag) ->
            if (tag.id != ID) {
                put(key, tag.copy())
                return@forEach
            }
            if (contains(key, ID)) getCompound(key).merge(tag as CompoundTag) else put(key, tag.copy())
        }
    }

    override fun <T> examine(examiner: TagExaminer<T>) = examiner.examineCompound(this)

    override fun copy(): Tag {
        val copy = tags.mapValuesTo(mutableMapOf()) { it.value.copy() }
        return CompoundTag(copy)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return tags == (other as CompoundTag).tags
    }

    override fun hashCode() = tags.hashCode()

    override fun toString() = asString()

    private fun getNumber(name: String): NumberTag? = try {
        if (contains(name, 99)) tags[name] as NumberTag else null
    } catch (exception: ClassCastException) {
        null
    }

    companion object {

        const val ID = 10
        val TYPE = TagType("TAG_Compound")
        val READER = object : TagReader<CompoundTag> {

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
        val WRITER = object : TagWriter<CompoundTag> {

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
