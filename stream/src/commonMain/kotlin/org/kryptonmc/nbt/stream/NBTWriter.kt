/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.stream

import okio.BufferedSink
import okio.Closeable
import okio.IOException
import org.kryptonmc.nbt.ByteArrayTag
import org.kryptonmc.nbt.ByteTag
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.DoubleTag
import org.kryptonmc.nbt.FloatTag
import org.kryptonmc.nbt.IntArrayTag
import org.kryptonmc.nbt.IntTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.LongArrayTag
import org.kryptonmc.nbt.LongTag
import org.kryptonmc.nbt.ShortTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.Tag
import org.kryptonmc.nbt.util.UUID
import kotlin.jvm.JvmStatic

public interface NBTWriter : Closeable {

    /**
     * Opens a new byte array scope for this writer.
     *
     * @param size the expected size of the array
     * @throws IllegalArgumentException if the size is < 0, or no name has been
     * set with [name]
     * @throws IllegalStateException if the nesting is too high
     * @throws IOException if an I/O error occurred
     */
    public fun beginByteArray(size: Int)

    /**
     * Closes the current byte array scope.
     *
     * @throws IllegalStateException if the current scope is not a byte array,
     * or the last thing that was written was a name
     */
    public fun endByteArray()

    /**
     * Opens a new integer array scope for this writer.
     *
     * @param size the expected size of the array
     * @throws IllegalArgumentException if the size is < 0, or no name has been
     * set with [name]
     * @throws IllegalStateException if the nesting is too high
     * @throws IOException if an I/O error occurred
     */
    public fun beginIntArray(size: Int)

    /**
     * Closes the current integer array scope.
     *
     * @throws IllegalStateException if the current scope is not an integer
     * array, or the last thing that was written was a name
     */
    public fun endIntArray()

    /**
     * Opens a new long array scope for this writer.
     *
     * @param size the expected size of the array
     * @throws IllegalArgumentException if the size is < 0, or no name has been
     * set with [name]
     * @throws IllegalStateException if the nesting is too high
     * @throws IOException if an I/O error occurred
     */
    public fun beginLongArray(size: Int)

    /**
     * Closes the current long array scope.
     *
     * @throws IllegalStateException if the current scope is not a long array,
     * or the last thing that was written was a name
     */
    public fun endLongArray()

    /**
     * Opens a new list scope for this writer.
     *
     * @param elementType the expected type of all the tags
     * @param size the expected size of the list
     * @throws IllegalArgumentException if the size is < 0, or no name has been
     * set with [name]
     * @throws IllegalStateException if the element type is non-zero and the
     * size is zero, or the nesting is too high
     * @throws IOException if an I/O error occurred
     */
    public fun beginList(elementType: Int, size: Int)

    /**
     * Closes the current list scope.
     *
     * @throws IllegalStateException if the current scope is not a list,
     * or the last thing that was written was a name
     */
    public fun endList()

    /**
     * Opens a new compound scope for this writer.
     *
     * @throws IllegalArgumentException if no name has been set with [name]
     * @throws IOException if an I/O error occurred
     */
    public fun beginCompound()

    /**
     * Closes the current compound scope.
     *
     * @throws IllegalStateException if the current scope is not a compound,
     * or the last thing that was written was a name
     */
    public fun endCompound()

    /**
     * Writes the given [name] to this writer.
     *
     * @throws IllegalStateException if the current scope is not a compound
     */
    public fun name(name: String)

    /**
     * Writes the given boolean [value] to this writer.
     *
     * @param value the value
     * @throws IllegalArgumentException if no name has been set using [name]
     * @throws IOException if an I/O error occurs
     */
    public fun value(value: Boolean)

    /**
     * Writes the given byte [value] to this writer.
     *
     * @param value the value
     * @throws IllegalArgumentException if no name has been set using [name]
     * @throws IOException if an I/O error occurs
     */
    public fun value(value: Byte)

    /**
     * Writes the given short [value] to this writer.
     *
     * @param value the value
     * @throws IllegalArgumentException if no name has been set using [name]
     * @throws IOException if an I/O error occurs
     */
    public fun value(value: Short)

    /**
     * Writes the given integer [value] to this writer.
     *
     * @param value the value
     * @throws IllegalArgumentException if no name has been set using [name]
     * @throws IOException if an I/O error occurs
     */
    public fun value(value: Int)

    /**
     * Writes the given long [value] to this writer.
     *
     * @param value the value
     * @throws IllegalArgumentException if no name has been set using [name]
     * @throws IOException if an I/O error occurs
     */
    public fun value(value: Long)

    /**
     * Writes the given float [value] to this writer.
     *
     * @param value the value
     * @throws IllegalArgumentException if no name has been set using [name]
     * @throws IOException if an I/O error occurs
     */
    public fun value(value: Float)

    /**
     * Writes the given double [value] to this writer.
     *
     * @param value the value
     * @throws IllegalArgumentException if no name has been set using [name]
     * @throws IOException if an I/O error occurs
     */
    public fun value(value: Double)

    /**
     * Writes the given string [value] to this writer.
     *
     * @param value the value
     * @throws IllegalArgumentException if no name has been set using [name]
     * @throws IOException if an I/O error occurs
     */
    public fun value(value: String)

    /**
     * Writes the given UUID [value] to this writer.
     *
     * @param value the value
     * @throws IllegalArgumentException if no name has been set using [name]
     * @throws IOException if an I/O error occurs
     */
    public fun value(value: UUID)

    /**
     * Writes an end tag to this writer.
     *
     * @throws IOException if an I/O error occurs
     */
    public fun end()

    /**
     * Writes the given generic [tag] to this writer.
     *
     * @param tag the tag to write
     */
    public fun write(tag: Tag): Unit = when (tag) {
        is ByteTag -> value(tag.value)
        is ShortTag -> value(tag.value)
        is IntTag -> value(tag.value)
        is LongTag -> value(tag.value)
        is FloatTag -> value(tag.value)
        is DoubleTag -> value(tag.value)
        is StringTag -> value(tag.value)
        is ListTag -> {
            beginList(tag.elementType, tag.size)
            tag.forEach { write(it) }
            endList()
        }
        is CompoundTag -> {
            beginCompound()
            tag.tags.forEach {
                name(it.key)
                write(it.value)
            }
            endCompound()
        }
        is ByteArrayTag -> {
            beginByteArray(tag.size)
            tag.forEach { value(it) }
            endByteArray()
        }
        is IntArrayTag -> {
            beginIntArray(tag.size)
            tag.forEach { value(it) }
            endIntArray()
        }
        is LongArrayTag -> {
            beginLongArray(tag.size)
            tag.forEach { value(it) }
            endLongArray()
        }
        else -> error("Don't know how to write $tag!")
    }

    public companion object {

        /**
         * Creates a new NBT writer for writing binary NBT data.
         *
         * @param sink the binary sink
         * @return a new binary NBT writer
         */
        @JvmStatic
        public fun binary(sink: BufferedSink): NBTWriter = BinaryNBTWriter(sink)
    }
}
