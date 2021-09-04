/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

import okio.BufferedSink
import okio.BufferedSource
import org.kryptonmc.nbt.io.TagReader
import org.kryptonmc.nbt.io.TagWriter
import org.kryptonmc.nbt.util.add
import org.kryptonmc.nbt.util.arraycopy
import org.kryptonmc.nbt.util.remove
import kotlin.jvm.JvmField

public class ByteArrayTag(data: ByteArray) : AbstractMutableList<ByteTag>(), MutableCollectionTag<ByteTag> {

    public var data: ByteArray = data
        private set

    override val id: Int = ID
    override val elementType: Int = ByteTag.ID
    override val type: TagType = TYPE
    override val size: Int
        get() = data.size

    public constructor(data: Collection<Byte>) : this(data.toByteArray())

    override fun get(index: Int): ByteTag = ByteTag.of(data[index])

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

    override fun write(output: BufferedSink) {
        WRITER.write(output, this)
    }

    override fun <T> examine(examiner: TagExaminer<T>): Unit = examiner.examineByteArray(this)

    override fun copy(): ByteArrayTag {
        val copy = ByteArray(data.size)
        arraycopy(data, 0, copy, 0, data.size)
        return ByteArrayTag(copy)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ByteArrayTag) return false
        return data.contentEquals(other.data)
    }

    override fun hashCode(): Int = data.contentHashCode()

    override fun toString(): String = "ByteArrayTag(data=${data.contentToString()})"

    public companion object {

        public const val ID: Int = 7
        @JvmField
        public val TYPE: TagType = TagType("TAG_Byte_Array")
        @JvmField
        public val READER: TagReader<ByteArrayTag> = object : TagReader<ByteArrayTag> {

            override fun read(input: BufferedSource, depth: Int): ByteArrayTag {
                val size = input.readInt()
                val bytes = ByteArray(size)
                input.readFully(bytes)
                return ByteArrayTag(bytes)
            }
        }
        @JvmField
        public val WRITER: TagWriter<ByteArrayTag> = object : TagWriter<ByteArrayTag> {

            override fun write(output: BufferedSink, value: ByteArrayTag) {
                output.writeInt(value.data.size)
                output.write(value.data)
            }
        }
    }
}
