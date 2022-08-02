/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.util.functional

import org.kryptonmc.util.function.Function3
import org.kryptonmc.util.function.Function4
import org.kryptonmc.util.function.Function5
import org.kryptonmc.util.function.Function6
import org.kryptonmc.util.function.Function7
import org.kryptonmc.util.function.Function8
import org.kryptonmc.util.function.Function9
import org.kryptonmc.util.function.Function10
import org.kryptonmc.util.function.Function11
import org.kryptonmc.util.function.Function12
import org.kryptonmc.util.function.Function13
import org.kryptonmc.util.function.Function14
import org.kryptonmc.util.function.Function15
import org.kryptonmc.util.function.Function16
import java.util.function.BiFunction
import java.util.function.Function

public interface Applicative<FN : K1, Mu : Applicative.Mu> : Functor<FN, Mu> {

    public fun <A> point(a: A): App<FN, A>

    public fun <A, R> lift1(function: App<FN, Function<A, R>>): Function<App<FN, A>, App<FN, R>>

    public fun <A, B, R> lift2(function: App<FN, BiFunction<A, B, R>>): BiFunction<App<FN, A>, App<FN, B>, App<FN, R>> =
        BiFunction { fa, fb -> ap2(function, fa, fb) }

    public fun <A, R> ap(function: App<FN, Function<A, R>>, argument: App<FN, A>): App<FN, R> = lift1(function).apply(argument)

    public fun <A, R> ap(function: Function<A, R>, argument: App<FN, A>): App<FN, R> = map(function, argument)

    public fun <A, B, R> ap2(function: App<FN, BiFunction<A, B, R>>, a: App<FN, A>, b: App<FN, B>): App<FN, R> {
        val curry: Function<BiFunction<A, B, R>, Function<A, Function<B, R>>> = Function { f ->
            Function { a1 -> Function { b1 -> f.apply(a1, b1) } }
        }
        return ap(ap(map(curry, function), a), b)
    }

    public fun <A, B, C, R> ap3(function: App<FN, Function3<A, B, C, R>>, a: App<FN, A>, b: App<FN, B>, c: App<FN, C>): App<FN, R> =
        ap2(ap(map(Function3<A, B, C, R>::curry, function), a), b, c)

    public fun <A, B, C, D, R> ap4(
        function: App<FN, Function4<A, B, C, D, R>>,
        a: App<FN, A>,
        b: App<FN, B>,
        c: App<FN, C>,
        d: App<FN, D>
    ): App<FN, R> = ap2(ap2(map(Function4<A, B, C, D, R>::curry2, function), a, b), c, d)

    public fun <A, B, C, D, E, R> ap5(
        function: App<FN, Function5<A, B, C, D, E, R>>,
        a: App<FN, A>,
        b: App<FN, B>,
        c: App<FN, C>,
        d: App<FN, D>,
        e: App<FN, E>
    ): App<FN, R> = ap3(ap2(map(Function5<A, B, C, D, E, R>::curry2, function), a, b), c, d, e)

    public fun <A, B, C, D, E, F, R> ap6(
        function: App<FN, Function6<A, B, C, D, E, F, R>>,
        a: App<FN, A>,
        b: App<FN, B>,
        c: App<FN, C>,
        d: App<FN, D>,
        e: App<FN, E>,
        f: App<FN, F>
    ): App<FN, R> = ap3(ap3(map(Function6<A, B, C, D, E, F, R>::curry3, function), a, b, c), d, e, f)

    public fun <A, B, C, D, E, F, G, R> ap7(
        function: App<FN, Function7<A, B, C, D, E, F, G, R>>,
        a: App<FN, A>,
        b: App<FN, B>,
        c: App<FN, C>,
        d: App<FN, D>,
        e: App<FN, E>,
        f: App<FN, F>,
        g: App<FN, G>
    ): App<FN, R> = ap4(ap3(map(Function7<A, B, C, D, E, F, G, R>::curry4, function), a, b, c), d, e, f, g)

    public fun <A, B, C, D, E, F, G, H, R> ap8(
        function: App<FN, Function8<A, B, C, D, E, F, G, H, R>>,
        a: App<FN, A>,
        b: App<FN, B>,
        c: App<FN, C>,
        d: App<FN, D>,
        e: App<FN, E>,
        f: App<FN, F>,
        g: App<FN, G>,
        h: App<FN, H>
    ): App<FN, R> = ap4(ap4(map(Function8<A, B, C, D, E, F, G, H, R>::curry4, function), a, b, c, d), e, f, g, h)

    public fun <A, B, C, D, E, F, G, H, I, R> ap9(
        function: App<FN, Function9<A, B, C, D, E, F, G, H, I, R>>,
        a: App<FN, A>,
        b: App<FN, B>,
        c: App<FN, C>,
        d: App<FN, D>,
        e: App<FN, E>,
        f: App<FN, F>,
        g: App<FN, G>,
        h: App<FN, H>,
        i: App<FN, I>
    ): App<FN, R> = ap5(ap4(map(Function9<A, B, C, D, E, F, G, H, I, R>::curry4, function), a, b, c, d), e, f, g, h, i)

