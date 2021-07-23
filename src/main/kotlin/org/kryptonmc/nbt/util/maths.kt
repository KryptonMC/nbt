package org.kryptonmc.nbt.util

fun Float.floor(): Int {
    val value = toInt()
    return if (this < value) value - 1 else value
}

fun Double.floor(): Int {
    val value = toInt()
    return if (this < value) value - 1 else value
}
