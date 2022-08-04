/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.serialization.codecs

import org.kryptonmc.util.function.Function3
import org.kryptonmc.util.function.Function4
import org.kryptonmc.util.functional.App
import org.kryptonmc.util.functional.Applicative
import org.kryptonmc.util.functional.K1
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.Decoder
import org.kryptonmc.serialization.Encoder
import org.kryptonmc.serialization.MapCodec
import org.kryptonmc.serialization.MapDecoder
import org.kryptonmc.serialization.MapEncoder
import java.util.function.BiFunction
import java.util.function.Function

public class CompoundCodecBuilder<O, F> private constructor(
    private val getter: Function<O, F>,
    private val encoder: Function<O, MapEncoder<F>>,
    private val decoder: MapDecoder<F>
) : App<CompoundCodecBuilder.Mu<O>, F> {

    public class Mu<O> : K1

    public class Instance<O> : Applicative<Mu<O>, Instance.Mu<O>> {

        override fun <A> point(a: A): App<CompoundCodecBuilder.Mu<O>, A> = Companion.point(a)

        override fun <A, R> lift1(
            function: App<CompoundCodecBuilder.Mu<O>, Function<A, R>>
        ): Function<App<CompoundCodecBuilder.Mu<O>, A>, App<CompoundCodecBuilder.Mu<O>, R>> = Function { fa ->
            val f = unbox(function)
            val a = unbox(fa)
            CompoundCodecBuilder(
                { f.getter.apply(it).apply(a.getter.apply(it)) },
                {
                    val fEncoder = f.encoder.apply(it)
                    val aEncoder = a.encoder.apply(it)
                    val aFromO = a.getter.apply(it)
                    object : MapEncoder<R> {

                        override fun encode(value: R, prefix: CompoundTag.Builder): CompoundTag.Builder {
                            aEncoder.encode(aFromO, prefix)
                            fEncoder.encode({ value }, prefix)
                            return prefix
                        }

                        override fun toString(): String = "$fEncoder * $aEncoder"
                    }
                },
                object : MapDecoder<R> {

                    override fun decode(input: CompoundTag): R = f.decoder.decode(input).apply(a.decoder.decode(input))

                    override fun toString(): String = "${f.decoder} * ${a.decoder}"
                }
            )
        }

        override fun <A, B, R> ap2(
            function: App<CompoundCodecBuilder.Mu<O>, BiFunction<A, B, R>>,
            a: App<CompoundCodecBuilder.Mu<O>, A>,
            b: App<CompoundCodecBuilder.Mu<O>, B>
        ): App<CompoundCodecBuilder.Mu<O>, R> {
            val f = unbox(function)
            val fa = unbox(a)
            val fb = unbox(b)
            return CompoundCodecBuilder(
                { f.getter.apply(it).apply(fa.getter.apply(it), fb.getter.apply(it)) },
                {
                    val fEncoder = f.encoder.apply(it)
                    val aEncoder = fa.encoder.apply(it)
                    val aFromO = fa.getter.apply(it)
                    val bEncoder = fb.encoder.apply(it)
                    val bFromO = fb.getter.apply(it)
                    object : MapEncoder<R> {

                        override fun encode(value: R, prefix: CompoundTag.Builder): CompoundTag.Builder {
                            aEncoder.encode(aFromO, prefix)
                            bEncoder.encode(bFromO, prefix)
                            fEncoder.encode({ _, _ -> value }, prefix)
                            return prefix
                        }

                        override fun toString(): String = "$fEncoder * $aEncoder * $bEncoder"
                    }
                },
                object : MapDecoder<R> {

                    override fun decode(input: CompoundTag): R = f.decoder.decode(input).apply(fa.decoder.decode(input), fb.decoder.decode(input))

                    override fun toString(): String = "${f.decoder} * ${fa.decoder} * ${fb.decoder}"
                }
            )
        }

        override fun <A, B, C, R> ap3(
            function: App<CompoundCodecBuilder.Mu<O>, Function3<A, B, C, R>>,
            a: App<CompoundCodecBuilder.Mu<O>, A>,
            b: App<CompoundCodecBuilder.Mu<O>, B>,
            c: App<CompoundCodecBuilder.Mu<O>, C>
        ): App<CompoundCodecBuilder.Mu<O>, R> {
            val f = unbox(function)
            val fa = unbox(a)
            val fb = unbox(b)
            val fc = unbox(c)
            return CompoundCodecBuilder(
                { f.getter.apply(it).apply(fa.getter.apply(it), fb.getter.apply(it), fc.getter.apply(it)) },
                {
                    val fEncoder = f.encoder.apply(it)
                    val aEncoder = fa.encoder.apply(it)
                    val aFromO = fa.getter.apply(it)
                    val bEncoder = fb.encoder.apply(it)
                    val bFromO = fb.getter.apply(it)
                    val cEncoder = fc.encoder.apply(it)
                    val cFromO = fc.getter.apply(it)
                    object : MapEncoder<R> {

                        override fun encode(value: R, prefix: CompoundTag.Builder): CompoundTag.Builder {
                            aEncoder.encode(aFromO, prefix)
                            bEncoder.encode(bFromO, prefix)
                            cEncoder.encode(cFromO, prefix)
                            fEncoder.encode({ _, _, _ -> value }, prefix)
                            return prefix
                        }

                        override fun toString(): String = "$fEncoder * $aEncoder * $bEncoder * $cEncoder"
                    }
                },
                object : MapDecoder<R> {

                    override fun decode(input: CompoundTag): R =
                        f.decoder.decode(input).apply(fa.decoder.decode(input), fb.decoder.decode(input), fc.decoder.decode(input))

                    override fun toString(): String = "${f.decoder} * ${fa.decoder} * ${fb.decoder} * ${fc.decoder}"
                }
            )
        }

        override fun <A, B, C, D, R> ap4(
            function: App<CompoundCodecBuilder.Mu<O>, Function4<A, B, C, D, R>>,
            a: App<CompoundCodecBuilder.Mu<O>, A>,
            b: App<CompoundCodecBuilder.Mu<O>, B>,
            c: App<CompoundCodecBuilder.Mu<O>, C>,
            d: App<CompoundCodecBuilder.Mu<O>, D>
        ): App<CompoundCodecBuilder.Mu<O>, R> {
            val f = unbox(function)
            val fa = unbox(a)
            val fb = unbox(b)
            val fc = unbox(c)
            val fd = unbox(d)
            return CompoundCodecBuilder(
                { f.getter.apply(it).apply(fa.getter.apply(it), fb.getter.apply(it), fc.getter.apply(it), fd.getter.apply(it)) },
                {
                    val fEncoder = f.encoder.apply(it)
                    val aEncoder = fa.encoder.apply(it)
                    val aFromO = fa.getter.apply(it)
                    val bEncoder = fb.encoder.apply(it)
                    val bFromO = fb.getter.apply(it)
                    val cEncoder = fc.encoder.apply(it)
                    val cFromO = fc.getter.apply(it)
                    val dEncoder = fd.encoder.apply(it)
                    val dFromO = fd.getter.apply(it)
                    object : MapEncoder<R> {

                        override fun encode(value: R, prefix: CompoundTag.Builder): CompoundTag.Builder {
                            aEncoder.encode(aFromO, prefix)
                            bEncoder.encode(bFromO, prefix)
                            cEncoder.encode(cFromO, prefix)
                            dEncoder.encode(dFromO, prefix)
                            fEncoder.encode({ _, _, _, _ -> value }, prefix)
                            return prefix
                        }

                        override fun toString(): String = "$fEncoder * $aEncoder * $bEncoder * $cEncoder * $dEncoder"
                    }
                },
                object : MapDecoder<R> {

                    override fun decode(input: CompoundTag): R = f.decoder.decode(input)
                        .apply(fa.decoder.decode(input), fb.decoder.decode(input), fc.decoder.decode(input), fd.decoder.decode(input))

                    override fun toString(): String = "${f.decoder} * ${fa.decoder} * ${fb.decoder} * ${fc.decoder} * ${fd.decoder}"
                }
            )
        }

        override fun <T, R> map(function: Function<T, R>, ts: App<CompoundCodecBuilder.Mu<O>, T>): App<CompoundCodecBuilder.Mu<O>, R> {
            val unbox = unbox(ts)
            val getter = unbox.getter
            return CompoundCodecBuilder(
                getter.andThen(function),
                {
                    object : MapEncoder<R> {

                        private val encoder = unbox.encoder.apply(it)

                        override fun encode(value: R, prefix: CompoundTag.Builder): CompoundTag.Builder = encoder.encode(getter.apply(it), prefix)

                        override fun toString(): String = "$encoder[mapped]"
                    }
                },
                unbox.decoder.map(function)
            )
        }

        private class Mu<O> : Applicative.Mu
    }

    public companion object {

        @JvmStatic
        public fun <O, F> unbox(box: App<Mu<O>, F>): CompoundCodecBuilder<O, F> = box as CompoundCodecBuilder<O, F>

        @JvmStatic
        public fun <O> instance(): Instance<O> = Instance()

        @JvmStatic
        public fun <O, F> of(getter: Function<O, F>, name: String, fieldCodec: Codec<F>): CompoundCodecBuilder<O, F> =
            of(getter, fieldCodec.field(name))

        @JvmStatic
        public fun <O, F> of(getter: Function<O, F>, codec: MapCodec<F>): CompoundCodecBuilder<O, F> = CompoundCodecBuilder(getter, { codec }, codec)

        @JvmStatic
        public fun <O, F> point(instance: F): CompoundCodecBuilder<O, F> =
            CompoundCodecBuilder({ instance }, { Encoder.empty() }, Decoder.unit(instance))

        @JvmStatic
        public fun <O> create(builder: Function<Instance<O>, App<Mu<O>, O>>): Codec<O> = build(builder.apply(instance())).codec()

        @JvmStatic
        public fun <O> createMap(builder: Function<Instance<O>, App<Mu<O>, O>>): MapCodec<O> = build(builder.apply(instance()))

        @JvmStatic
        public fun <O> build(builderBox: App<Mu<O>, O>): MapCodec<O> {
            val unboxed = unbox(builderBox)
            return object : MapCodec<O> {

                override fun decode(input: CompoundTag): O = unboxed.decoder.decode(input)

                override fun encode(value: O, prefix: CompoundTag.Builder): CompoundTag.Builder =
                    unboxed.encoder.apply(value).encode(value, prefix)

                override fun toString(): String = "RecordCodec[${unboxed.decoder}]"
            }
        }
    }
}
