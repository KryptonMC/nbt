package org.kryptonmc.nbt

abstract class CollectionTag<E : Tag>(val elementType: Int) : AbstractMutableList<E>(), Tag {

    abstract operator fun set(index: Int, tag: Tag): Boolean

    abstract fun add(index: Int, tag: Tag): Boolean
}
