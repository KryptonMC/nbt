/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.io

import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.EndTag
import org.kryptonmc.nbt.Tag
import org.kryptonmc.nbt.toTagReader
import java.io.DataInput
import java.io.DataInputStream
import java.io.DataOutput
import java.io.DataOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Path
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream

object TagIO {

    @JvmStatic
    fun read(input: DataInput) = input.readUnnamedTag(0) as? CompoundTag ?: throw IOException("Root tag must be a named compound!")

    @JvmStatic
    @JvmOverloads
    fun read(input: InputStream, compression: TagCompression = TagCompression.NONE) = DataInputStream(compression.decompress(input)).use {
        read(it as DataInput)
    }

    @JvmStatic
    @JvmOverloads
    fun read(path: Path, compression: TagCompression = TagCompression.NONE) = read(path.inputStream(), compression)

    @JvmStatic
    @JvmOverloads
    fun read(file: File, compression: TagCompression = TagCompression.NONE) = read(file.inputStream(), compression)

    @JvmStatic
    fun readNamed(input: DataInput) = input.readNamedTag(0)

    @JvmStatic
    @JvmOverloads
    fun readNamed(input: InputStream, compression: TagCompression = TagCompression.NONE) = DataInputStream(compression.decompress(input)).use {
        readNamed(it as DataInput)
    }

    @JvmStatic
    @JvmOverloads
    fun readNamed(path: Path, compression: TagCompression = TagCompression.NONE) = readNamed(path.inputStream(), compression)

    @JvmStatic
    @JvmOverloads
    fun readNamed(file: File, compression: TagCompression = TagCompression.NONE) = readNamed(file.inputStream(), compression)

    @JvmStatic
    fun write(output: DataOutput, tag: CompoundTag) = output.writeNamedTag("", tag)

    @JvmStatic
    @JvmOverloads
    fun write(output: OutputStream, tag: CompoundTag, compression: TagCompression = TagCompression.NONE) =
        DataOutputStream(compression.compress(output)).use { write(it as DataOutput, tag) }

    @JvmStatic
    @JvmOverloads
    fun write(path: Path, tag: CompoundTag, compression: TagCompression = TagCompression.NONE) = path.outputStream().use {
        write(it, tag, compression)
    }

    @JvmStatic
    @JvmOverloads
    fun write(file: File, tag: CompoundTag, compression: TagCompression = TagCompression.NONE) = file.outputStream().use {
        write(it, tag, compression)
    }

    @JvmStatic
    fun write(output: DataOutput, name: String, tag: CompoundTag) = output.writeNamedTag(name, tag)

    @JvmStatic
    @JvmOverloads
    fun write(output: OutputStream, name: String, tag: CompoundTag, compression: TagCompression = TagCompression.NONE) =
        DataOutputStream(compression.compress(output)).use { write(it as DataOutput, name, tag) }

    @JvmStatic
    @JvmOverloads
    fun write(path: Path, name: String, tag: CompoundTag, compression: TagCompression = TagCompression.NONE) = path.outputStream().use {
        write(it, name, tag, compression)
    }

    @JvmStatic
    @JvmOverloads
    fun write(file: File, name: String, tag: CompoundTag, compression: TagCompression = TagCompression.NONE) = file.outputStream().use {
        write(it, name, tag, compression)
    }
}

private fun DataOutput.writeNamedTag(name: String, tag: Tag) {
    writeByte(tag.id)
    if (tag.id != EndTag.ID) {
        writeUTF(name)
        tag.writer.write(this, tag)
    }
}

private fun DataInput.readNamedTag(depth: Int): Pair<String, Tag> {
    val type = readByte().toInt()
    if (type == 0) return "" to EndTag
    val name = readUTF()
    return name to type.toTagReader().read(this, depth)
}

private fun DataInput.readUnnamedTag(depth: Int): Tag {
    val type = readByte().toInt()
    if (type == 0) return EndTag
    readUTF()
    return type.toTagReader().read(this, depth)
}
