package org.kryptonmc.nbt.io

import okio.BufferedSink
import okio.BufferedSource
import okio.IOException
import okio.use
import okio.utf8Size
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.EndTag
import org.kryptonmc.nbt.Tag
import kotlin.jvm.JvmStatic
import kotlin.jvm.JvmSynthetic

/**
 * A utility for reading and writing tags.
 *
 * All compound tags returned here are **mutable**.
 */
public expect object TagIO {

    /**
     * Reads a compound tag from the given [input].
     *
     * @param input the input
     * @return the resulting compound tag
     */
    @JvmStatic
    @Throws(IOException::class)
    public fun read(input: BufferedSource): CompoundTag

    /**
     * Reads a compound tag from the given [input], using the given
     * [compression] to decompress the input before reading the data.
     *
     * @param input the input
     * @return the resulting compound tag
     */
    @JvmStatic
    @Throws(IOException::class)
    public fun read(input: BufferedSource, compression: TagCompression): CompoundTag

    /**
     * Reads a named tag from the given [input].
     *
     * @param input the input
     * @return the resulting name to tag pair
     */
    @JvmStatic
    @Throws(IOException::class)
    public fun readNamed(input: BufferedSource): Pair<String, Tag>

    /**
     * Reads a named tag from the given [input], using the given [compression]
     * to decompress the input before reading the data.
     *
     * @param input the input
     * @param compression the compression
     * @return the resulting name to tag pair
     */
    @JvmStatic
    @Throws(IOException::class)
    public fun readNamed(input: BufferedSource, compression: TagCompression): Pair<String, Tag>

    /**
     * Writes the given [value] to the given [output] uncompressed.
     *
     * @param output the output to write to
     * @param value the value to write
     */
    @JvmStatic
    @Throws(IOException::class)
    public fun write(output: BufferedSink, value: CompoundTag)

    /**
     * Writes the given [value] to the given [output], compressing the data
     * with the given [compression].
     *
     * @param output the output to write to
     * @param compression the compression
     * @param value the value to write
     */
    @JvmStatic
    @Throws(IOException::class)
    public fun write(output: BufferedSink, value: CompoundTag, compression: TagCompression)

    /**
     * Writes the given [value] with the given [name] to the given [output]
     * uncompressed.
     *
     * @param output the output to write to
     * @param name the name of the value to write
     * @param value the value to write
     */
    @JvmStatic
    @Throws(IOException::class)
    public fun writeNamed(output: BufferedSink, name: String, value: CompoundTag)

    /**
     * Writes the given [value] with the given [name] to the given [output],
     * compressing the data with the given [compression].
     *
     * @param output the output to write to
     * @param name the name of the value to write
     * @param value the value to write
     * @param compression the compression
     */
    @JvmStatic
    @Throws(IOException::class)
    public fun writeNamed(output: BufferedSink, name: String, value: CompoundTag, compression: TagCompression)
}
