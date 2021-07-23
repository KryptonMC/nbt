package org.kryptonmc.nbt

interface TagExaminer<T> {

    fun examine(tag: Tag<*>): T

    fun examineEnd(tag: EndTag)

    fun examineByte(tag: ByteTag)

    fun examineShort(tag: ShortTag)

    fun examineInt(tag: IntTag)

    fun examineLong(tag: LongTag)

    fun examineFloat(tag: FloatTag)

    fun examineDouble(tag: DoubleTag)
}
