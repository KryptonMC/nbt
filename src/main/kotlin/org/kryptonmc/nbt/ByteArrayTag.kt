package org.kryptonmc.nbt

import org.kryptonmc.nbt.io.TagReader
import org.kryptonmc.nbt.io.TagWriter
import org.kryptonmc.nbt.util.add
import org.kryptonmc.nbt.util.remove
import java.io.DataInput
import java.io.DataOutput

@Suppress("UNCHECKED_CAST")
class ByteArrayTag(data: ByteArray) : CollectionTag<ByteTag>(ByteTag.ID) {

    var data = data
        private set

    override val id = ID
    override val type = TYPE
    override val reader = READER
    override val writer = WRITER as TagWriter<Tag>
    override val size: Int
        get() = data.size

    constructor(data: Collection<Byte>) : this(data.toByteArray())

    override fun get(index: Int) = ByteTag.of(data[index])

    override fun set(index: Int, element: ByteTag): ByteTag {
        val oldValue = data[index]
        data[index] = element.value
        return ByteTag.of(oldValue)
    }

    override fun add(index: Int, element: ByteTag) {
        data = data.add(index, element.value)
    }

    override fun setTag(index: Int, tag: Tag): Boolean {
        if (tag !is NumberTag) return false
        data[index] = tag.toByte()
        return true
    }

    override fun addTag(index: Int, tag: Tag): Boolean {
        if (tag !is NumberTag) return false
        data = data.add(index, tag.toByte())
        return true
    }

    override fun removeAt(index: Int): ByteTag {
        val oldValue = data[index]
        data = data.remove(index)
        return ByteTag.of(oldValue)
    }

    override fun clear() {
        data = ByteArray(0)
    }

    override fun <T> examine(examiner: TagExaminer<T>) = examiner.examineByteArray(this)

    override fun copy(): ByteArrayTag {
        val copy = ByteArray(data.size)
        System.arraycopy(data, 0, copy, 0, data.size)
        return ByteArrayTag(copy)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return data.contentEquals((other as ByteArrayTag).data)
    }

    override fun hashCode() = data.contentHashCode()

    companion object {

        const val ID = 7
        val TYPE = TagType("TAG_Byte_Array")
        val READER = object : TagReader<ByteArrayTag> {

            override fun read(input: DataInput, depth: Int): ByteArrayTag {
                val size = input.readInt()
                val bytes = ByteArray(size)
                input.readFully(bytes)
                return ByteArrayTag(bytes)
            }
        }
        val WRITER = object : TagWriter<ByteArrayTag> {

            override fun write(output: DataOutput, tag: ByteArrayTag) {
                output.writeInt(tag.data.size)
                output.write(tag.data)
            }
        }
    }
}
