package org.kryptonmc.nbt.util

import okio.ArrayIndexOutOfBoundsException
import kotlin.jvm.JvmSynthetic

@JvmSynthetic
internal fun ByteArray.add(index: Int, value: Byte): ByteArray {
    if (index > size || index < 0) throw ArrayIndexOutOfBoundsException("Index $index out of bounds for length $size!")
    val result = ByteArray(size + 1)
    copyInto(result, 0, 0, index)
    result[index] = value
    if (index < size) copyInto(result, index + 1, index, size)
    return result
}

@JvmSynthetic
internal fun IntArray.add(index: Int, value: Int): IntArray {
    if (index > size || index < 0) throw ArrayIndexOutOfBoundsException("Index $index out of bounds for length $size!")
    val result = IntArray(size + 1)
    copyInto(result, 0, 0, index)
    result[index] = value
    if (index < size) copyInto(result, index + 1, index, size)
    return result
}

@JvmSynthetic
internal fun LongArray.add(index: Int, value: Long): LongArray {
    if (index > size || index < 0) throw ArrayIndexOutOfBoundsException("Index $index out of bounds for length $size!")
    val result = LongArray(size + 1)
    copyInto(result, 0, 0, index)
    result[index] = value
    if (index < size) copyInto(result, index + 1, index, size)
    return result
}

@JvmSynthetic
internal fun ByteArray.remove(index: Int): ByteArray {
    if (index < 0 || index >= size) throw ArrayIndexOutOfBoundsException("Index $index out of bounds for length $size!")
    val result = ByteArray(size - 1)
    copyInto(result, 0, 0, index)
    if (index < size - 1) copyInto(result, index, index + 1, size - 1)
    return result
}

@JvmSynthetic
internal fun IntArray.remove(index: Int): IntArray {
    if (index < 0 || index >= size) throw ArrayIndexOutOfBoundsException("Index $index out of bounds for length $size!")
    val result = IntArray(size - 1)
    copyInto(result, 0, 0, index)
    if (index < size - 1) copyInto(result, index, index + 1, size - 1)
    return result
}

@JvmSynthetic
internal fun LongArray.remove(index: Int): LongArray {
    if (index < 0 || index >= size) throw ArrayIndexOutOfBoundsException("Index $index out of bounds for length $size!")
    val result = LongArray(size - 1)
    copyInto(result, 0, 0, index)
    if (index < size - 1) copyInto(result, index, index + 1, size - 1)
    return result
}
