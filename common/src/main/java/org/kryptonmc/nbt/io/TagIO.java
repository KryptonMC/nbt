/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import org.jetbrains.annotations.NotNull;
import org.kryptonmc.nbt.CompoundTag;

/**
 * A utility used for reading and writing NBT data.
 */
public final class TagIO {

    /**
     * Reads a compound tag from the given input, using the given compression
     * to decompress the input before reading the data.
     *
     * @param input the input to read from
     * @param compression the compression to decompress the data with
     * @return the resulting compound tag
     * @throws IOException if an I/O error occurs
     */
    public static @NotNull CompoundTag read(final @NotNull InputStream input, final @NotNull TagCompression compression) throws IOException {
        try (final InputStream stream = compression.decompress(input)) {
            return TagUtil.ensureCompound(TagUtil.readUnnamedTag(stream));
        }
    }

    /**
     * Reads a compound tag from the given path, using the given compression
     * to decompress the input before reading the data, opening a new stream
     * with the given open options.
     *
     * @param path the path to read from
     * @param compression the compression to decompress the data with
     * @param options the options to open the stream with
     * @return the resulting compound tag
     * @throws IOException if an I/O error occurs
     */
    public static @NotNull CompoundTag read(final @NotNull Path path, final @NotNull TagCompression compression,
                                            final @NotNull OpenOption@NotNull... options) throws IOException {
        return read(Files.newInputStream(path, options), compression);
    }

    /**
     * Reads a compound tag from the given file, using the given compression
     * to decompress the input before reading the data.
     *
     * @param file the file to read from
     * @param compression the compression to decompress the data with
     * @return the resulting compound tag
     * @throws IOException if an I/O error occurs
     */
    public static @NotNull CompoundTag read(final @NotNull File file, final @NotNull TagCompression compression) throws IOException {
        return read(new FileInputStream(file), compression);
    }

    /**
     * Reads a named tag from the given input, using the given compression to
     * decompress the input before reading the data.
     *
     * @param input the input to read from
     * @param compression the compression to decompress the data with
     * @return the resulting named tag
     * @throws IOException if an I/O error occurs
     */
    public static @NotNull NamedTag readNamed(final @NotNull InputStream input, final @NotNull TagCompression compression) throws IOException {
        try (final InputStream stream = compression.decompress(input)) {
            return TagUtil.readNamedTag(stream);
        }
    }

    /**
     * Reads a named tag from the given path, using the given compression to
     * decompress the input before reading the data, opening a new stream with
     * the given open options.
     *
     * @param path the path to read from
     * @param compression the compression to decompress the data with
     * @param options the options to open the stream with
     * @return the resulting named tag
     * @throws IOException if an I/O error occurs
     */
    public static @NotNull NamedTag readNamed(final @NotNull Path path, final @NotNull TagCompression compression,
                                              final @NotNull OpenOption@NotNull... options) throws IOException {
        return readNamed(Files.newInputStream(path, options), compression);
    }

    /**
     * Reads a named tag from the given file, using the given compression to
     * decompress the input before reading the data.
     *
     * @param file the file to read from
     * @param compression the compression to decompress the data with
     * @return the resulting named tag
     * @throws IOException if an I/O error occurs
     */
    public static @NotNull NamedTag readNamed(final @NotNull File file, final @NotNull TagCompression compression) throws IOException {
        return readNamed(new FileInputStream(file), compression);
    }

    /**
     * Writes an unnamed compound tag to the given output, using the given
     * compression to compress the output before writing the data.
     *
     * @param output the output to write to
     * @param value the value to write
     * @param compression the compression to compress the data with
     * @throws IOException if an I/O error occurs
     */
    public static void write(final @NotNull OutputStream output, final @NotNull CompoundTag value,
                             final @NotNull TagCompression compression) throws IOException {
        writeNamed(output, "", value, compression);
    }

    /**
     * Writes an unnamed compound tag to the given path, using the given
     * compression to compress the output before writing the data, opening a
     * new stream with the given open options.
     *
     * @param path the path to write to
     * @param value the value to write
     * @param options the options to open the stream with
     * @param compression the compression to compress the data with
     * @throws IOException if an I/O error occurs
     */
    public static void write(final @NotNull Path path, final @NotNull CompoundTag value,
                             final @NotNull TagCompression compression, final @NotNull OpenOption@NotNull... options) throws IOException {
        write(Files.newOutputStream(path, options), value, compression);
    }

    /**
     * Writes an unnamed compound tag to the given file, using the given
     * compression to compress the output before writing the data.
     *
     * @param file the file to write to
     * @param value the value to write
     * @param compression the compression to compress the data with
     * @throws IOException if an I/O error occurs
     */
    public static void write(final @NotNull File file, final @NotNull CompoundTag value,
                             final @NotNull TagCompression compression) throws IOException {
        write(new FileOutputStream(file), value, compression);
    }

    /**
     * Writes a named tag to the given output, using the given compression to
     * compress the output before writing the data.
     *
     * @param output the output to write to
     * @param name the name of the value to write
     * @param value the value to write
     * @param compression the compression to compress the data with
     * @throws IOException if an I/O error occurs
     */
    public static void writeNamed(final @NotNull OutputStream output, final @NotNull String name, final @NotNull CompoundTag value,
                                  final @NotNull TagCompression compression) throws IOException {
        try (final OutputStream stream = compression.compress(output)) {
            TagUtil.writeNamedTag(stream, name, value);
        }
    }

    /**
     * Writes a named tag to the given path, using the given compression to
     * compress the output before writing the data, opening a new stream with
     * the given open options.
     *
     * @param path the path to write to
     * @param name the name of the value to write
     * @param value the value to write
     * @param options the options to open the stream with
     * @param compression the compression to compress the data with
     * @throws IOException if an I/O error occurs
     */
    public static void writeNamed(final @NotNull Path path, final @NotNull String name, final @NotNull CompoundTag value,
                                  final @NotNull TagCompression compression, final @NotNull OpenOption@NotNull... options) throws IOException {
        writeNamed(Files.newOutputStream(path, options), name, value, compression);
    }

    /**
     * Writes a named tag to the given file, using the given compression to
     * compress the output before writing the data.
     *
     * @param file the file to write to
     * @param name the name of the value to write
     * @param value the value to write
     * @param compression the compression to compress the data with
     * @throws IOException if an I/O error occurs
     */
    public static void writeNamed(final @NotNull File file, final @NotNull String name, final @NotNull CompoundTag value,
                                  final @NotNull TagCompression compression) throws IOException {
        writeNamed(new FileOutputStream(file), name, value, compression);
    }

    private TagIO() {
        throw new AssertionError("This class cannot be instantiated!");
    }
}
