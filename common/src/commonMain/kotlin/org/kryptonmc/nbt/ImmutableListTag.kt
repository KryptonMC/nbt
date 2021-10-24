package org.kryptonmc.nbt

public class ImmutableListTag(data: List<Tag> = emptyList(), elementType: Int = 0) : ListTag(data, elementType) {

    override val size: Int
        get() = data.size

    override fun get(index: Int): Tag = data[index]

    override fun copy(): ListTag = this // Immutable, no need to copy
}
