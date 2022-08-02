/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.serialization.codecs

import org.kryptonmc.serialization.Decoder
import org.kryptonmc.serialization.MapDecoder
import org.kryptonmc.nbt.CompoundTag
import java.util.Objects

public class FieldDecoder<T>(private val name: String, private val elementDecoder: Decoder<T>) : MapDecoder<T> {

    override fun decode(input: CompoundTag): T {
        val value = requireNotNull(input.get(name)) { "Could not find required key $name in input $input!" }
        return elementDecoder.decode(value)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        return Objects.equals(name, (other as FieldDecoder<*>).name) && Objects.equals(elementDecoder, other.elementDecoder)
    }

    override fun hashCode(): Int = Objects.hash(name, elementDecoder)

    override fun toString(): String = "FieldDecoder($name: $elementDecoder)"
}
