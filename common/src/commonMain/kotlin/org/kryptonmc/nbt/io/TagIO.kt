package org.kryptonmc.nbt.io

import okio.BufferedSink
import okio.BufferedSource
import okio.IOException
import okio.use
import okio.utf8Size
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.EndTag
import org.kryptonmc.nbt.Tag
import kotlin.jvm.JvmStatic
import kotlin.jvm.JvmSynthetic

public expect object TagIO {

    @JvmStatic
    @Throws(IOException::class)
    public fun read(input: BufferedSource): CompoundTag

    @JvmStatic
    @Throws(IOException::class)
    public fun read(input: BufferedSource, compression: TagCompression): CompoundTag

    @JvmStatic
    @Throws(IOException::class)
    public fun readNamed(input: BufferedSource): Pair<String, Tag>

    @JvmStatic
    @Throws(IOException::class)
    public fun readNamed(input: BufferedSource, compression: TagCompression): Pair<String, Tag>

    @JvmStatic
    @Throws(IOException::class)
    public fun write(output: BufferedSink, value: CompoundTag)

    @JvmStatic
    @Throws(IOException::class)
    public fun write(output: BufferedSink, value: CompoundTag, compression: TagCompression)

    @JvmStatic
    @Throws(IOException::class)
    public fun writeNamed(output: BufferedSink, name: String, value: CompoundTag)

    @JvmStatic
    @Throws(IOException::class)
    public fun writeNamed(output: BufferedSink, name: String, value: CompoundTag, compression: TagCompression)
}

@JvmSynthetic
internal fun BufferedSink.writeNamedTag(name: String, value: Tag) {
    writeByte(value.id)
    if (value.id != EndTag.ID) {
        writeShort(name.utf8Size().toInt())
        writeUtf8(name)
        value.write(this)
    }
}

@JvmSynthetic
internal fun BufferedSource.readNamedTag(depth: Int): Pair<String, Tag> {
    val type = readByte().toInt()
    if (type == 0) return "" to EndTag
    val nameLength = readShort()
    val name = readUtf8(nameLength.toLong())
    return name to Types.reader(type).read(this, depth)
}

@JvmSynthetic
internal fun BufferedSource.readUnnamedTag(depth: Int): Tag {
    val type = readByte().toInt()
    if (type == 0) return EndTag
    val nameLength = readShort()
    readUtf8(nameLength.toLong())
    return Types.reader(type).read(this, depth)
}
