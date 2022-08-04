/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.serialization.codecs

import kotlinx.collections.immutable.persistentListOf
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.Tag
import org.kryptonmc.nbt.list
import org.kryptonmc.serialization.Codec
import java.util.Objects
import java.util.stream.Stream

public class ListCodec<T>(private val elementCodec: Codec<T>) : Codec<List<T>> {

    override fun encode(value: List<T>): ListTag = list {
        value.forEach { add(elementCodec.encode(it)) }
    }

    override fun decode(tag: Tag): List<T> {
        check(tag is ListTag) { "Expected list tag for list codec, got $tag!" }
        val result = persistentListOf<T>().builder()
        val failed = mutableListOf<Tag>()
        tag.data.forEach {
            try {
                result.add(elementCodec.decode(it))
            } catch (_: Exception) {
                failed.add(it)
            }
        }
        if (failed.isNotEmpty()) error("Failed to decode list! Missing input: $failed")
        return result.build()
    }

    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        return Objects.equals(elementCodec, (other as ListCodec<*>).elementCodec)
    }

    override fun hashCode(): Int = Objects.hash(elementCodec)

    override fun toString(): String = "ListCodec[$elementCodec]"
}
