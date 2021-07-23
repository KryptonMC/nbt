package org.kryptonmc.nbt

import org.kryptonmc.nbt.io.TagReader
import org.kryptonmc.nbt.io.TagWriter
import java.io.DataInput
import java.io.DataOutput

class ShortTag private constructor(override val value: Short) : NumberTag<ShortTag>(value) {

    override val id = 2
    override val type = TYPE
    override val reader = READER
    override val writer = WRITER

    override fun <T> examine(examiner: TagExaminer<T>) = examiner.examineShort(this)

    override fun copy() = this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return value == (other as ShortTag).value
    }

    override fun hashCode() = value.toInt()

    companion object {

        private val CACHE = Array(1153) { ShortTag((-128 + it).toShort()) }

        val TYPE = TagType("TAG_Short", true)
        val READER = object : TagReader<ShortTag> {

            override fun read(input: DataInput) = of(input.readShort())
        }
        val WRITER = object : TagWriter<ShortTag> {

            override fun write(output: DataOutput, tag: ShortTag) = output.writeShort(tag.value.toInt())
        }

        fun of(value: Short) = if (value in -128..1024) CACHE[value.toInt()] else ShortTag(value)
    }
}
