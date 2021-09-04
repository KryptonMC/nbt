package org.kryptonmc.nbt.io

import okio.BufferedSink
import okio.BufferedSource
import okio.IOException
import okio.use
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.Tag

public actual object TagIO {

    public actual fun read(input: BufferedSource): CompoundTag = input.use { it.readUnnamedTag(0) } as? CompoundTag
        ?: throw IOException("Root tag must be an unnamed compound!")

    public actual fun read(input: BufferedSource, compression: TagCompression): CompoundTag = read(compression.decompress(input))

    public actual fun readNamed(input: BufferedSource): Pair<String, Tag> = input.use { it.readNamedTag(0) }

    public actual fun readNamed(input: BufferedSource, compression: TagCompression): Pair<String, Tag> =
        readNamed(compression.decompress(input))

    public actual fun write(output: BufferedSink, value: CompoundTag) {
        output.use { it.writeNamedTag("", value) }
    }

    public actual fun write(output: BufferedSink, value: CompoundTag, compression: TagCompression) {
        write(compression.compress(output), value)
    }

    public actual fun writeNamed(output: BufferedSink, name: String, value: CompoundTag) {
        output.use { it.writeNamedTag(name, value) }
    }

    public actual fun writeNamed(output: BufferedSink, name: String, value: CompoundTag, compression: TagCompression) {
        writeNamed(compression.compress(output), name, value)
    }
}
