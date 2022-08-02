/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.io

import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.Tag
import org.kryptonmc.nbt.util.NoSpread
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.OpenOption
import java.nio.file.Path

/**
 * A utility for reading and writing tags.
 *
 * All compound tags returned here are **mutable**.
 */
public object TagIO {

    /**
     * Reads a compound tag from the given [input], using the given
     * [compression] to decompress the input before reading the data.
     *
     * If no compression is provided, it defaults to [TagCompression.NONE],
     * which performs no compression on the data.
     *
     * @param input the input to read from
     * @param compression the compression to decompress the data with
     * @return the resulting compound tag
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun read(input: InputStream, compression: TagCompression = TagCompression.NONE): CompoundTag =
        compression.decompress(input).use { it.readUnnamedTag(0) }.ensureCompound()

    /**
     * Reads a compound tag from the given [path], using the given
     * [compression] to decompress the input before reading the data.
     *
     * If no compression is provided, it defaults to [TagCompression.NONE],
     * which performs no compression on the data.
     *
     * @param path the path to read from
     * @param compression the compression to decompress the data with
     * @return the resulting compound tag
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun read(path: Path, compression: TagCompression = TagCompression.NONE): CompoundTag = read(Files.newInputStream(path), compression)

    /**
     * Reads a compound tag from the given [path], using the given
     * [compression] to decompress the input before reading the data, and
     * opening a new input stream with the given open [options].
     *
     * If no compression is provided, it defaults to [TagCompression.NONE],
     * which performs no compression on the data.
     *
     * @param path the path to read from
     * @param compression the compression to decompress the data with
     * @param options the open options for the input stream
     * @return the resulting compound tag
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun read(path: Path, compression: TagCompression = TagCompression.NONE, vararg options: OpenOption): CompoundTag =
        read(NoSpread.filesNewInputStream(path, options), compression)

    /**
     * Reads a compound tag from the given [file], using the given
     * [compression] to decompress the input before reading the data.
     *
     * If no compression is provided, it defaults to [TagCompression.NONE],
     * which performs no compression on the data.
     *
     * @param file the file to read from
     * @param compression the compression to decompress the data with
     * @return the resulting compound tag
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun read(file: File, compression: TagCompression = TagCompression.NONE): CompoundTag = read(FileInputStream(file), compression)

    /**
     * Reads a named tag from the given [input], using the given [compression]
     * to decompress the input before reading the data.
     *
     * If no compression is provided, it defaults to [TagCompression.NONE],
     * which performs no compression on the data.
     *
     * @param input the input to read from
     * @param compression the compression to decompress the data with
     * @return the resulting name to tag pair
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun readNamed(input: InputStream, compression: TagCompression = TagCompression.NONE): Pair<String, Tag> =
        compression.decompress(input).use { it.readNamedTag(0) }

    /**
     * Reads a named tag from the given [path], using the given [compression]
     * to decompress the input before reading the data.
     *
     * If no compression is provided, it defaults to [TagCompression.NONE],
     * which performs no compression on the data.
     *
     * @param path the path to read from
     * @param compression the compression to decompress the data with
     * @return the resulting compound tag
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun readNamed(path: Path, compression: TagCompression = TagCompression.NONE): Pair<String, Tag> =
        readNamed(Files.newInputStream(path), compression)

    /**
     * Reads a named tag from the given [path], using the given [compression]
     * to decompress the input before reading the data, and opening a new input
     * stream with the given open [options].
     *
     * If no compression is provided, it defaults to [TagCompression.NONE],
     * which performs no compression on the data.
     *
     * @param path the path to read from
     * @param compression the compression to decompress the data with
     * @param options the open options for the input stream
     * @return the resulting compound tag
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun readNamed(path: Path, compression: TagCompression = TagCompression.NONE, vararg options: OpenOption): Pair<String, Tag> =
        readNamed(NoSpread.filesNewInputStream(path, options), compression)

    /**
     * Reads a named tag from the given [file], using the given [compression]
     * to decompress the input before reading the data.
     *
     * If no compression is provided, it defaults to [TagCompression.NONE],
     * which performs no compression on the data.
     *
     * @param file the file to read from
     * @param compression the compression to decompress the data with
     * @return the resulting compound tag
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun readNamed(file: File, compression: TagCompression = TagCompression.NONE): Pair<String, Tag> =
        readNamed(FileInputStream(file), compression)

    /**
     * Writes the given [value] to the given [output], compressing the data
     * with the given [compression].
     *
     * If no compression is provided, it defaults to [TagCompression.NONE],
     * which performs no compression on the data.
     *
     * @param output the output to write to
     * @param value the value to write
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun write(output: OutputStream, value: CompoundTag, compression: TagCompression = TagCompression.NONE) {
        compression.compress(output).use { it.writeNamedTag("", value) }
    }

    /**
     * Writes the given [value] to the given [path], compressing the data with
     * the given [compression].
     *
     * If no compression is provided, it defaults to [TagCompression.NONE],
     * which performs no compression on the data.
     *
     * @param path the path to write to
     * @param value the value to write
     * @param compression the compression to compress the data with
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun write(path: Path, value: CompoundTag, compression: TagCompression = TagCompression.NONE) {
        write(Files.newOutputStream(path), value, compression)
    }

    /**
     * Writes the given [value] to the given [path], compressing the data with
     * the given [compression], and opening a new output stream with the given
     * [options].
     *
     * If no compression is provided, it defaults to [TagCompression.NONE],
     * which performs no compression on the data.
     *
     * @param path the path to write to
     * @param value the value to write
     * @param compression the compression to compress the data with
     * @param options the options to open the output stream with
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun write(path: Path, value: CompoundTag, compression: TagCompression = TagCompression.NONE, vararg options: OpenOption) {
        write(NoSpread.filesNewOutputStream(path, options), value, compression)
    }

    /**
     * Writes the given [value] to the given [file], compressing the data with
     * the given [compression].
     *
     * If no compression is provided, it defaults to [TagCompression.NONE],
     * which performs no compression on the data.
     *
     * @param file the file to write to
     * @param compression the compression to compress the data with
     * @param value the value to write
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun write(file: File, value: CompoundTag, compression: TagCompression = TagCompression.NONE) {
        write(FileOutputStream(file), value, compression)
    }

    /**
     * Writes the given [value] with the given [name] to the given [output]
     * uncompressed.
     *
     * @param output the output to write to
     * @param name the name of the value to write
     * @param value the value to write
     * @param compression the compression to compress the data with
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun writeNamed(output: OutputStream, name: String, value: CompoundTag, compression: TagCompression = TagCompression.NONE) {
        compression.compress(output).use { it.writeNamedTag(name, value) }
    }

    /**
     * Writes the given [value] to the given [path], compressing the data with
     * the given [compression].
     *
     * If no compression is provided, it defaults to [TagCompression.NONE],
     * which performs no compression on the data.
     *
     * @param path the path to write to
     * @param name the name of the value
     * @param value the value to write
     * @param compression the compression to compress the data with
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun writeNamed(path: Path, name: String, value: CompoundTag, compression: TagCompression = TagCompression.NONE) {
        writeNamed(Files.newOutputStream(path), name, value, compression)
    }

    /**
     * Writes the given [value] to the given [path], compressing the data with
     * the given [compression], and opening a new output stream with the given
     * [options].
     *
     * If no compression is provided, it defaults to [TagCompression.NONE],
     * which performs no compression on the data.
     *
     * @param path the path to write to
     * @param name the name of the value
     * @param value the value to write
     * @param compression the compression to compress the data with
     * @param options the options to open the output stream with
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun writeNamed(
        path: Path,
        name: String,
        value: CompoundTag,
        compression: TagCompression = TagCompression.NONE,
        vararg options: OpenOption
    ) {
        writeNamed(NoSpread.filesNewOutputStream(path, options), name, value, compression)
    }

    /**
     * Writes the given [value] to the given [file], compressing the data with
     * the given [compression].
     *
     * If no compression is provided, it defaults to [TagCompression.NONE],
     * which performs no compression on the data.
     *
     * @param file the file to write to
     * @param name the name of the value
     * @param value the value to write
     * @param compression the compression to compress the data with
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun writeNamed(file: File, name: String, value: CompoundTag, compression: TagCompression = TagCompression.NONE) {
        writeNamed(FileOutputStream(file), name, value, compression)
    }
}

private fun Tag.ensureCompound(): CompoundTag = this as? CompoundTag ?: throw IOException("Root tag must be an unnamed compound!")
