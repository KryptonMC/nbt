/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.serialization.codecs

import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.Decoder
import org.kryptonmc.serialization.Encoder
import org.kryptonmc.serialization.MapCodec
import org.kryptonmc.nbt.CompoundTag
import java.util.function.Function

public class KeyDispatchCodec<K, V>(
    private val typeKey: String,
    private val keyCodec: Codec<K>,
    private val type: Function<V, K>,
    private val decoder: Function<K, Decoder<V>>,
    private val encoder: Function<V, Encoder<V>?>
) : MapCodec<V> {

    public constructor(
        typeKey: String,
        keyCodec: Codec<K>,
        type: Function<V, K>,
        codec: Function<K, Codec<V>>
    ) : this(typeKey, keyCodec, type, codec::apply, Function { getEncoder(type, codec::apply, it) })

    override fun decode(input: CompoundTag): V {
        val keyName = requireNotNull(input.get(typeKey)) { "Input $input does not contain the key $typeKey!" }
        val key = keyCodec.decode(keyName)
        val valueDecoder = decoder.apply(key)
        if (valueDecoder is MapCodec.StandardCodec) return valueDecoder.codec.decode(input)
        return valueDecoder.decode(input.get("value")!!)
    }

    override fun encode(value: V, prefix: CompoundTag.Builder): CompoundTag.Builder {
        val elementEncoder = encoder.apply(value) ?: return prefix
        if (elementEncoder is MapCodec.StandardCodec) {
            return elementEncoder.codec.encode(value, prefix).put(typeKey, keyCodec.encode(type.apply(value)))
        }
        prefix.put(typeKey, keyCodec.encode(type.apply(value)))
        prefix.put("value", elementEncoder.encode(value))
        return prefix
    }

    override fun toString(): String = "KeyDispatchCodec[$keyCodec $type $decoder]"

    public companion object {

        @JvmStatic
        private fun <K, V> getEncoder(type: Function<V, K>, encoder: Function<K, Encoder<V>?>, input: V): Encoder<V>? =
            encoder.apply(type.apply(input))
    }
}
