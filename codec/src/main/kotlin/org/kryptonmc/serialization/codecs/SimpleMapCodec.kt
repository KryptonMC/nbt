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

public class SimpleMapCodec<K, V>(override val keyCodec: Codec<K>, override val valueCodec: Codec<V>) : MapCodec<Map<K, V>>, BaseMapCodec<K, V> {

    override fun decode(input: CompoundTag): Map<K, V> = super.decode(input)

    override fun encode(value: Map<K, V>, builder: CompoundTag.Builder): CompoundTag.Builder = super.encode(value, builder)
}