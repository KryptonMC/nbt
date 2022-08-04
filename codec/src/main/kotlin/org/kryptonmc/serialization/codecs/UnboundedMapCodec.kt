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
import org.kryptonmc.nbt.Tag
import org.kryptonmc.serialization.Codec
import java.util.Objects

public class UnboundedMapCodec<K, V>(override val keyCodec: Codec<K>, override val valueCodec: Codec<V>) : BaseMapCodec<K, V>, Codec<Map<K, V>> {

    override fun decode(tag: Tag): Map<K, V> {
        check(tag is CompoundTag) { "Provided value for unbounded map codec was not a map! Value: $tag" }
        return decode(tag)
    }

    override fun encode(value: Map<K, V>): Tag = encode(value, CompoundTag.immutableBuilder()).build()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        return Objects.equals(keyCodec, (other as UnboundedMapCodec<*, *>).keyCodec) && Objects.equals(valueCodec, other.valueCodec)
    }

    override fun hashCode(): Int = Objects.hash(keyCodec, valueCodec)

    override fun toString(): String = "UnboundedMapCodec[$keyCodec -> $valueCodec]"
}