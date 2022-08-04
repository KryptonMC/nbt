/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.util

import org.kryptonmc.util.functional.App
import org.kryptonmc.util.functional.Applicative
import org.kryptonmc.util.functional.CartesianLike
import org.kryptonmc.util.functional.K1
import org.kryptonmc.util.functional.Traversable
import java.util.Objects
import java.util.function.Function

@JvmRecord
public data class Pair<F, S>(public val first: F, public val second: S) : App<Pair.Mu<S>, F> {

    public fun <F2> mapFirst(function: Function<F, F2>): Pair<F2, S> = of(function.apply(first), second)

    public fun <S2> mapSecond(function: Function<S, S2>): Pair<F, S2> = of(first, function.apply(second))

    public fun swap(): Pair<S, F> = of(second, first)

    override fun equals(other: Any?): Boolean = other is Pair<*, *> && first == other.first && second == other.second

    override fun hashCode(): Int = Objects.hash(first, second)

    override fun toString(): String = "($first, $second)"

    public class Mu<S> : K1

    public class Instance<S2> : Traversable<Mu<S2>, Instance.Mu<S2>>, CartesianLike<Mu<S2>, S2, Instance.Mu<S2>> {

        override fun <T, R> map(function: Function<T, R>, ts: App<Pair.Mu<S2>, T>): App<Pair.Mu<S2>, R> = unbox(ts).mapFirst(function)

        override fun <F : K1, A, B> traverse(
            applicative: Applicative<F, *>,
            function: Function<A, App<F, B>>,
            input: App<Pair.Mu<S2>, A>
        ): App<F, App<Pair.Mu<S2>, B>> {
            val pair = unbox(input)
            return applicative.ap({ b -> of(b, pair.second) }, function.apply(pair.first))
        }

        override fun <A> to(input: App<Pair.Mu<S2>, A>): App<Pair.Mu<S2>, A> = input

        override fun <A> from(input: App<Pair.Mu<S2>, A>): App<Pair.Mu<S2>, A> = input

        public class Mu<S2> : Traversable.Mu, CartesianLike.Mu
    }

    public companion object {

        @JvmStatic
        public fun <F, S> unbox(box: App<Mu<S>, F>): Pair<F, S> = box as Pair<F, S>

        @JvmStatic
        public fun <F, S> of(first: F, second: S): Pair<F, S> = Pair(first, second)
    }
}