package org.kryptonmc.nbt.util

import okio.ArrayIndexOutOfBoundsException
import java.lang.reflect.Array

internal actual fun ByteArray.add(index: Int, value: Byte): ByteArray = add(index, value, java.lang.Byte.TYPE) as ByteArray

internal actual fun IntArray.add(index: Int, value: Int): IntArray = add(index, value, Integer.TYPE) as IntArray

internal actual fun LongArray.add(index: Int, value: Long): LongArray = add(index, value, java.lang.Long.TYPE) as LongArray

internal actual fun ByteArray.remove(index: Int): ByteArray = remove(index, java.lang.Byte.TYPE) as ByteArray

internal actual fun IntArray.remove(index: Int): IntArray = remove(index, Integer.TYPE) as IntArray

internal actual fun LongArray.remove(index: Int): LongArray = remove(index, java.lang.Long.TYPE) as LongArray

internal actual fun arraycopy(src: Any, srcPos: Int, dest: Any, destPos: Int, length: Int) {
    System.arraycopy(src, srcPos, dest, destPos, length)
}

private fun Any?.add(index: Int, element: Any, type: Class<*>): Any {
    if (this == null) {
        if (index != 0) throw ArrayIndexOutOfBoundsException("Index $index out of bounds for length 0!")
        val joinedArray = Array.newInstance(type, 1)
        Array.set(joinedArray, 0, element)
        return joinedArray
    }
    val length = Array.getLength(this)
    if (index > length || index < 0) throw ArrayIndexOutOfBoundsException("Index $index out of bounds for length $length!")
    val result = Array.newInstance(type, length + 1)
    System.arraycopy(this, 0, result, 0, index)
    Array.set(result, index, element)
    if (index < length) System.arraycopy(this, index, result, index + 1, length - index)
    return result
}

private fun Any?.remove(index: Int, type: Class<*>): Any {
    if (this == null) throw ArrayIndexOutOfBoundsException("Index $index out of bounds for length 0!")
    val length = Array.getLength(this)
    if (index < 0 || index >= length) throw ArrayIndexOutOfBoundsException("Index $index out of bounds for length $length!")
    val result = Array.newInstance(type, length - 1)
    System.arraycopy(this, 0, result, 0, index)
    if (index < length - 1) System.arraycopy(this, index + 1, result, index, length - index - 1)
    return result
}
