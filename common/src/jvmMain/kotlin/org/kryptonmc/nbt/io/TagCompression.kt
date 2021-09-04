package org.kryptonmc.nbt.io

import okio.BufferedSink
import okio.BufferedSource
import okio.buffer
import okio.deflate
import okio.gzip
import okio.inflate

public actual class TagCompression actual constructor(
    private val decompressor: (BufferedSource) -> BufferedSource,
    private val compressor: (BufferedSink) -> BufferedSink
) {

    public actual fun decompress(input: BufferedSource): BufferedSource = decompressor(input)

    public actual fun compress(output: BufferedSink): BufferedSink = compressor(output)

    public actual companion object {

        public actual val NONE: TagCompression = TagCompression({ it }, { it })
        public actual val GZIP: TagCompression = TagCompression({ it.gzip().buffer() }, { it.gzip().buffer() })
        public actual val ZLIB: TagCompression = TagCompression({ it.inflate().buffer() }, { it.deflate().buffer() })
    }
}
