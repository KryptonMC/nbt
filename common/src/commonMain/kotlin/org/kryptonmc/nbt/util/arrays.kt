package org.kryptonmc.nbt.util

import okio.ArrayIndexOutOfBoundsException
import kotlin.jvm.JvmSynthetic

/**
 * Based on ArrayUtils `add` from Commons Lang 3:
 * https://github.com/apache/commons-lang/blob/11d40f5799b355dcec2b156f6c7a669abdde5de3/src/main/java/org/apache/commons/lang3/ArrayUtils.java#L646
 */
@JvmSynthetic
internal fun ByteArray.add(index: Int, value: Byte): ByteArray {
    if (index > size || index < 0) throw ArrayIndexOutOfBoundsException("Index $index out of bounds for length $size!")
    val result = ByteArray(size + 1)
    copyInto(result, 0, 0, index)
    result[index] = value
    if (index < size) copyInto(result, index + 1, index, size)
    return result
}

/**
 * Based on ArrayUtils `add` from Commons Lang 3:
 * https://github.com/apache/commons-lang/blob/11d40f5799b355dcec2b156f6c7a669abdde5de3/src/main/java/org/apache/commons/lang3/ArrayUtils.java#L646
 */
@JvmSynthetic
internal fun IntArray.add(index: Int, value: Int): IntArray {
    if (index > size || index < 0) throw ArrayIndexOutOfBoundsException("Index $index out of bounds for length $size!")
    val result = IntArray(size + 1)
    copyInto(result, 0, 0, index)
    result[index] = value
    if (index < size) copyInto(result, index + 1, index, size)
    return result
}

/**
 * Based on ArrayUtils `add` from Commons Lang 3:
 * https://github.com/apache/commons-lang/blob/11d40f5799b355dcec2b156f6c7a669abdde5de3/src/main/java/org/apache/commons/lang3/ArrayUtils.java#L646
 */
@JvmSynthetic
internal fun LongArray.add(index: Int, value: Long): LongArray {
    if (index > size || index < 0) throw ArrayIndexOutOfBoundsException("Index $index out of bounds for length $size!")
    val result = LongArray(size + 1)
    copyInto(result, 0, 0, index)
    result[index] = value
    if (index < size) copyInto(result, index + 1, index, size)
    return result
}

/**
 * Based on ArrayUtils `remove` from Commons Lang 3:
 * https://github.com/apache/commons-lang/blob/11d40f5799b355dcec2b156f6c7a669abdde5de3/src/main/java/org/apache/commons/lang3/ArrayUtils.java#L4997
 */
@JvmSynthetic
internal fun ByteArray.remove(index: Int): ByteArray {
    if (index < 0 || index >= size) throw ArrayIndexOutOfBoundsException("Index $index out of bounds for length $size!")
    val result = ByteArray(size - 1)
    copyInto(result, 0, 0, index)
    if (index < size - 1) copyInto(result, index, index + 1, size - 1)
    return result
}

/**
 * Based on ArrayUtils `remove` from Commons Lang 3:
 * https://github.com/apache/commons-lang/blob/11d40f5799b355dcec2b156f6c7a669abdde5de3/src/main/java/org/apache/commons/lang3/ArrayUtils.java#L4997
 */
@JvmSynthetic
internal fun IntArray.remove(index: Int): IntArray {
    if (index < 0 || index >= size) throw ArrayIndexOutOfBoundsException("Index $index out of bounds for length $size!")
    val result = IntArray(size - 1)
    copyInto(result, 0, 0, index)
    if (index < size - 1) copyInto(result, index, index + 1, size - 1)
    return result
}

/**
 * Based on ArrayUtils `remove` from Commons Lang 3:
 * https://github.com/apache/commons-lang/blob/11d40f5799b355dcec2b156f6c7a669abdde5de3/src/main/java/org/apache/commons/lang3/ArrayUtils.java#L4997
 */
@JvmSynthetic
internal fun LongArray.remove(index: Int): LongArray {
    if (index < 0 || index >= size) throw ArrayIndexOutOfBoundsException("Index $index out of bounds for length $size!")
    val result = LongArray(size - 1)
    copyInto(result, 0, 0, index)
    if (index < size - 1) copyInto(result, index, index + 1, size - 1)
    return result
}
