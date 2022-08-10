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
import java.io.DataOutput
import java.util.function.IntConsumer

/**
 * A tag that holds an integer array.
 */
public class IntArrayTag(data: IntArray) : AbstractCollectionTag<IntTag>() {

    /**
     * The backing data for this tag.
     */
    public var data: IntArray = data
        private set
    override val size: Int
        get() = data.size
    override val elementType: Int
        get() = IntTag.ID

    override val id: Int
        get() = ID
    override val type: TagType
        get() = TYPE

    /**
     * Creates a new integer array tag from the given [data].
     *
     * @param data the backing data for the tag
     */
    public constructor(data: Collection<Int>) : this(data.toIntArray())

    override fun get(index: Int): IntTag = IntTag.of(data[index])

    public fun set(index: Int, value: Int) {
        data[index] = value
    }

    override fun set(index: Int, element: IntTag): IntTag {
        val old = data[index]
        data[index] = element.value
        return IntTag.of(old)
    }

    public fun add(value: Int) {
        data = data.copyOf(data.size + 1)
        data[data.size - 1] = value
    }

    public fun add(index: Int, value: Int) {
        data = data.add(index, value)
    }

    override fun add(index: Int, element: IntTag) {
        add(index, element.value)
    }

    public fun remove(index: Int) {
        data = data.remove(index)
    }

    override fun removeAt(index: Int): IntTag {
        val old = data[index]
        data = data.remove(index)
        return IntTag.of(old)
    }

    @JvmSynthetic
    public inline fun forEachInt(action: (Int) -> Unit) {
        data.forEach(action)
    }

    public fun forEachInt(action: IntConsumer) {
        data.forEach(action::accept)
    }

    override fun clear() {
        data = EMPTY_DATA
    }

    override fun write(output: DataOutput) {
        WRITER.write(output, this)
    }

    override fun <T> examine(examiner: TagExaminer<T>) {
        examiner.examineIntArray(this)
    }

    override fun copy(): IntArrayTag {
        val copy = IntArray(data.size)
        System.arraycopy(data, 0, copy, 0, data.size)
        return IntArrayTag(copy)
    }

    override fun equals(other: Any?): Boolean = this === other || (other is IntArrayTag && data.contentEquals(other.data))

    override fun hashCode(): Int = data.contentHashCode()

    override fun toString(): String = "IntArrayTag(data=${data.contentToString()})"

    public companion object {

        public const val ID: Int = 11
        @JvmField
        public val TYPE: TagType = TagType("TAG_Int_Array")
        @JvmField
        public val READER: TagReader<IntArrayTag> = TagReader { input, _ ->
            val size = input.readInt()
            val ints = IntArray(size)
            for (i in 0 until size) {
                ints[i] = input.readInt()
            }
            IntArrayTag(ints)
        }
        @JvmField
        public val WRITER: TagWriter<IntArrayTag> = TagWriter { output, value ->
            output.writeInt(value.data.size)
            for (i in value.data.indices) {
                output.writeInt(value.data[i])
            }
        }
        @JvmSynthetic
        internal val EMPTY_DATA: IntArray = IntArray(0)
    }
}
