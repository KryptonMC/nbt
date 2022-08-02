/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.util.functional

import org.kryptonmc.util.Either
import java.util.function.Function

public interface CocartesianLike<T : K1, C, Mu : CocartesianLike.Mu> : Functor<T, Mu>, Traversable<T, Mu> {

    public fun <A> to(input: App<T, A>): App<Either.Mu<C>, A>

    public fun <A> from(input: App<Either.Mu<C>, A>): App<T, A>

    override fun <F : K1, A, B> traverse(applicative: Applicative<F, *>, function: Function<A, App<F, B>>, input: App<T, A>): App<F, App<T, B>> =
        applicative.map(::from, Either.Instance<C>().traverse(applicative, function, to(input)))

    public interface Mu : Functor.Mu, Traversable.Mu

    public companion object {

        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        public fun <F : K1, C, Mu : CocartesianLike.Mu> unbox(proofBox: App<Mu, F>): CocartesianLike<F, C, Mu> =
            proofBox as CocartesianLike<F, C, Mu>
    }
}