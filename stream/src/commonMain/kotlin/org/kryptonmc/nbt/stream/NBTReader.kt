/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.stream

import okio.BufferedSource
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
import kotlin.jvm.JvmStatic

/**
 * A reader for decoding NBT using the streaming API.
 */
public interface NBTReader : Closeable {

    /**
     * Gets the type of the next tag available to this reader.
     *
     * @return the type of the next tag
     * @throws IOException if an I/O error occurred
     */
    public fun peekType(): Byte

    /**
     * Opens a new byte array scope for this reader.
     *
     * @return the size of the byte array
     * @throws IllegalStateException if the read type is not a byte array,
     * or the nesting is too deep
     * @throws IOException if an I/O error occurred
     */
    public fun beginByteArray(): Int

    /**
     * Closes the current byte array scope.
     *
     * @throws IllegalStateException if the current scope is not a byte array,
     * or the last thing that was written was a name
     */
    public fun endByteArray()

    /**
     * Opens a new integer array scope for this reader.
     *
     * @return the size of the integer array
     * @throws IllegalStateException if the read type is not an integer array,
     * or the nesting is too deep
     * @throws IOException if an I/O error occurred
     */
    public fun beginIntArray(): Int

    /**
     * Closes the current integer array scope.
     *
     * @throws IllegalStateException if the current scope is not an integer
     * array, or the last thing that was written was a name
     */
    public fun endIntArray()

    /**
     * Opens a new long array scope for this reader.
     *
     * @return the size of the long array
     * @throws IllegalStateException if the read type is not a long array,
     * or the nesting is too deep
     * @throws IOException if an I/O error occurred
     */
    public fun beginLongArray(): Int

    /**
     * Closes the current long array scope.
     *
     * @throws IllegalStateException if the current scope is not a long array,
     * or the last thing that was written was a name
     */
    public fun endLongArray()

    /**
     * Opens a new list scope for this reader, expecting the list to hold
     * elements of the given [elementType].
     *
     * @return the size of the byte array
     * @throws IllegalStateException if the read type is not an integer array,
     * the nesting is too deep, or the element type is not of the expected
     * type
     * @throws IOException if an I/O error occurred
     */
    public fun beginList(elementType: Int): Int

    /**
     * Closes the current list scope.
     *
     * @throws IllegalStateException if the current scope is not a list,
     * or the last thing that was written was a name
     */
    public fun endList()

    /**
     * Opens a new compound scope for this reader.
     *
     * @throws IllegalStateException if the read type is not a compound,
     * or the nesting is too deep
     * @throws IOException if an I/O error occurred
     */
    public fun beginCompound()

    /**
     * Closes the current compound scope.
     *
     * @throws IllegalStateException if the current scope is not a compound,
     * the last thing that was written was a name, or the next tag is not an
     * end tag
     */
    public fun endCompound()

    /**
     * Checks if this reader has a next value that can be read.
     *
     * @return true if there is more data to be read, false otherwise
     */
    public fun hasNext(): Boolean

    /**
     * Reads the type of the next available tag.
     *
     * @return the type of the next tag
     * @throws IOException if an I/O error occurs
     */
    public fun nextType(): Byte

    /**
     * Reads the name of the next available compound tag.
     *
     * @return the name of the next compound tag
     * @throws IllegalStateException if the current scope is not a compound,
     * or the writer is closed
     * @throws IOException if an I/O error occurs
     */
    public fun nextName(): String

    /**
     * Reads the next available byte tag value.
     *
     * @return the next byte
     * @throws IllegalStateException if the type of the next tag is not a
     * byte tag
     * @throws IOException if an I/O error occurs
     */
    public fun nextByte(): Byte

    /**
     * Reads the next available short tag value.
     *
     * @return the next short
     * @throws IllegalStateException if the type of the next tag is not a
     * short tag
     * @throws IOException if an I/O error occurs
     */
    public fun nextShort(): Short

    /**
     * Reads the next available integer tag value.
     *
     * @return the next integer
     * @throws IllegalStateException if the type of the next tag is not an
     * integer tag
     * @throws IOException if an I/O error occurs
     */
    public fun nextInt(): Int

    /**
     * Reads the next available float tag value.
     *
     * @return the next float
     * @throws IllegalStateException if the type of the next tag is not a
     * long tag
     * @throws IOException if an I/O error occurs
     */
    public fun nextLong(): Long

    /**
     * Reads the next available float tag value.
     *
     * @return the next float
     * @throws IllegalStateException if the type of the next tag is not a
     * float tag
     * @throws IOException if an I/O error occurs
     */
    public fun nextFloat(): Float

    /**
     * Reads the next available double tag value.
     *
     * @return the next double
     * @throws IllegalStateException if the type of the next tag is not a
     * double tag
     * @throws IOException if an I/O error occurs
     */
    public fun nextDouble(): Double

    /**
     * Reads the next available string tag value.
     *
     * @return the next string
     * @throws IllegalStateException if the type of the next tag is not a
     * string tag
     * @throws IOException if an I/O error occurs
     */
    public fun nextString(): String

    /**
     * Reads and consumes the next end tag.
     *
     * @throws IllegalStateException if the type of the next tag is not
     * the end tag
     * @throws IOException if an I/O error occurs
     */
    public fun nextEnd()

    /**
     * Reads the next generic tag.
     *
     * @return the next generic tag
     */
    public fun read(): Tag = when (peekType().toInt()) {
        ByteArrayTag.ID -> {
            val size = beginByteArray()
            val bytes = ByteArray(size)
            var i = 0
            while (hasNext()) {
                bytes[i] = nextByte()
                ++i
            }
            endByteArray()
            ByteArrayTag(bytes)
        }
        IntArrayTag.ID -> {
            val size = beginIntArray()
            val ints = IntArray(size)
            var i = 0
            while (hasNext()) {
                ints[i] = nextInt()
                ++i
            }
            endIntArray()
            IntArrayTag(ints)
        }
        LongArrayTag.ID -> {
            val size = beginLongArray()
            val longs = LongArray(size)
            var i = 0
            while (hasNext()) {
                longs[i] = nextLong()
                ++i
            }
            endLongArray()
            LongArrayTag(longs)
        }
        ListTag.ID -> {
            val type = nextType().toInt()
            val size = beginList(type)
            if (type == 0 && size > 0) {
                throw RuntimeException("Missing required type for non-empty list tag!")
            }
            val list = ListTag.builder(type)
            while (hasNext()) {
                list.add(read())
            }
            endList()
            list.build()
        }
        CompoundTag.ID -> {
            var type = nextType().toInt()
            val compound = CompoundTag.builder()
            while (type != 0) {
                val name = nextName()
                val value = read()
                compound.put(name, value)
                type = nextType().toInt()
            }
            compound.build()
        }
        ByteTag.ID -> ByteTag.of(nextByte())
        ShortTag.ID -> ShortTag.of(nextShort())
        IntTag.ID -> IntTag.of(nextInt())
        LongTag.ID -> LongTag.of(nextLong())
        FloatTag.ID -> FloatTag.of(nextFloat())
        DoubleTag.ID -> DoubleTag.of(nextDouble())
        StringTag.ID -> StringTag.of(nextString())
        else -> error("Expected a value, but was ${peekType()}!")
    }

    public companion object {

        /**
         * Creates a new NBT reader for reading binary NBT data.
         *
         * @param source the binary source
         * @return a new binary NBT reader
         */
        @JvmStatic
        public fun binary(source: BufferedSource): NBTReader = BinaryNBTReader(source)
    }
}
