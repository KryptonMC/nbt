/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.serialization.codecs

import org.kryptonmc.nbt.Tag
import org.kryptonmc.serialization.Codec
import org.kryptonmc.util.Either
import java.util.Objects

public class EitherCodec<L, R>(private val left: Codec<L>, private val right: Codec<R>) : Codec<Either<L, R>> {

    override fun decode(tag: Tag): Either<L, R> {
        return try {
            Either.left(left.decode(tag))
        } catch (_: Exception) {
            Either.right(right.decode(tag))
        }
    }

    override fun encode(value: Either<L, R>): Tag = value.map(left::encode, right::encode)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        return Objects.equals(left, (other as EitherCodec<*, *>).left) && Objects.equals(right, other.right)
    }

    override fun hashCode(): Int = Objects.hash(left, right)

    override fun toString(): String = "EitherCodec[$left, $right]"
}