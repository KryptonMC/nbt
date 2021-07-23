package org.kryptonmc.nbt

abstract class CollectionTag<E : Tag>(open val elementType: Int) : AbstractMutableList<E>(), Tag {

    abstract fun setTag(index: Int, tag: Tag): Boolean

    abstract fun addTag(index: Int, tag: Tag): Boolean
}