    public fun <A, B, C, D, E, F, G, H, I, J, R> ap10(
        function: App<FN, Function10<A, B, C, D, E, F, G, H, I, J, R>>,
        a: App<FN, A>,
        b: App<FN, B>,
        c: App<FN, C>,
        d: App<FN, D>,
        e: App<FN, E>,
        f: App<FN, F>,
        g: App<FN, G>,
        h: App<FN, H>,
        i: App<FN, I>,
        j: App<FN, J>
    ): App<FN, R> = ap5(ap5(map(Function10<A, B, C, D, E, F, G, H, I, J, R>::curry5, function), a, b, c, d, e), f, g, h, i, j)

    public fun <A, B, C, D, E, F, G, H, I, J, K, R> ap11(
        function: App<FN, Function11<A, B, C, D, E, F, G, H, I, J, K, R>>,
        a: App<FN, A>,
        b: App<FN, B>,
        c: App<FN, C>,
        d: App<FN, D>,
        e: App<FN, E>,
        f: App<FN, F>,
        g: App<FN, G>,
        h: App<FN, H>,
        i: App<FN, I>,
        j: App<FN, J>,
        k: App<FN, K>
    ): App<FN, R> = ap6(ap5(map(Function11<A, B, C, D, E, F, G, H, I, J, K, R>::curry5, function), a, b, c, d, e), f, g, h, i, j, k)

    public fun <A, B, C, D, E, F, G, H, I, J, K, L, R> ap12(
        function: App<FN, Function12<A, B, C, D, E, F, G, H, I, J, K, L, R>>,
        a: App<FN, A>,
        b: App<FN, B>,
        c: App<FN, C>,
        d: App<FN, D>,
        e: App<FN, E>,
        f: App<FN, F>,
        g: App<FN, G>,
        h: App<FN, H>,
        i: App<FN, I>,
        j: App<FN, J>,
        k: App<FN, K>,
        l: App<FN, L>
    ): App<FN, R> = ap6(ap6(map(Function12<A, B, C, D, E, F, G, H, I, J, K, L, R>::curry6, function), a, b, c, d, e, f), g, h, i, j, k, l)

    public fun <A, B, C, D, E, F, G, H, I, J, K, L, M, R> ap13(
        function: App<FN, Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, R>>,
        a: App<FN, A>,
        b: App<FN, B>,
        c: App<FN, C>,
        d: App<FN, D>,
        e: App<FN, E>,
        f: App<FN, F>,
        g: App<FN, G>,
        h: App<FN, H>,
        i: App<FN, I>,
        j: App<FN, J>,
        k: App<FN, K>,
        l: App<FN, L>,
        m: App<FN, M>
    ): App<FN, R> = ap7(ap6(map(Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, R>::curry6, function), a, b, c, d, e, f), g, h, i, j, k, l, m)

    public fun <A, B, C, D, E, F, G, H, I, J, K, L, M, N, R> ap14(
        function: App<FN, Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, R>>,
        a: App<FN, A>,
        b: App<FN, B>,
        c: App<FN, C>,
        d: App<FN, D>,
        e: App<FN, E>,
        f: App<FN, F>,
        g: App<FN, G>,
        h: App<FN, H>,
        i: App<FN, I>,
        j: App<FN, J>,
        k: App<FN, K>,
        l: App<FN, L>,
        m: App<FN, M>,
        n: App<FN, N>
    ): App<FN, R> =
        ap7(ap7(map(Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, R>::curry7, function), a, b, c, d, e, f, g), h, i, j, k, l, m, n)

    public fun <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, R> ap15(
        function: App<FN, Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, R>>,
        a: App<FN, A>,
        b: App<FN, B>,
        c: App<FN, C>,
        d: App<FN, D>,
        e: App<FN, E>,
        f: App<FN, F>,
        g: App<FN, G>,
        h: App<FN, H>,
        i: App<FN, I>,
        j: App<FN, J>,
        k: App<FN, K>,
        l: App<FN, L>,
        m: App<FN, M>,
        n: App<FN, N>,
        o: App<FN, O>
    ): App<FN, R> =
        ap8(ap7(map(Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, R>::curry7, function), a, b, c, d, e, f, g), h, i, j, k, l, m, n, o)

    public fun <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, R> ap16(
        function: App<FN, Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, R>>,
        a: App<FN, A>,
        b: App<FN, B>,
        c: App<FN, C>,
        d: App<FN, D>,
        e: App<FN, E>,
        f: App<FN, F>,
        g: App<FN, G>,
        h: App<FN, H>,
        i: App<FN, I>,
        j: App<FN, J>,
        k: App<FN, K>,
        l: App<FN, L>,
        m: App<FN, M>,
        n: App<FN, N>,
        o: App<FN, O>,
        p: App<FN, P>
    ): App<FN, R> = ap8(ap8(map(
        Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, R>::curry8,
        function
    ), a, b, c, d, e, f, g, h), i, j, k, l, m, n, o, p)

    public fun <A, B, R> apply2(function: BiFunction<A, B, R>, a: App<FN, A>, b: App<FN, B>): App<FN, R> = ap2(point(function), a, b)

    public interface Mu : Functor.Mu
}
