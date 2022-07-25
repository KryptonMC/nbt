package org.kryptonmc.nbt.io

import okio.BufferedSink
import okio.BufferedSource
import okio.utf8Size
import org.kryptonmc.nbt.EndTag
import org.kryptonmc.nbt.Tag
import kotlin.jvm.JvmSynthetic

@JvmSynthetic
internal fun BufferedSink.writeNamedTag(name: String, value: Tag) {
    writeByte(value.id)
    if (value.id == EndTag.ID) return
    writeShort(name.utf8Size().toInt())
    writeUtf8(name)
    value.write(this)
}

@JvmSynthetic
internal fun BufferedSource.readNamedTag(depth: Int): Pair<String, Tag> {
    val type = readByte().toInt()
    if (type == 0) return Pair("", EndTag)
    val nameLength = readShort()
    val name = readUtf8(nameLength.toLong())
    return Pair(name, Types.reader(type).read(this, depth))
}

@JvmSynthetic
internal fun BufferedSource.readUnnamedTag(depth: Int): Tag {
    val type = readByte().toInt()
    if (type == 0) return EndTag
    val nameLength = readShort()
    readUtf8(nameLength.toLong())
    return Types.reader(type).read(this, depth)
}
