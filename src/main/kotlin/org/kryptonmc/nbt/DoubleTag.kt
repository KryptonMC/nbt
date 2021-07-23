package org.kryptonmc.nbt

import org.kryptonmc.nbt.io.TagReader
import org.kryptonmc.nbt.io.TagWriter
import org.kryptonmc.nbt.util.floor
import java.io.DataInput
import java.io.DataOutput

class DoubleTag(override val value: Double) : NumberTag<DoubleTag>(value) {

    override val id = 6
    override val type = TYPE
    override val reader = READER
    override val writer = WRITER

    override fun <T> examine(examiner: TagExaminer<T>) = examiner.examineDouble(this)

    override fun copy() = this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return value == (other as DoubleTag).value
    }

    override fun hashCode(): Int {
        val temp = value.toBits()
        return (temp xor (temp ushr 32)).toInt()
    }

    override fun toLong() = value.floor().toLong()

    override fun toInt() = value.floor()

    override fun toShort() = (value.floor() and '\uFFFF'.code).toShort()

    override fun toByte() = (value.floor() and 255).toByte()

    companion object {

        val ZERO = DoubleTag(0.0)
        val TYPE = TagType("TAG_Double", true)
        val READER = object : TagReader<DoubleTag> {

            override fun read(input: DataInput) = DoubleTag(input.readDouble())
        }
        val WRITER = object : TagWriter<DoubleTag> {

            override fun write(output: DataOutput, tag: DoubleTag) = output.writeDouble(tag.value)
        }
    }
}
