package org.kryptonmc.nbt.io

import okio.BufferedSink
import okio.BufferedSource

public expect class TagCompression(
    decompressor: (BufferedSource) -> BufferedSource,
    compressor: (BufferedSink) -> BufferedSink
) {

    public fun decompress(input: BufferedSource): BufferedSource

    public fun compress(output: BufferedSink): BufferedSink

    public companion object {

        public val NONE: TagCompression
        public val GZIP: TagCompression
        public val ZLIB: TagCompression
    }
}
