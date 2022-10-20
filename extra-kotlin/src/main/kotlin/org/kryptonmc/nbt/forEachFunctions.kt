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
public inline fun ByteArrayTag.forEachByte(action: (Byte) -> Unit) {
    data.forEach(action)
}

@JvmSynthetic
public inline fun IntArrayTag.forEachInt(action: (Int) -> Unit) {
    data.forEach(action)
}

@JvmSynthetic
public inline fun LongArrayTag.forEachLong(action: (Long) -> Unit) {
    data.forEach(action)
}

@JvmSynthetic
public inline fun CompoundTag.forEachByte(action: (String, Byte) -> Unit) {
    forEachTypedTag(action, ByteTag::value)
}

@JvmSynthetic
public inline fun CompoundTag.forEachShort(action: (String, Short) -> Unit) {
    forEachTypedTag(action, ShortTag::value)
}

@JvmSynthetic
public inline fun CompoundTag.forEachInt(action: (String, Int) -> Unit) {
    forEachTypedTag(action, IntTag::value)
}

@JvmSynthetic
public inline fun CompoundTag.forEachLong(action: (String, Long) -> Unit) {
    forEachTypedTag(action, LongTag::value)
}

@JvmSynthetic
public inline fun CompoundTag.forEachFloat(action: (String, Float) -> Unit) {
    forEachTypedTag(action, FloatTag::value)
}

@JvmSynthetic
public inline fun CompoundTag.forEachDouble(action: (String, Double) -> Unit) {
    forEachTypedTag(action, DoubleTag::value)
}

@JvmSynthetic
public inline fun CompoundTag.forEachString(action: (String, String) -> Unit) {
    forEachTypedTag(action, StringTag::value)
}

@JvmSynthetic
public inline fun CompoundTag.forEachByteArray(action: (String, ByteArray) -> Unit) {
    forEachTypedTag(action, ByteArrayTag::getData)
}

@JvmSynthetic
public inline fun CompoundTag.forEachIntArray(action: (String, IntArray) -> Unit) {
    forEachTypedTag(action, IntArrayTag::getData)
}

@JvmSynthetic
public inline fun CompoundTag.forEachLongArray(action: (String, LongArray) -> Unit) {
    forEachTypedTag(action, LongArrayTag::getData)
}

@JvmSynthetic
public inline fun CompoundTag.forEachList(action: (String, ListTag) -> Unit) {
    forEachTypedTag<ListTag, ListTag>(action) { it }
}

@JvmSynthetic
public inline fun CompoundTag.forEachCompound(action: (String, CompoundTag) -> Unit) {
    forEachTypedTag<CompoundTag, CompoundTag>(action) { it }
}

@JvmSynthetic
public inline fun ListTag.forEachByte(action: (Byte) -> Unit) {
    forEachTypedTag(action, ByteTag::value)
}

@JvmSynthetic
public inline fun ListTag.forEachShort(action: (Short) -> Unit) {
    forEachTypedTag(action, ShortTag::value)
}

@JvmSynthetic
public inline fun ListTag.forEachInt(action: (Int) -> Unit) {
    forEachTypedTag(action, IntTag::value)
}

@JvmSynthetic
public inline fun ListTag.forEachLong(action: (Long) -> Unit) {
    forEachTypedTag(action, LongTag::value)
}

@JvmSynthetic
public inline fun ListTag.forEachFloat(action: (Float) -> Unit) {
    forEachTypedTag(action, FloatTag::value)
}

@JvmSynthetic
public inline fun ListTag.forEachDouble(action: (Double) -> Unit) {
    forEachTypedTag(action, DoubleTag::value)
}

@JvmSynthetic
public inline fun ListTag.forEachString(action: (String) -> Unit) {
    forEachTypedTag(action, StringTag::value)
}

@JvmSynthetic
public inline fun ListTag.forEachByteArray(action: (ByteArray) -> Unit) {
    forEachTypedTag(action, ByteArrayTag::getData)
}

@JvmSynthetic
public inline fun ListTag.forEachIntArray(action: (IntArray) -> Unit) {
    forEachTypedTag(action, IntArrayTag::getData)
}

@JvmSynthetic
public inline fun ListTag.forEachLongArray(action: (LongArray) -> Unit) {
    forEachTypedTag(action, LongArrayTag::getData)
}

@JvmSynthetic
public inline fun ListTag.forEachList(action: (ListTag) -> Unit) {
    forEachTypedTag<ListTag, ListTag>(action) { it }
}

@JvmSynthetic
public inline fun ListTag.forEachCompound(action: (CompoundTag) -> Unit) {
    forEachTypedTag<CompoundTag, CompoundTag>(action) { it }
}

@JvmSynthetic
@PublishedApi
internal inline fun <reified T : Tag, R> CompoundTag.forEachTypedTag(action: (String, R) -> Unit, converter: (T) -> R) {
    for ((key, value) in data) {
        if (value is T) action(key, converter(value))
    }
}

@JvmSynthetic
@PublishedApi
internal inline fun <reified T : Tag, R> ListTag.forEachTypedTag(action: (R) -> Unit, converter: (T) -> R) {
    for (value in data) {
        if (value is T) action(converter(value))
    }
}
