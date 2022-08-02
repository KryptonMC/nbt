/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
@file:JvmSynthetic
package org.kryptonmc.nbt

import kotlin.jvm.JvmSynthetic

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
@DslMarker
internal annotation class NBTDsl

/**
 * Creates a new compound tag from the given [builder].
 *
 * @param builder the builder
 * @return a new compound tag
 */
@NBTDsl
@JvmSynthetic
public inline fun compound(builder: CompoundTag.Builder.() -> Unit): CompoundTag = CompoundTag.immutableBuilder().apply(builder).build()

/**
 * Creates a new compound tag builder and applies the given [builder] to it.
 *
 * @param builder the builder
 * @return a new compound tag builder
 */
@NBTDsl
@JvmSynthetic
public inline fun buildCompound(builder: CompoundTag.Builder.() -> Unit): CompoundTag.Builder = CompoundTag.immutableBuilder().apply(builder)

/**
 * Creates a new mutable compound tag and applies the given [builder] to it.
 *
 * @param builder the builder
 * @return a new mutable compound tag
 */
@NBTDsl
@JvmSynthetic
public inline fun mutableCompound(builder: MutableCompoundTag.() -> Unit): MutableCompoundTag = MutableCompoundTag().apply(builder)

/**
 * Creates a new list tag from the given [builder].
 *
 * @param builder the builder
 * @return a new list tag
 */
@NBTDsl
@JvmSynthetic
public inline fun list(builder: ListTag.Builder.() -> Unit): ListTag = ListTag.immutableBuilder().apply(builder).build()

/**
 * Creates a new list tag builder and applies the given [builder] to it.
 *
 * @param builder the builder
 * @return a new list tag builder
 */
@NBTDsl
@JvmSynthetic
public inline fun buildList(builder: ListTag.Builder.() -> Unit): ListTag.Builder = ListTag.immutableBuilder().apply(builder)

/**
 * Creates a new mutable list tag and applies the given [builder] to it.
 *
 * @param builder the builder
 * @return a new mutable list tag
 */
@NBTDsl
@JvmSynthetic
public inline fun mutableList(builder: MutableListTag.() -> Unit): MutableListTag = MutableListTag().apply(builder)
