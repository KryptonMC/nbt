/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
@file:Suppress("NOTHING_TO_INLINE")
package org.kryptonmc.nbt

import org.pcollections.TreePVector

@JvmSynthetic
public inline fun CompoundTag.Builder.boolean(name: String, value: Boolean): CompoundTag.Builder = putBoolean(name, value)

@JvmSynthetic
public inline fun CompoundTag.Builder.byte(name: String, value: Byte): CompoundTag.Builder = putByte(name, value)

@JvmSynthetic
public inline fun CompoundTag.Builder.short(name: String, value: Short): CompoundTag.Builder = putShort(name, value)

@JvmSynthetic
public inline fun CompoundTag.Builder.int(name: String, value: Int): CompoundTag.Builder = putInt(name, value)

@JvmSynthetic
public inline fun CompoundTag.Builder.long(name: String, value: Long): CompoundTag.Builder = putLong(name, value)

@JvmSynthetic
public inline fun CompoundTag.Builder.float(name: String, value: Float): CompoundTag.Builder = putFloat(name, value)

@JvmSynthetic
public inline fun CompoundTag.Builder.double(name: String, value: Double): CompoundTag.Builder = putDouble(name, value)

@JvmSynthetic
public inline fun CompoundTag.Builder.string(name: String, value: String): CompoundTag.Builder = putString(name, value)

@JvmSynthetic
public inline fun CompoundTag.Builder.byteArray(name: String, values: ByteArray): CompoundTag.Builder = putByteArray(name, values)

@JvmSynthetic
public inline fun CompoundTag.Builder.intArray(name: String, values: IntArray): CompoundTag.Builder = putIntArray(name, values)

@JvmSynthetic
public inline fun CompoundTag.Builder.longArray(name: String, values: LongArray): CompoundTag.Builder = putLongArray(name, values)

@JvmSynthetic
public inline fun CompoundTag.Builder.bytes(name: String, vararg values: Byte): CompoundTag.Builder = putByteArray(name, values)

@JvmSynthetic
public inline fun CompoundTag.Builder.ints(name: String, vararg values: Int): CompoundTag.Builder = putIntArray(name, values)

@JvmSynthetic
public inline fun CompoundTag.Builder.longs(name: String, vararg values: Long): CompoundTag.Builder = putLongArray(name, values)

@JvmSynthetic
public inline fun CompoundTag.Builder.list(name: String, builder: ListTag.Builder.() -> Unit): CompoundTag.Builder =
    put(name, ImmutableListTag.builder().apply(builder).build())

@JvmSynthetic
public inline fun CompoundTag.Builder.list(name: String, elementType: Int, vararg elements: Tag): CompoundTag.Builder =
    list(name, elementType, elements.asList())

@JvmSynthetic
public inline fun CompoundTag.Builder.list(name: String, elementType: Int, elements: Collection<Tag>): CompoundTag.Builder =
    put(name, ImmutableListTag.of(TreePVector.from(elements), elementType))

@JvmSynthetic
public inline fun CompoundTag.Builder.compound(name: String, builder: CompoundTag.Builder.() -> Unit): CompoundTag.Builder =
    put(name, ImmutableCompoundTag.builder().apply(builder).build())

@JvmSynthetic
public inline fun CompoundTag.update(name: String, function: (CompoundTag) -> CompoundTag): CompoundTag = put(name, function(getCompound(name)))

@JvmSynthetic
public inline fun CompoundTag.update(name: String, elementType: Int, function: (ListTag) -> ListTag): CompoundTag =
    put(name, function(getList(name, elementType)))
