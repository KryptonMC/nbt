package org.kryptonmc.nbt.util

internal expect fun ByteArray.add(index: Int, value: Byte): ByteArray

internal expect fun IntArray.add(index: Int, value: Int): IntArray

internal expect fun LongArray.add(index: Int, value: Long): LongArray

internal expect fun ByteArray.remove(index: Int): ByteArray

internal expect fun IntArray.remove(index: Int): IntArray

internal expect fun LongArray.remove(index: Int): LongArray

internal expect fun arraycopy(src: Any, srcPos: Int, dest: Any, destPos: Int, length: Int)
