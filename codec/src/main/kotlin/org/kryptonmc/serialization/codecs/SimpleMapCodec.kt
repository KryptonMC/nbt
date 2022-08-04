/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.serialization.codecs

import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.MapCodec
import java.util.Objects

public class SimpleMapCodec<K, V>(override val keyCodec: Codec<K>, override val valueCodec: Codec<V>) : BaseMapCodec<K, V>, MapCodec<Map<K, V>> {

    override fun decode(input: CompoundTag): Map<K, V> = super.decode(input)

    override fun encode(value: Map<K, V>, prefix: CompoundTag.Builder): CompoundTag.Builder = super.encode(value, prefix)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        return Objects.equals(keyCodec, (other as SimpleMapCodec<*, *>).keyCodec) && Objects.equals(valueCodec, other.valueCodec)
    }

    override fun hashCode(): Int = Objects.hash(keyCodec, valueCodec)

    override fun toString(): String = "SimpleMapCodec[$keyCodec -> $valueCodec]"
}