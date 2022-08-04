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
import org.kryptonmc.serialization.MapCodec
import org.kryptonmc.util.Pair
import java.util.Objects

public class PairMapCodec<A, B>(private val first: MapCodec<A>, private val second: MapCodec<B>) : MapCodec<Pair<A, B>> {

    override fun decode(input: CompoundTag): Pair<A, B> = Pair.of(first.decode(input), second.decode(input))

    override fun encode(value: Pair<A, B>, prefix: CompoundTag.Builder): CompoundTag.Builder =
        first.encode(value.first, second.encode(value.second, prefix))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        return Objects.equals(first, (other as PairMapCodec<*, *>).first) && Objects.equals(second, other.second)
    }

    override fun hashCode(): Int = Objects.hash(first, second)

    override fun toString(): String = "PairMapCodec[$first, $second]"
}
