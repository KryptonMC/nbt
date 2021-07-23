/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.util

import org.kryptonmc.nbt.IntArrayTag
import org.kryptonmc.nbt.Tag
import java.util.UUID

@JvmSynthetic
internal fun Tag.toUUID(): UUID {
    require(type === IntArrayTag.TYPE) { "Expected UUID tag to be of type ${IntArrayTag.TYPE.name}, but instead found tag of type ${type.name}" }
    val array = (this as IntArrayTag).data
    require(array.size == 4) { "Expected UUID array to be of length 4, but instead found array of length ${array.size}" }
    return array.toUUID()
}

@JvmSynthetic
internal fun UUID.toTag() = IntArrayTag(toIntArray())

private fun IntArray.toUUID() = UUID(
    ((this[0].toLong() shl 32) or (this[1].toLong() and 4294967295L)),
    ((this[2].toLong() shl 32) or (this[3].toLong() and 4294967295L))
)

private fun UUID.toIntArray(): IntArray {
    val most = mostSignificantBits
    val least = leastSignificantBits
    return intArrayOf((most shr 32).toInt(), most.toInt(), (least shr 32).toInt(), least.toInt())
}
