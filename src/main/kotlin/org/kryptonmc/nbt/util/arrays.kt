/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.util

import java.lang.reflect.Array

@JvmSynthetic
internal fun ByteArray.add(index: Int, value: Byte) = add(index, value, java.lang.Byte.TYPE) as ByteArray

@JvmSynthetic
internal fun IntArray.add(index: Int, value: Int) = add(index, value, Integer.TYPE) as IntArray

@JvmSynthetic
internal fun LongArray.add(index: Int, value: Long) = add(index, value, java.lang.Long.TYPE) as LongArray

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

@JvmSynthetic
internal fun ByteArray.remove(index: Int) = remove(index, java.lang.Byte.TYPE) as ByteArray

@JvmSynthetic
internal fun IntArray.remove(index: Int) = remove(index, Integer.TYPE) as IntArray

@JvmSynthetic
internal fun LongArray.remove(index: Int) = remove(index, java.lang.Long.TYPE) as LongArray

private fun Any?.remove(index: Int, type: Class<*>): Any {
    if (this == null) throw ArrayIndexOutOfBoundsException("Index $index out of bounds for length 0!")
    val length = Array.getLength(this)
    if (index < 0 || index >= length) throw ArrayIndexOutOfBoundsException("Index $index out of bounds for length $length!")
    val result = Array.newInstance(type, length - 1)
    System.arraycopy(this, 0, result, 0, index)
    if (index < length - 1) System.arraycopy(this, index + 1, result, index, length - index - 1)
    return result
}
