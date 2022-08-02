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

public class NullableFieldCodec<T>(private val name: String, private val elementCodec: Codec<T>) : MapCodec<T?> {

    override fun decode(input: CompoundTag): T? {
        val value = input.get(name) ?: return null
        return try {
            elementCodec.decode(value)
        } catch (_: Exception) {
            null
        }
    }

    override fun encode(value: T?, builder: CompoundTag.Builder): CompoundTag.Builder {
        if (value != null) return builder.put(name, elementCodec.encode(value))
        return builder
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        return Objects.equals(name, (other as NullableFieldCodec<*>).name) && Objects.equals(elementCodec, other.elementCodec)
    }

    override fun hashCode(): Int = Objects.hash(name, elementCodec)

    override fun toString(): String = "NullableFieldCodec($name: $elementCodec)"
}
