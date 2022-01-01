package org.kryptonmc.nbt.util

import org.kryptonmc.nbt.IntArrayTag
import org.kryptonmc.nbt.Tag
import kotlin.jvm.JvmSynthetic

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
    (this[0].toLong() shl 32) or (this[1].toLong() and 4294967295L),
    (this[2].toLong() shl 32) or (this[3].toLong() and 4294967295L)
)

private fun UUID.toIntArray(): IntArray {
    val most = mostSignificantBits
    val least = leastSignificantBits
    return intArrayOf((most shr 32).toInt(), most.toInt(), (least shr 32).toInt(), least.toInt())
}
