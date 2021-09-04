package org.kryptonmc.nbt.io

import okio.BufferedSink
import okio.BufferedSource

public class TagCompression(
    private val decompressor: (BufferedSource) -> BufferedSource,
    private val compressor: (BufferedSink) -> BufferedSink
) {

    public fun decompress(input: BufferedSource): BufferedSource = decompressor(input)

    public fun compress(output: BufferedSink): BufferedSink = compressor(output)

    public companion object {

        public val NONE: TagCompression = TagCompression({ it }, { it })
        public val GZIP: TagCompression = TagCompression({ it.gzip() }, { it.gzip() })
        public val ZLIB: TagCompression = TagCompression({ it.zlib() }, { it.zlib() })
    }
}
