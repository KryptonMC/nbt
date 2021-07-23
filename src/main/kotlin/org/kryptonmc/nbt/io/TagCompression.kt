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
