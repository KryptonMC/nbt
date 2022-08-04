/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.serialization.codecs

import org.kryptonmc.serialization.Encoder
import org.kryptonmc.serialization.MapEncoder
import org.kryptonmc.nbt.CompoundTag
import java.util.Objects

public class FieldEncoder<T>(private val name: String, private val elementEncoder: Encoder<T>) : MapEncoder<T> {

    override fun encode(value: T, prefix: CompoundTag.Builder): CompoundTag.Builder = prefix.put(name, elementEncoder.encode(value))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        return Objects.equals(name, (other as FieldEncoder<*>).name) && Objects.equals(elementEncoder, other.elementEncoder)
    }

    override fun hashCode(): Int = Objects.hash(name, elementEncoder)

    override fun toString(): String = "FieldEncoder[$name: $elementEncoder]"
}
