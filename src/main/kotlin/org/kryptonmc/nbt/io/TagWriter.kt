package org.kryptonmc.nbt.io

import org.kryptonmc.nbt.Tag
import java.io.ByteArrayOutputStream
import java.io.DataOutput
import java.io.DataOutputStream
import java.io.OutputStream

interface TagWriter<T : Tag<T>> {

    fun write(output: DataOutput, tag: T)

    fun write(output: OutputStream, tag: T) = write(DataOutputStream(output) as DataOutput, tag)

    fun write(output: OutputStream, tag: T, compression: TagCompression) = write(compression.compress(output), tag)

    fun write(tag: T): OutputStream = ByteArrayOutputStream().apply { write(this, tag) }
}
