package org.kryptonmc.nbt

import org.kryptonmc.nbt.io.TagReader
import org.kryptonmc.nbt.io.TagWriter
import org.kryptonmc.nbt.util.add
import org.kryptonmc.nbt.util.remove
import java.io.DataInput
import java.io.DataOutput

class IntArrayTag(data: IntArray) : CollectionTag<IntTag>(IntTag.ID) {

    var data = data
        private set

    override val id = ID
    override val type = TYPE
    override val reader = READER
    override val writer = WRITER
    override val size: Int
        get() = data.size

    constructor(data: Collection<Int>) : this(data.toIntArray())

    override fun get(index: Int) = IntTag.of(data[index])

    override fun set(index: Int, element: IntTag): IntTag {
        val oldValue = data[index]
        data[index] = element.value
        return IntTag.of(oldValue)
    }

    override fun add(index: Int, element: IntTag) {
        data = data.add(index, element.value)
    }

    override fun setTag(index: Int, tag: Tag): Boolean {
        if (tag !is NumberTag) return false
        data[index] = tag.toInt()
        return true
    }

    override fun addTag(index: Int, tag: Tag): Boolean {
        if (tag !is NumberTag) return false
        data = data.add(index, tag.toInt())
        return true
    }

    override fun removeAt(index: Int): IntTag {
        val oldValue = data[index]
        data = data.remove(index)
        return IntTag.of(oldValue)
    }

    override fun clear() {
        data = IntArray(0)
    }

    override fun <T> examine(examiner: TagExaminer<T>) = examiner.examineIntArray(this)

    override fun copy(): IntArrayTag {
        val copy = IntArray(data.size)
        System.arraycopy(data, 0, copy, 0, data.size)
        return IntArrayTag(copy)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return data.contentEquals((other as IntArrayTag).data)
    }

    override fun hashCode() = data.contentHashCode()

    companion object {

        const val ID = 11
        val TYPE = TagType("TAG_Int_Array")
        val READER = object : TagReader<IntArrayTag> {

            override fun read(input: DataInput, depth: Int): IntArrayTag {
                val size = input.readInt()
                val ints = IntArray(size)
                for (i in 0 until size) ints[i] = input.readInt()
                return IntArrayTag(ints)
            }
        }
        val WRITER = object : TagWriter<IntArrayTag> {

            override fun write(output: DataOutput, tag: IntArrayTag) {
                output.writeInt(tag.data.size)
                tag.data.forEach { output.writeInt(it) }
            }
        }
    }
}
