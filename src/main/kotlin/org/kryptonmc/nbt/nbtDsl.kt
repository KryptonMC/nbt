package org.kryptonmc.nbt

@DslMarker
private annotation class NBTDsl

@NBTDsl
inline fun compound(builder: CompoundTag.() -> Unit): CompoundTag = CompoundTag().apply(builder)

@NBTDsl
inline fun CompoundTag.list(name: String, builder: ListTag.() -> Unit): CompoundTag = apply { put(name, ListTag().apply(builder)) }

@NBTDsl
fun CompoundTag.list(name: String, vararg elements: Tag): CompoundTag = apply { put(name, ListTag().apply { addAll(elements) }) }
