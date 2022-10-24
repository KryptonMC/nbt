/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

@JvmSynthetic
public inline fun CompoundTag.Builder.list(name: String, builder: ListTag.Builder.() -> Unit): CompoundTag.Builder =
    put(name, ImmutableListTag.builder().apply(builder).build())

@JvmSynthetic
public inline fun CompoundTag.Builder.compound(name: String, builder: CompoundTag.Builder.() -> Unit): CompoundTag.Builder =
    put(name, ImmutableCompoundTag.builder().apply(builder).build())
