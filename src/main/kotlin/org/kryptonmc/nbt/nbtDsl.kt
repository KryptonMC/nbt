/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

@DslMarker
internal annotation class NBTDsl

@NBTDsl
public inline fun compound(builder: CompoundTag.Builder.() -> Unit): CompoundTag = CompoundTag.builder().apply(builder).build()

@NBTDsl
public inline fun buildCompound(builder: CompoundTag.Builder.() -> Unit): CompoundTag.Builder = CompoundTag.builder().apply(builder)

@NBTDsl
public inline fun mutableCompound(builder: MutableCompoundTag.() -> Unit): MutableCompoundTag = MutableCompoundTag().apply(builder)

@NBTDsl
public inline fun list(builder: ListTag.Builder.() -> Unit): ListTag = ListTag.builder().apply(builder).build()

@NBTDsl
public inline fun buildList(builder: ListTag.Builder.() -> Unit): ListTag.Builder = ListTag.builder().apply(builder)

@NBTDsl
public inline fun mutableList(builder: MutableListTag.() -> Unit): MutableListTag = MutableListTag().apply(builder)
