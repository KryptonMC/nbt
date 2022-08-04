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
import org.kryptonmc.nbt.Tag
import java.util.function.Function

public fun interface MapEncoder<T> {

    public fun encode(value: T, prefix: CompoundTag.Builder): CompoundTag.Builder

    public fun <U> comap(mapper: Function<U, T>): MapEncoder<U> = object : MapEncoder<U> {

        override fun encode(value: U, prefix: CompoundTag.Builder): CompoundTag.Builder = this@MapEncoder.encode(mapper.apply(value), prefix)

        override fun toString(): String = "${this@MapEncoder}[comapped]"
    }

    public fun encoder(): Encoder<T> = object : Encoder<T> {

        override fun encode(value: T): Tag = this@MapEncoder.encode(value, CompoundTag.immutableBuilder()).build()

        override fun toString(): String = this@MapEncoder.toString()
    }
}
