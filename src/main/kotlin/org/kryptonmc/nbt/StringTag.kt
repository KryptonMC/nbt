package org.kryptonmc.nbt

import org.kryptonmc.nbt.io.TagReader
import org.kryptonmc.nbt.io.TagWriter
import java.io.DataInput
import java.io.DataOutput

class StringTag private constructor(val value: String) : Tag {

    override val id = ID
    override val type = TYPE
    override val reader = READER
    override val writer = WRITER

    override fun <T> examine(examiner: TagExaminer<T>) = examiner.examineString(this)

    override fun copy() = this

    override fun asString() = value

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return value == (other as StringTag).value
    }

    override fun hashCode() = value.hashCode()

    companion object {

        private val EMPTY = StringTag("")

        const val ID = 8
        val TYPE = TagType("TAG_String", true)
        val READER = object : TagReader<StringTag> {

            override fun read(input: DataInput, depth: Int) = of(input.readUTF())
        }
        val WRITER = object : TagWriter<StringTag> {

            override fun write(output: DataOutput, tag: StringTag) = output.writeUTF(tag.value)
        }

        fun of(value: String) = if (value.isEmpty()) EMPTY else StringTag(value)
    }
}
