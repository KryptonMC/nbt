package org.kryptonmc.nbt

import org.kryptonmc.nbt.io.TagReader
import org.kryptonmc.nbt.io.TagWriter
import java.io.DataInput
import java.io.DataOutput

class ListTag(private val data: MutableList<Tag>, elementType: Int) : CollectionTag<Tag>(elementType) {

    override var elementType = elementType
        @JvmSynthetic internal set

    override val id = ID
    override val type = TYPE
    override val reader = READER
    override val writer = WRITER
    override val size: Int
        get() = data.size

    override fun get(index: Int) = data[index]

    override fun set(index: Int, element: Tag): Tag {
        val oldValue = data[index]
        if (!setTag(index, element)) throw UnsupportedOperationException("Trying to add tag of type ${element.id} to list of type $elementType")
        return oldValue
    }

    override fun add(index: Int, element: Tag) {
        if (!addTag(index, element)) throw UnsupportedOperationException("Trying to add tag of type ${element.id} to list of type $elementType")
    }

    override fun setTag(index: Int, tag: Tag) = if (updateType(tag)) {
        data[index] = tag
        true
    } else false

    override fun addTag(index: Int, tag: Tag) = if (updateType(tag)) {
        data.add(index, tag)
        true
    } else false

    override fun removeAt(index: Int): Tag {
        val oldValue = data.removeAt(index)
        if (data.isEmpty()) elementType = 0
        return oldValue
    }

    override fun <T> examine(examiner: TagExaminer<T>) = examiner.examineList(this)

    override fun copy(): Tag {
        val iterable = if (elementType.toTagType().isValue) data else data.map { it.copy() }
        val list = ArrayList(iterable)
        return ListTag(list, elementType)
    }

    override fun clear() {
        data.clear()
        elementType = 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return data == (other as ListTag).data
    }

    override fun hashCode() = data.hashCode()

    override fun toString() = asString()

    private fun updateType(tag: Tag): Boolean {
        if (tag.id == 0) return false
        if (elementType == 0) {
            elementType = tag.id
            return true
        }
        return elementType == tag.id
    }

    companion object {

        const val ID = 9
        val TYPE = TagType("TAG_List")
        val READER = object : TagReader<ListTag> {

            override fun read(input: DataInput, depth: Int): ListTag {
                if (depth > 512) throw RuntimeException("Depth too high! Given depth $depth is higher than maximum depth 512!")
                val type = input.readByte().toInt()
                val size = input.readInt()
                if (type == 0 && size > 0) throw RuntimeException("Missing required type byte for ListTag!")
                val reader = type.toTagReader()
                val data = ArrayList<Tag>(size)
                for (i in 0 until size) data.add(reader.read(input, depth + 1))
                return ListTag(data, type)
            }
        }
        val WRITER = object : TagWriter<ListTag> {

            @Suppress("UNCHECKED_CAST")
            override fun write(output: DataOutput, tag: ListTag) {
                tag.elementType = if (tag.data.isEmpty()) 0 else tag.data[0].id
                output.writeByte(tag.elementType)
                output.writeInt(tag.data.size)
                tag.data.forEach { (it.writer as TagWriter<Tag>).write(output, it) }
            }
        }
    }
}
