package org.kryptonmc.nbt.util

import org.kryptonmc.nbt.IntArrayTag
import org.kryptonmc.nbt.Tag
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
