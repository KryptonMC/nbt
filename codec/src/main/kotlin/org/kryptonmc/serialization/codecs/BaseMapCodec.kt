/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.serialization.codecs

import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.Tag
import org.kryptonmc.serialization.Codec
import org.kryptonmc.util.Pair

public interface BaseMapCodec<K, V> {

    public val keyCodec: Codec<K>

    public val valueCodec: Codec<V>

    public fun decode(input: CompoundTag): Map<K, V> {
        val read = persistentMapOf<K, V>().builder()
        val failed = persistentListOf<Pair<Tag, Tag>>().builder()
        input.data.forEach {
            val keyTag = StringTag.of(it.key)
            try {
                val key = keyCodec.decode(keyTag)
                val value = valueCodec.decode(it.value)
                read[key] = value
            } catch (_: Exception) {
                failed.add(Pair.of(keyTag, it.value))
            }
        }
        val errors = failed.build()
        if (errors.isNotEmpty()) error("Failed to parse map! Missed input: $errors")
        return read.build()
    }

    public fun encode(value: Map<K, V>, builder: CompoundTag.Builder): CompoundTag.Builder = builder.apply {
        value.forEach {
            val key = keyCodec.encode(it.key)
            check(key is StringTag) { "Encoded key must be a string!" }
            put(key.value, valueCodec.encode(it.value))
        }
    }
}