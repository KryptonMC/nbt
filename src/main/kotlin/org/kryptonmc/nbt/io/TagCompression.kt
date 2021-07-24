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
import java.util.zip.DeflaterOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import java.util.zip.InflaterInputStream

public enum class TagCompression(
    private val decompressor: (InputStream) -> InputStream,
    private val compressor: (OutputStream) -> OutputStream
) {

    NONE({ it }, { it }),
    GZIP(::GZIPInputStream, ::GZIPOutputStream),
    ZLIB(::InflaterInputStream, ::DeflaterOutputStream);

    public fun decompress(input: InputStream): InputStream = decompressor(input)

    public fun compress(output: OutputStream): OutputStream = compressor(output)
}
