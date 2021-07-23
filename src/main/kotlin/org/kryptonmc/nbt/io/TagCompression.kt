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

enum class TagCompression(
    private val decompressor: (InputStream) -> InputStream,
    private val compressor: (OutputStream) -> OutputStream
) {

    NONE({ it }, { it }),
    GZIP(::GZIPInputStream, ::GZIPOutputStream),
    ZLIB(::InflaterInputStream, ::DeflaterOutputStream);

    fun decompress(input: InputStream) = decompressor(input)

    fun compress(output: OutputStream) = compressor(output)
}
