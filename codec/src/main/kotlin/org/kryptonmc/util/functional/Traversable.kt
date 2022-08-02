/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.util.functional

import java.util.function.Function

public interface Traversable<T : K1, Mu : Traversable.Mu> : Functor<T, Mu> {

    public fun <F : K1, A, B> traverse(applicative: Applicative<F, *>, function: Function<A, App<F, B>>, input: App<T, A>): App<F, App<T, B>>

    public fun <F : K1, A> flip(applicative: Applicative<F, *>, input: App<T, App<F, A>>): App<F, App<T, A>> =
        traverse(applicative, Function.identity(), input)

    public interface Mu : Functor.Mu

    public companion object {

        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        public fun <F : K1, Mu : Traversable.Mu> unbox(proofBox: App<Mu, F>): Traversable<F, Mu> = proofBox as Traversable<F, Mu>
    }
}