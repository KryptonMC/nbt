/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.io

import org.kryptonmc.nbt.EndTag
import org.kryptonmc.nbt.Tag
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.InputStream
import java.io.OutputStream
import kotlin.jvm.JvmSynthetic

@JvmSynthetic
internal fun OutputStream.writeNamedTag(name: String, value: Tag) {
    val output = if (this is DataOutputStream) this else DataOutputStream(this)
    output.writeByte(value.id)
    if (value.id == EndTag.ID) return
    output.writeUTF(name)
    value.write(output)
}

@JvmSynthetic
internal fun InputStream.readNamedTag(depth: Int): Pair<String, Tag> {
    val input = if (this is DataInputStream) this else DataInputStream(this)
    val type = input.read()
    if (type == 0) return Pair("", EndTag)
    val name = input.readUTF()
    return Pair(name, Types.reader(type).read(input, depth))
}

@JvmSynthetic
internal fun InputStream.readUnnamedTag(depth: Int): Tag {
    val input = if (this is DataInputStream) this else DataInputStream(this)
    val type = input.read()
    if (type == 0) return EndTag
    input.readUTF()
    return Types.reader(type).read(input, depth)
}
