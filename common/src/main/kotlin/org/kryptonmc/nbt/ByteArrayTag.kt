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
import java.util.function.Consumer

/**
 * A tag that holds a byte array.
 */
public class ByteArrayTag(data: ByteArray) : AbstractCollectionTag<ByteTag>() {

    /**
     * The backing data for this tag.
     */
    public var data: ByteArray = data
        private set
    override val size: Int
        get() = data.size
    override val elementType: Int
        get() = ByteTag.ID

    override val id: Int
        get() = ID
    override val type: TagType
        get() = TYPE

    /**
     * Creates a new byte array tag from the given [data].
     *
     * @param data the backing data for the tag
     */
    public constructor(data: Collection<Byte>) : this(data.toByteArray())

    override fun get(index: Int): ByteTag = ByteTag.of(data[index])

    public fun set(index: Int, value: Byte) {
        data[index] = value
    }

    override fun set(index: Int, element: ByteTag): ByteTag {
        val old = data[index]
        data[index] = element.value
        return ByteTag.of(old)
    }

    public fun add(value: Byte) {
        data = data.copyOf(data.size + 1)
        data[data.size - 1] = value
    }

    public fun add(index: Int, value: Byte) {
        data = data.add(index, value)
    }

    override fun add(index: Int, element: ByteTag) {
        add(index, element.value)
    }

    public fun remove(index: Int) {
        data = data.remove(index)
    }

    override fun removeAt(index: Int): ByteTag {
        val old = data[index]
        data = data.remove(index)
        return ByteTag.of(old)
    }

    @JvmSynthetic
    public inline fun forEachByte(action: (Byte) -> Unit) {
        data.forEach(action)
    }

    public fun forEachByte(action: Consumer<Byte>) {
        data.forEach(action::accept)
    }

    override fun clear() {
        data = EMPTY_DATA
    }

    override fun write(output: DataOutput) {
        WRITER.write(output, this)
    }

    override fun <T> examine(examiner: TagExaminer<T>) {
        examiner.examineByteArray(this)
    }

    override fun copy(): ByteArrayTag {
        val copy = ByteArray(data.size)
        System.arraycopy(data, 0, copy, 0, data.size)
        return ByteArrayTag(copy)
    }

    override fun equals(other: Any?): Boolean = this === other || (other is ByteArrayTag && data.contentEquals(other.data))

    override fun hashCode(): Int = data.contentHashCode()

    override fun toString(): String = "ByteArrayTag(data=${data.contentToString()})"

    public companion object {

        public const val ID: Int = 7
        @JvmField
        public val TYPE: TagType = TagType("TAG_Byte_Array")
        @JvmField
        public val READER: TagReader<ByteArrayTag> = TagReader { input, _ ->
            val size = input.readInt()
            val bytes = ByteArray(size)
            input.readFully(bytes)
            ByteArrayTag(bytes)
        }
        @JvmField
        public val WRITER: TagWriter<ByteArrayTag> = TagWriter { output, value ->
            output.writeInt(value.data.size)
            output.write(value.data)
        }
        @JvmSynthetic
        internal val EMPTY_DATA: ByteArray = ByteArray(0)
    }
}
