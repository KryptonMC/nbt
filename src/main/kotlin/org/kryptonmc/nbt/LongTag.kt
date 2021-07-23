package org.kryptonmc.nbt

import org.kryptonmc.nbt.io.TagReader
import org.kryptonmc.nbt.io.TagWriter
import java.io.DataInput
import java.io.DataOutput

class LongTag private constructor(override val value: Long) : NumberTag<LongTag>(value) {

    override val id = 4
    override val type = TYPE
    override val reader = READER
    override val writer = WRITER

    override fun <T> examine(examiner: TagExaminer<T>) = examiner.examineLong(this)

    override fun copy() = this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return value == (other as LongTag).value
    }

    override fun hashCode() = (value xor (value ushr 32)).toInt()

    companion object {

        private val CACHE = Array(1153) { LongTag((-128 + it).toLong()) }

        val TYPE = TagType("TAG_Long", true)
        val READER = object : TagReader<LongTag> {

            override fun read(input: DataInput) = of(input.readLong())
        }
        val WRITER = object : TagWriter<LongTag> {

            override fun write(output: DataOutput, tag: LongTag) = output.writeLong(tag.value)
        }

        fun of(value: Long) = if (value in -128..1024) CACHE[value.toInt()] else LongTag(value)
    }
}
