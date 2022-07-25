package org.kryptonmc.nbt.io

import okio.BufferedSink
import okio.BufferedSource
import okio.IOException
import okio.buffer
import okio.sink
import okio.source
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.Tag
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Path

public actual object TagIO {

    @JvmStatic
    @Throws(IOException::class)
    public actual fun read(input: BufferedSource): CompoundTag = input.use { it.readUnnamedTag(0) } as? CompoundTag
        ?: throw IOException("Root tag must be an unnamed compound!")

    @JvmStatic
    @Throws(IOException::class)
    public actual fun read(input: BufferedSource, compression: TagCompression): CompoundTag = read(compression.decompress(input))

    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun read(input: InputStream, compression: TagCompression = TagCompression.NONE): CompoundTag = read(input.source().buffer(), compression)

    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun read(path: Path, compression: TagCompression = TagCompression.NONE): CompoundTag = read(path.source().buffer(), compression)

    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun read(file: File, compression: TagCompression = TagCompression.NONE): CompoundTag = read(file.source().buffer(), compression)

    @JvmStatic
    @Throws(IOException::class)
    public actual fun readNamed(input: BufferedSource): Pair<String, Tag> = input.use { it.readNamedTag(0) }

    @JvmStatic
    @Throws(IOException::class)
    public actual fun readNamed(input: BufferedSource, compression: TagCompression): Pair<String, Tag> = readNamed(compression.decompress(input))

    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun readNamed(input: InputStream, compression: TagCompression = TagCompression.NONE): CompoundTag =
        read(input.source().buffer(), compression)

    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun readNamed(path: Path, compression: TagCompression = TagCompression.NONE): Pair<String, Tag> =
        readNamed(path.source().buffer(), compression)

    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun readNamed(file: File, compression: TagCompression = TagCompression.NONE): Pair<String, Tag> =
        readNamed(file.source().buffer(), compression)

    @JvmStatic
    @Throws(IOException::class)
    public actual fun write(output: BufferedSink, value: CompoundTag) {
        output.use { it.writeNamedTag("", value) }
    }

    @JvmStatic
    @Throws(IOException::class)
    public actual fun write(output: BufferedSink, value: CompoundTag, compression: TagCompression) {
        write(compression.compress(output), value)
    }

    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun write(output: OutputStream, value: CompoundTag, compression: TagCompression = TagCompression.NONE) {
        write(output.sink().buffer(), value, compression)
    }

    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun write(path: Path, value: CompoundTag, compression: TagCompression = TagCompression.NONE) {
        write(path.sink().buffer(), value, compression)
    }

    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun write(file: File, value: CompoundTag, compression: TagCompression = TagCompression.NONE) {
        write(file.sink().buffer(), value, compression)
    }

    @JvmStatic
    @Throws(IOException::class)
    public actual fun writeNamed(output: BufferedSink, name: String, value: CompoundTag) {
        output.use { it.writeNamedTag(name, value) }
    }

    @JvmStatic
    @Throws(IOException::class)
    public actual fun writeNamed(output: BufferedSink, name: String, value: CompoundTag, compression: TagCompression) {
        writeNamed(compression.compress(output), name, value)
    }

    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun writeNamed(output: OutputStream, name: String, value: CompoundTag, compression: TagCompression = TagCompression.NONE) {
        writeNamed(output.sink().buffer(), name, value, compression)
    }

    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun writeNamed(path: Path, name: String, value: CompoundTag, compression: TagCompression = TagCompression.NONE) {
        writeNamed(path.sink().buffer(), name, value, compression)
    }

    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun writeNamed(file: File, name: String, value: CompoundTag, compression: TagCompression = TagCompression.NONE) {
        writeNamed(file.sink().buffer(), name, value, compression)
    }
}
