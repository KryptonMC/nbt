package org.kryptonmc.nbt.io

import okio.BufferedSink
import okio.BufferedSource
import kotlin.jvm.JvmField

/**
 * Compression used to compress and decompress input sources and output sinks.
 *
 * @param decompressor the decompressor
 * @param compressor the compressor
 */
public class TagCompression(
    private val decompressor: (BufferedSource) -> BufferedSource,
    private val compressor: (BufferedSink) -> BufferedSink
) {

    /**
     * Applies the decompressor to the given [input] and returns the resulting
     * source.
     *
     * @param input the input
     * @return the decompressed source
     */
    public fun decompress(input: BufferedSource): BufferedSource = decompressor(input)

    /**
     * Applies the compressor to the given [output] and returns the resulting
     * sink.
     *
     * @param output the output
     * @return the compressed source
     */
    public fun compress(output: BufferedSink): BufferedSink = compressor(output)

    public companion object {

        /**
         * Tag compression that passes the data through as-is, without applying
         * any compression or decompression to it.
         */
        @JvmField
        public val NONE: TagCompression = TagCompression({ it }, { it })

        /**
         * Tag compression that applies GZIP compression to the data.
         */
        @JvmField
        public val GZIP: TagCompression = TagCompression({ it.gzip() }, { it.gzip() })

        /**
         * Tag compression that applies ZLIB compression to the data.
         */
        @JvmField
        public val ZLIB: TagCompression = TagCompression({ it.zlib() }, { it.zlib() })
    }
}
