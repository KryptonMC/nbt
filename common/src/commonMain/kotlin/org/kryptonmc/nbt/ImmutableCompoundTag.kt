package org.kryptonmc.nbt

public class ImmutableCompoundTag(tags: Map<String, Tag> = emptyMap()) : CompoundTag(tags) {

    override fun copy(): Tag = this // Immutable, no need to copy
}
