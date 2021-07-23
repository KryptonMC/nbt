package org.kryptonmc.nbt

abstract class NumberTag<T : NumberTag<T>> protected constructor(open val value: Number) : Tag<T> {

    open fun toDouble() = value.toDouble()

    open fun toFloat() = value.toFloat()

    open fun toLong() = value.toLong()

    open fun toInt() = value.toInt()

    open fun toShort() = value.toShort()

    open fun toByte() = value.toByte()
}
