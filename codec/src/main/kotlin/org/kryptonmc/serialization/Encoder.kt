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
import org.kryptonmc.serialization.codecs.FieldEncoder
import org.kryptonmc.nbt.Tag
import java.util.function.Function

public fun interface Encoder<T> {

    public fun encode(value: T): Tag

    public fun encodeNullable(value: T): Tag? {
        return try {
            encode(value)
        } catch (_: Exception) {
            null
        }
    }

    public fun field(name: String): MapEncoder<T> = FieldEncoder(name, this)

    public fun <U> comap(mapper: Function<U, T>): Encoder<U> = object : Encoder<U> {

        override fun encode(value: U): Tag = this@Encoder.encode(mapper.apply(value))

        override fun toString(): String = "${this@Encoder}[comapped]"
    }

    public companion object {

        @JvmStatic
        public fun <T> empty(): MapEncoder<T> = object : MapEncoder<T> {

            override fun encode(value: T, prefix: CompoundTag.Builder): CompoundTag.Builder = prefix

            override fun toString(): String = "EmptyEncoder"
        }
    }
}
