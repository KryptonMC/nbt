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

public fun interface MapDecoder<T> {

    public fun decode(input: CompoundTag): T

    public fun <U> map(mapper: Function<T, U>): MapDecoder<U> = MapDecoder { mapper.apply(decode(it)) }

    public fun decoder(): Decoder<T> = Decoder {
        check(it is CompoundTag) { "Cannot decode map from tag $it as it is not a map!" }
        decode(it)
    }
}
