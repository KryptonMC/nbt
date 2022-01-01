package org.kryptonmc.nbt.io

import okio.Buffer
import okio.BufferedSink
import okio.BufferedSource
import okio.buffer
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.khronos.webgl.set

internal actual fun BufferedSource.gzip(): BufferedSource = ZLIBSource(buffer())

internal actual fun BufferedSource.zlib(): BufferedSource = ZLIBSource(buffer())

internal actual fun BufferedSink.gzip(): BufferedSink = ZLIBSink(buffer(), true, -1)

internal actual fun BufferedSink.zlib(): BufferedSink = ZLIBSink(buffer(), false, -1)

private class ZLIBSource(private val source: BufferedSource) : BufferedSource by source {

    private val inBuffer = Uint8Array(1)
    private val outBuffer = Uint8ArrayBuffer()
    private var inflationFinished = false

    private val inflate = Inflate().apply {
        onData = { outBuffer.reset(it) }
        onEnd = { inflationFinished = true }
    }

    override fun read(sink: Buffer, byteCount: Long): Long {
        while (!inflationFinished && outBuffer.exhausted()) {
            inBuffer[0] = source.readByte()
            inflate.push(inBuffer, ZFlushMode.SYNC_FLUSH)
        }
        return outBuffer.read(sink, byteCount)
    }

    private class Uint8ArrayBuffer {

        private lateinit var buffer: Uint8Array
        private var position = 0
        private var exhausted = true

        fun exhausted(): Boolean = exhausted

        fun read(sink: Buffer, byteCount: Long): Long {
            if (exhausted) return -1
            val remaining = buffer.byteLength - position
            val readBytes = minOf(remaining.toLong(), byteCount)
            repeat(readBytes.toInt()) { sink.writeByte(buffer[position++].toInt()) }
            if (position == buffer.length) exhausted = true
            return readBytes
        }

        fun reset(buffer: Uint8Array) {
            check(exhausted) { "Attempted to reset non-exhausted buffer!" }
            this.buffer = buffer
            position = 0
            exhausted = buffer.byteLength == 0
        }
    }
}

private class ZLIBSink(private val sink: BufferedSink, gzip: Boolean, level: Int) : BufferedSink by sink {

    private val inBuffer = Uint8Array(1)
    private val deflate = Deflate(ZLevel(level), calculateWindowBits(gzip)).apply {
        onData = {
            for (i in 0 until it.byteLength) {
                sink.writeByte(it[i].toInt())
            }
        }
    }

    override fun write(source: Buffer, byteCount: Long) {
        for (i in 0 until byteCount) {
            inBuffer[0] = source.readByte()
            deflate.push(inBuffer)
        }
    }

    override fun close() {
        deflate.push(Uint8Array(0), ZFlushMode.FINISH)
        sink.close()
    }

    companion object {

        private fun calculateWindowBits(gzip: Boolean): Int {
            var base = 15
            if (gzip) base += 16
            return base
        }
    }
}
