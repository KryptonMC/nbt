/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

import org.kryptonmc.nbt.io.TagReader
import org.kryptonmc.nbt.io.TagWriter
import org.kryptonmc.nbt.util.add
import org.kryptonmc.nbt.util.remove
import java.io.DataInput
import java.io.DataOutput

@Suppress("UNCHECKED_CAST")
public class IntArrayTag(data: IntArray) : CollectionTag<IntTag>(IntTag.ID) {

    public var data: IntArray = data
        private set

    override val id: Int = ID
    override val type: TagType = TYPE
    override val reader: TagReader<IntArrayTag> = READER
    override val writer: TagWriter<Tag> = WRITER as TagWriter<Tag>
    override val size: Int
        get() = data.size

    public constructor(data: Collection<Int>) : this(data.toIntArray())

    override fun get(index: Int): IntTag = IntTag.of(data[index])

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

    override fun <T> examine(examiner: TagExaminer<T>): Unit = examiner.examineIntArray(this)

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

    override fun hashCode(): Int = data.contentHashCode()

    public companion object {

        public const val ID: Int = 11
        public val TYPE: TagType = TagType("TAG_Int_Array")
        public val READER: TagReader<IntArrayTag> = object : TagReader<IntArrayTag> {

            override fun read(input: DataInput, depth: Int): IntArrayTag {
                val size = input.readInt()
                val ints = IntArray(size)
                for (i in 0 until size) ints[i] = input.readInt()
                return IntArrayTag(ints)
            }
        }
        public val WRITER: TagWriter<IntArrayTag> = object : TagWriter<IntArrayTag> {

            override fun write(output: DataOutput, tag: IntArrayTag) {
                output.writeInt(tag.data.size)
                tag.data.forEach { output.writeInt(it) }
            }
        }
    }
}
