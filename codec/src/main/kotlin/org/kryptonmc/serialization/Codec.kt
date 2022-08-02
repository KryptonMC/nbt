/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.serialization

import org.kryptonmc.nbt.ByteTag
import org.kryptonmc.nbt.DoubleTag
import org.kryptonmc.nbt.FloatTag
import org.kryptonmc.nbt.IntArrayTag
import org.kryptonmc.nbt.IntTag
import org.kryptonmc.nbt.LongTag
import org.kryptonmc.nbt.NumberTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.serialization.codecs.KeyDispatchCodec
import org.kryptonmc.serialization.codecs.NullableFieldCodec
import org.kryptonmc.serialization.codecs.OptionalFieldCodec
import org.kryptonmc.nbt.Tag
import org.kryptonmc.serialization.codecs.EitherCodec
import org.kryptonmc.serialization.codecs.ListCodec
import org.kryptonmc.serialization.codecs.SimpleMapCodec
import org.kryptonmc.serialization.codecs.UnboundedMapCodec
import org.kryptonmc.util.Either
import org.kryptonmc.util.isCollection
import org.kryptonmc.util.stream
import java.nio.ByteBuffer
import java.util.Arrays
import java.util.Optional
import java.util.function.Function
import java.util.function.UnaryOperator
import java.util.stream.IntStream

public interface Codec<T> : Encoder<T>, Decoder<T> {

    public fun list(): Codec<List<T>> = list(this)

    public fun <U> xmap(to: Function<T, U>, from: Function<U, T>): Codec<U> = of(comap(from), map(to))

    override fun field(name: String): MapCodec<T> = MapCodec.of(super<Encoder>.field(name), super<Decoder>.field(name))

    public fun nullableField(name: String): MapCodec<T?> = nullableField(name, this)

    public fun nullableField(name: String, default: T): MapCodec<T> = nullableField(name, this)
        .xmap({ it ?: default }, { if (it == default) null else it })

    public fun optionalField(name: String): MapCodec<Optional<T>> = optionalField(name, this)

    @Suppress("NULLABLE_TYPE_PARAMETER_AGAINST_NOT_NULL_TYPE_PARAMETER")
    public fun optionalField(name: String, default: T): MapCodec<T> = optionalField(name, this)
        .xmap({ it.orElse(default) }, { if (it == default) Optional.empty() else Optional.of(it) })

    public fun <E> dispatch(typeKey: String, type: Function<E, T>, codec: Function<T, Codec<E>>): Codec<E> =
        KeyDispatchCodec(typeKey, this, type, codec).codec()

    public fun <E> dispatch(type: Function<E, T>, codec: Function<T, Codec<E>>): Codec<E> = dispatch("type", type, codec)

    public companion object {

        @JvmField
        public val BOOLEAN: Codec<Boolean> = of(ByteTag::of, checkIs<ByteTag, _>("byte", "boolean") { it.value != 0.toByte() })
        @JvmField
        public val INT: Codec<Int> = of(IntTag::of, checkIs("int", "integer", IntTag::value))
        @JvmField
        public val LONG: Codec<Long> = of(LongTag::of, checkIs("long", "long", LongTag::value))
        @JvmField
        public val FLOAT: Codec<Float> = of(FloatTag::of, checkIs("float", "float", FloatTag::value))
        @JvmField
        public val DOUBLE: Codec<Double> = of(DoubleTag::of, checkIs("double", "double", DoubleTag::value))
        @JvmField
        public val STRING: Codec<String> = of(StringTag::of, checkIs("string", "string", StringTag::value))
        @JvmField
        public val INT_STREAM: Codec<IntStream> = of({ IntArrayTag(it.toArray()) }) { input ->
            if (input is IntArrayTag) return@of Arrays.stream(input.data)
            check(input.isCollection()) { "Cannot decode $input to an int stream as it is not a collection!" }
            val list = input.stream().toList()
            require(list.all { it is NumberTag }) { "Some elements in $input are not integers!" }
            list.stream().mapToInt { (it as NumberTag).toInt() }
        }

        @JvmStatic
        public fun <T> of(encoder: Encoder<T>, decoder: Decoder<T>): Codec<T> = object : Codec<T> {

            override fun decode(tag: Tag): T = decoder.decode(tag)

            override fun encode(value: T): Tag = encoder.encode(value)
        }

        @JvmStatic
        public fun <L, R> either(left: Codec<L>, right: Codec<R>): Codec<Either<L, R>> = EitherCodec(left, right)

        @JvmStatic
        public fun <E> list(elementCodec: Codec<E>): Codec<List<E>> = ListCodec(elementCodec)

        @JvmStatic
        public fun <K, V> simpleMap(keyCodec: Codec<K>, valueCodec: Codec<V>): SimpleMapCodec<K, V> = SimpleMapCodec(keyCodec, valueCodec)

        @JvmStatic
        public fun <K, V> unboundedMap(keyCodec: Codec<K>, valueCodec: Codec<V>): UnboundedMapCodec<K, V> = UnboundedMapCodec(keyCodec, valueCodec)

        @JvmStatic
        public fun <F> nullableField(name: String, elementCodec: Codec<F>): MapCodec<F?> = NullableFieldCodec(name, elementCodec)

        @JvmStatic
        public fun <F> optionalField(name: String, elementCodec: Codec<F>): MapCodec<Optional<F>> = OptionalFieldCodec(name, elementCodec)

        @JvmStatic
        public fun intRange(min: Int, max: Int): Codec<Int> {
            val checker = checkRange(min, max)
            return INT.xmap(checker, checker)
        }

        @JvmStatic
        public fun floatRange(min: Float, max: Float): Codec<Float> {
            val checker = checkRange(min, max)
            return FLOAT.xmap(checker, checker)
        }

        @JvmStatic
        public fun doubleRange(min: Double, max: Double): Codec<Double> {
            val checker = checkRange(min, max)
            return DOUBLE.xmap(checker, checker)
        }

        private fun <C : Comparable<C>> checkRange(min: C, max: C): UnaryOperator<C> = UnaryOperator {
            check(it in min..max) { "Value $it outside of required range! Must be between $min and $max!" }
            it
        }
    }
}

private inline fun <reified T : Tag, R> checkIs(tagType: String, name: String, crossinline mapper: (T) -> R): Decoder<R> = Decoder {
    check(it is T) { "Expected $tagType tag for $name codec, was $it!" }
    mapper(it)
}
