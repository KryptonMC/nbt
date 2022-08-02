/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.serialization.codecs

import org.kryptonmc.serialization.MapCodec
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.serialization.Codec
import java.util.Objects
import java.util.Optional

public class OptionalFieldCodec<T>(private val name: String, private val elementCodec: Codec<T>) : MapCodec<Optional<T>> {

    @Suppress("NULLABLE_TYPE_PARAMETER_AGAINST_NOT_NULL_TYPE_PARAMETER")
    override fun decode(input: CompoundTag): Optional<T> {
        val value = input.get(name) ?: return Optional.empty()
        return try {
            Optional.of(elementCodec.decode(value))
        } catch (_: Exception) {
            Optional.empty()
        }
    }

    override fun encode(value: Optional<T>, builder: CompoundTag.Builder): CompoundTag.Builder {
        if (value.isPresent) return builder.put(name, elementCodec.encode(value.get()))
        return builder
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        return Objects.equals(name, (other as OptionalFieldCodec<*>).name) && Objects.equals(elementCodec, other.elementCodec)
    }

    override fun hashCode(): Int = Objects.hash(name, elementCodec)

    override fun toString(): String = "OptionalFieldCodec($name: $elementCodec)"
}
