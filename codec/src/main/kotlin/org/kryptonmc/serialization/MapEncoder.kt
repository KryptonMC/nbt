/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.serialization

import org.kryptonmc.nbt.CompoundTag
import java.util.function.Function

public fun interface MapEncoder<T> {

    public fun encode(value: T, builder: CompoundTag.Builder): CompoundTag.Builder

    public fun <U> comap(mapper: Function<U, T>): MapEncoder<U> = MapEncoder { value, builder -> encode(mapper.apply(value), builder) }

    public fun encoder(): Encoder<T> = Encoder { encode(it, CompoundTag.immutableBuilder()).build() }
}
