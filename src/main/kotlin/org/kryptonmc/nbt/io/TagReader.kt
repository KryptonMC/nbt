package org.kryptonmc.nbt.io

import org.kryptonmc.nbt.Tag
import java.io.DataInput
import java.io.DataInputStream
import java.io.InputStream

interface TagReader<T : Tag<T>> {

    fun read(input: DataInput): T

    fun read(input: InputStream): T = read(DataInputStream(input) as DataInput)

    fun read(input: InputStream, compression: TagCompression): T = read(compression.decompress(input))
}
