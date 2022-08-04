/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.serialization

import org.kryptonmc.serialization.codecs.PairMapCodec
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.Tag
import org.kryptonmc.serialization.codecs.CompoundCodecBuilder
import org.kryptonmc.serialization.codecs.EitherMapCodec
import org.kryptonmc.util.Either
import org.kryptonmc.util.Pair
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Supplier

public interface MapCodec<T> : MapEncoder<T>, MapDecoder<T> {

    public fun field(name: String): MapCodec<T> = codec().field(name)

    public fun codec(): Codec<T> = StandardCodec(this)

    public fun <U> xmap(to: Function<T, U>, from: Function<U, T>): MapCodec<U> = of(comap(from), map(to))

    public fun orElse(value: T, onError: Consumer<String?>): MapCodec<T> = mapResult({ onError.accept(it.message) }) {
        onError.accept(it.message)
        value
    }

    public fun orElse(value: T): MapCodec<T> = mapResult(null) { value }

    public fun orElseGet(valueGetter: Supplier<T>, onError: Consumer<String?>): MapCodec<T> = mapResult({ onError.accept(it.message) }) {
        onError.accept(it.message)
        valueGetter.get()
    }

    public fun orElseGet(valueGetter: Supplier<T>): MapCodec<T> = mapResult(null) { valueGetter.get() }

    public fun mapResult(encodeError: Consumer<Exception>?, decodeError: Function<Exception, T>): MapCodec<T> = object : MapCodec<T> {

        override fun encode(value: T, prefix: CompoundTag.Builder): CompoundTag.Builder {
            return try {
                encode(value, prefix)
            } catch (exception: Exception) {
                encodeError?.accept(exception)
                prefix
            }
        }

        override fun decode(input: CompoundTag): T {
            return try {
                decode(input)
            } catch (exception: Exception) {
                decodeError.apply(exception)
            }
        }

        override fun toString(): String = "${this@MapCodec}[mapResult ${encodeError?.toString().orEmpty()} $decodeError]"
    }

    public fun <O> getting(getter: Function<O, T>): CompoundCodecBuilder<O, T> = CompoundCodecBuilder.of(getter, this)

    public class StandardCodec<T>(public val codec: MapCodec<T>) : Codec<T> {

        override fun decode(tag: Tag): T {
            check(tag is CompoundTag) { "Expected compound tag for map codec, got $tag!" }
            return codec.decode(tag)
        }

        override fun encode(value: T): Tag = codec.encode(value, CompoundTag.immutableBuilder()).build()
    }

    public companion object {

        @JvmField
        public val EMPTY: MapCodec<Unit> = of(Encoder.empty(), Decoder.unit(Unit))

        @JvmStatic
        public fun <T> of(encoder: MapEncoder<T>, decoder: MapDecoder<T>): MapCodec<T> = of(encoder, decoder) { "MapCodec[$encoder $decoder]" }

        @JvmStatic
        public fun <T> of(encoder: MapEncoder<T>, decoder: MapDecoder<T>, name: Supplier<String>): MapCodec<T> = object : MapCodec<T> {

            override fun decode(input: CompoundTag): T = decoder.decode(input)

            override fun encode(value: T, prefix: CompoundTag.Builder): CompoundTag.Builder = encoder.encode(value, prefix)

            override fun toString(): String = name.get()
        }

        @JvmStatic
        public fun <F, S> pair(first: MapCodec<F>, second: MapCodec<S>): MapCodec<Pair<F, S>> = PairMapCodec(first, second)

        @JvmStatic
        public fun <L, R> either(left: MapCodec<L>, right: MapCodec<R>): MapCodec<Either<L, R>> = EitherMapCodec(left, right)

        @JvmStatic
        public fun <T> unit(default: T): MapCodec<T> = of(Encoder.empty(), Decoder.unit(default))

        @JvmStatic
        public fun <T> unit(default: Supplier<T>): MapCodec<T> = of(Encoder.empty(), Decoder.unit(default))
    }
}
