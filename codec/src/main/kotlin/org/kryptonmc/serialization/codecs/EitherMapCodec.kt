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
import org.kryptonmc.util.Either
import java.util.Objects

public class EitherMapCodec<L, R>(private val left: MapCodec<L>, private val right: MapCodec<R>) : MapCodec<Either<L, R>> {

    override fun decode(input: CompoundTag): Either<L, R> {
        return try {
            Either.left(left.decode(input))
        } catch (_: Exception) {
            Either.right(right.decode(input))
        }
    }

    override fun encode(value: Either<L, R>, builder: CompoundTag.Builder): CompoundTag.Builder =
        value.map({ left.encode(it, builder) }, { right.encode(it, builder) })

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        return Objects.equals(left, (other as EitherMapCodec<*, *>).left) && Objects.equals(right, other.right)
    }

    override fun hashCode(): Int = Objects.hash(left, right)

    override fun toString(): String = "EitherMapCodec($left, $right)"
}