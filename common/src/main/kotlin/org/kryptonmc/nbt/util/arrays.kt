/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.util

import kotlin.jvm.JvmSynthetic

/**
 * Based on ArrayUtils `add` for byte arrays from Commons Lang 3:
 * https://github.com/apache/commons-lang/blob/11d40f5799b355dcec2b156f6c7a669abdde5de3/src/main/java/org/apache/commons/lang3/ArrayUtils.java#L309
 */
@JvmSynthetic
internal fun ByteArray.add(index: Int, value: Byte): ByteArray {
    if (index > size || index < 0) throw ArrayIndexOutOfBoundsException("Index $index out of bounds for length $size!")
    val result = ByteArray(size + 1)
    System.arraycopy(this, 0, result, 0, index)
    result[index] = value
    if (index < size) System.arraycopy(this, index, result, index + 1, size - index)
    return result
}

/**
 * Based on ArrayUtils `add` for int arrays from Commons Lang 3:
 * https://github.com/apache/commons-lang/blob/11d40f5799b355dcec2b156f6c7a669abdde5de3/src/main/java/org/apache/commons/lang3/ArrayUtils.java#L568
 */
@JvmSynthetic
internal fun IntArray.add(index: Int, value: Int): IntArray {
    if (index > size || index < 0) throw ArrayIndexOutOfBoundsException("Index $index out of bounds for length $size!")
    val result = IntArray(size + 1)
    System.arraycopy(this, 0, result, 0, index)
    result[index] = value
    if (index < size) System.arraycopy(this, index, result, index + 1, size - index)
    return result
}

/**
 * Based on ArrayUtils `add` for long arrays from Commons Lang 3:
 * https://github.com/apache/commons-lang/blob/11d40f5799b355dcec2b156f6c7a669abdde5de3/src/main/java/org/apache/commons/lang3/ArrayUtils.java#L604
 */
@JvmSynthetic
internal fun LongArray.add(index: Int, value: Long): LongArray {
    if (index > size || index < 0) throw ArrayIndexOutOfBoundsException("Index $index out of bounds for length $size!")
    val result = LongArray(size + 1)
    System.arraycopy(this, 0, result, 0, index)
    result[index] = value
    if (index < size) System.arraycopy(this, index, result, index + 1, size - index)
    return result
}

/**
 * Based on ArrayUtils `remove` for byte arrays from Commons Lang 3:
 * https://github.com/apache/commons-lang/blob/11d40f5799b355dcec2b156f6c7a669abdde5de3/src/main/java/org/apache/commons/lang3/ArrayUtils.java#L4806
 */
@JvmSynthetic
internal fun ByteArray.remove(index: Int): ByteArray {
    if (index < 0 || index >= size) throw ArrayIndexOutOfBoundsException("Index $index out of bounds for length $size!")
    val result = ByteArray(size - 1)
    System.arraycopy(this, 0, result, 0, index)
    if (index < size - 1) System.arraycopy(this, index + 1, result, index, size - index - 1)
    return result
}

/**
 * Based on ArrayUtils `remove` for int arrays from Commons Lang 3:
 * https://github.com/apache/commons-lang/blob/11d40f5799b355dcec2b156f6c7a669abdde5de3/src/main/java/org/apache/commons/lang3/ArrayUtils.java#L4938
 */
@JvmSynthetic
internal fun IntArray.remove(index: Int): IntArray {
    if (index < 0 || index >= size) throw ArrayIndexOutOfBoundsException("Index $index out of bounds for length $size!")
    val result = IntArray(size - 1)
    System.arraycopy(this, 0, result, 0, index)
    if (index < size - 1) System.arraycopy(this, index + 1, result, index, size - index - 1)
    return result
}

/**
 * Based on ArrayUtils `remove` for long arrays from Commons Lang 3:
 * https://github.com/apache/commons-lang/blob/11d40f5799b355dcec2b156f6c7a669abdde5de3/src/main/java/org/apache/commons/lang3/ArrayUtils.java#L4970
 */
@JvmSynthetic
internal fun LongArray.remove(index: Int): LongArray {
    if (index < 0 || index >= size) throw ArrayIndexOutOfBoundsException("Index $index out of bounds for length $size!")
    val result = LongArray(size - 1)
    System.arraycopy(this, 0, result, 0, index)
    if (index < size - 1) System.arraycopy(this, index + 1, result, index, size - index - 1)
    return result
}
