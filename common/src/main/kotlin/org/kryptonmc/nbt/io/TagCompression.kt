/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.io

import java.io.InputStream
import java.io.OutputStream
import java.util.function.UnaryOperator
import java.util.zip.DeflaterOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import java.util.zip.InflaterInputStream

/**
 * Compression used to compress and decompress input sources and output sinks.
 *
 * @param decompressor the decompressor
 * @param compressor the compressor
 */
public class TagCompression(private val decompressor: UnaryOperator<InputStream>, private val compressor: UnaryOperator<OutputStream>) {

    /**
     * Applies the decompressor to the given [input] and returns the resulting
     * source.
     *
     * @param input the input
     * @return the decompressed source
     */
    public fun decompress(input: InputStream): InputStream = decompressor.apply(input)

    /**
     * Applies the compressor to the given [output] and returns the resulting
     * sink.
     *
     * @param output the output
     * @return the compressed source
     */
    public fun compress(output: OutputStream): OutputStream = compressor.apply(output)

    public companion object {

        /**
         * Tag compression that passes the data through as-is, without applying
         * any compression or decompression to it.
         */
        @JvmField
        public val NONE: TagCompression = TagCompression(UnaryOperator.identity(), UnaryOperator.identity())

        /**
         * Tag compression that applies GZIP compression to the data.
         */
        @JvmField
        public val GZIP: TagCompression = TagCompression(::GZIPInputStream, ::GZIPOutputStream)

        /**
         * Tag compression that applies ZLIB compression to the data.
         */
        @JvmField
        public val ZLIB: TagCompression = TagCompression(::InflaterInputStream, ::DeflaterOutputStream)
    }
}
