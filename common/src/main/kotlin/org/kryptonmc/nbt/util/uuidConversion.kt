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
import kotlin.jvm.JvmSynthetic

private const val UNSIGNED_INT_MAX = 0xFFFFFFFF

@JvmSynthetic
internal fun Tag.toUUID(): UUID? {
    if (id != IntArrayTag.ID) return null
    val array = (this as IntArrayTag).data
    if (array.size != 4) return null
    return array.toUUID()
}

@JvmSynthetic
internal fun UUID.toTag(): IntArrayTag = IntArrayTag(toIntArray())

private fun IntArray.toUUID(): UUID = UUID(
    (this[0].toLong() shl Int.SIZE_BITS) or (this[1].toLong() and UNSIGNED_INT_MAX),
    (this[2].toLong() shl Int.SIZE_BITS) or (this[3].toLong() and UNSIGNED_INT_MAX)
)

private fun UUID.toIntArray(): IntArray {
    val most = mostSignificantBits
    val least = leastSignificantBits
    return intArrayOf((most shr Int.SIZE_BITS).toInt(), most.toInt(), (least shr Int.SIZE_BITS).toInt(), least.toInt())
}
