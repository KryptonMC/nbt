/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.serialization

import org.kryptonmc.nbt.Tag
import org.kryptonmc.serialization.codecs.FieldDecoder
import java.util.function.Function
import java.util.function.Supplier

public fun interface Decoder<T> {

    public fun decode(tag: Tag): T

    public fun decodeNullable(tag: Tag): T? {
        return try {
            decode(tag)
        } catch (_: Exception) {
            null
        }
    }

    public fun field(name: String): MapDecoder<T> = FieldDecoder(name, this)

    public fun <U> map(mapper: Function<T, U>): Decoder<U> = Decoder { mapper.apply(decode(it)) }

    public companion object {

        @JvmStatic
        public fun <T> unit(value: T): MapDecoder<T> = MapDecoder { value }

        @JvmStatic
        public fun <T> unit(valueGetter: Supplier<T>): MapDecoder<T> = MapDecoder { valueGetter.get() }
    }
}
