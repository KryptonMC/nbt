package org.kryptonmc.nbt.util

import kotlin.jvm.JvmSynthetic

@JvmSynthetic
internal fun Float.floor(): Int {
    val value = toInt()
    return if (this < value) value - 1 else value
}

@JvmSynthetic
internal fun Double.floor(): Int {
    val value = toInt()
    return if (this < value) value - 1 else value
}
