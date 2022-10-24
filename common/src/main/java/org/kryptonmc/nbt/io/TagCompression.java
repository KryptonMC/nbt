/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;
import org.jetbrains.annotations.NotNull;

/**
 * A compression type for NBT data.
 *
 * @param decompressor the decompressor function
 * @param compressor the compressor function
 */
public record TagCompression(@NotNull CompressorFunction<@NotNull InputStream> decompressor,
                             @NotNull CompressorFunction<@NotNull OutputStream> compressor) {

    /**
     * The compression type for uncompressed NBT data.
     */
    public static final @NotNull TagCompression NONE = new TagCompression(input -> input, output -> output);
    /**
     * The compression type for GZIP compressed data.
     */
    public static final @NotNull TagCompression GZIP = new TagCompression(GZIPInputStream::new, GZIPOutputStream::new);
    /**
     * The compression type for ZLIB compressed data.
     */
    public static final @NotNull TagCompression ZLIB = new TagCompression(InflaterInputStream::new, DeflaterOutputStream::new);

    /**
     * Decompresses the given input stream using the decompressor function.
     *
     * @param input the input
     * @return the decompressed input
     * @throws IOException if an I/O error occurs
     */
    public @NotNull InputStream decompress(final @NotNull InputStream input) throws IOException {
        return decompressor.apply(input);
    }

    /**
     * Compresses the given output stream using the compressor function.
     *
     * @param output the output
     * @return the compressed output
     * @throws IOException if an I/O error occurs
     */
    public @NotNull OutputStream compress(final @NotNull OutputStream output) throws IOException {
        return compressor.apply(output);
    }

    /**
     * A function that takes a closeable output and returns the compressed or
     * decompressed variant.
     *
     * @param <T> the type of the value
     */
    @FunctionalInterface
    public interface CompressorFunction<T extends @NotNull Closeable> {

        /**
         * Performs this function on the given value.
         *
         * @param value the value
         * @return the result
         * @throws IOException if an I/O error occurs
         */
        @NotNull T apply(final @NotNull T value) throws IOException;
    }
}
