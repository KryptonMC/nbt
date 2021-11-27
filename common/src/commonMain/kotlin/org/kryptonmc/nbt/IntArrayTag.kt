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
import org.kryptonmc.nbt.util.remove
import kotlin.jvm.JvmField

/**
 * A tag that holds an integer array.
 */
public class IntArrayTag(data: IntArray) : AbstractMutableList<IntTag>(), MutableCollectionTag<IntTag> {

    /**
     * The backing data for this tag.
     */
    public var data: IntArray = data
        private set

    override val id: Int = ID
    override val elementType: Int = IntTag.ID
    override val type: TagType = TYPE
    override val size: Int
        get() = data.size

    /**
     * Creates a new integer array tag from the given [data].
     *
     * @param data the backing data for the tag
     */
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

    override fun write(output: BufferedSink) {
        WRITER.write(output, this)
    }

    override fun <T> examine(examiner: TagExaminer<T>): Unit = examiner.examineIntArray(this)

    override fun copy(): IntArrayTag {
        val copy = IntArray(data.size)
        data.copyInto(copy, 0, 0, data.size)
        return IntArrayTag(copy)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is IntArrayTag) return false
        return data.contentEquals(other.data)
    }

    override fun hashCode(): Int = data.contentHashCode()

    override fun toString(): String = "IntArrayTag(data=${data.contentToString()})"

    public companion object {

        public const val ID: Int = 11
        @JvmField
        public val TYPE: TagType = TagType("TAG_Int_Array")
        @JvmField
        public val READER: TagReader<IntArrayTag> = object : TagReader<IntArrayTag> {

            override fun read(input: BufferedSource, depth: Int): IntArrayTag {
                val size = input.readInt()
                val ints = IntArray(size)
                for (i in 0 until size) ints[i] = input.readInt()
                return IntArrayTag(ints)
            }
        }
        @JvmField
        public val WRITER: TagWriter<IntArrayTag> = object : TagWriter<IntArrayTag> {

            override fun write(output: BufferedSink, value: IntArrayTag) {
                output.writeInt(value.data.size)
                for (i in value.data.indices) output.writeInt(value.data[i])
            }
        }
    }
}
