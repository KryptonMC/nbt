package org.kryptonmc.nbt

import org.kryptonmc.nbt.io.TagCompression
import org.kryptonmc.nbt.io.TagReader
import org.kryptonmc.nbt.io.TagWriter
import java.io.DataInput
import java.io.DataOutput
import java.io.InputStream
import java.io.OutputStream

object EndTag : Tag<EndTag> {

    override val id = 0
    override val type = TagType("TAG_End", true)
    override val reader = object : TagReader<EndTag> {

        override fun read(input: DataInput) = EndTag

        override fun read(input: InputStream) = EndTag

        override fun read(input: InputStream, compression: TagCompression) = EndTag
    }
    override val writer = object : TagWriter<EndTag> {

        override fun write(output: DataOutput, tag: EndTag) = Unit

        override fun write(output: OutputStream, tag: EndTag) = Unit

        override fun write(output: OutputStream, tag: EndTag, compression: TagCompression) = Unit
    }

    override fun <T> examine(examiner: TagExaminer<T>) = examiner.examineEnd(this)

    override fun copy() = this
}
