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

public fun interface MapDecoder<T> {

    public fun decode(input: CompoundTag): T

    public fun <U> map(mapper: Function<T, U>): MapDecoder<U> = object : MapDecoder<U> {

        override fun decode(input: CompoundTag): U = mapper.apply(this@MapDecoder.decode(input))

        override fun toString(): String = "${this@MapDecoder}[mapped]"
    }

    public fun decoder(): Decoder<T> = object : Decoder<T> {

        override fun decode(tag: Tag): T {
            check(tag is CompoundTag) { "Cannot decode map from tag $tag as it is not a map!" }
            return this@MapDecoder.decode(tag)
        }

        override fun toString(): String = this@MapDecoder.toString()
    }
}
