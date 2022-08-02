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

/**
 * Many different product classes that allow us to do some magic with
 * constructing an object for a codec.
 */
public interface Products {

    @JvmRecord
    public data class P1<FN : K1, A>(val a: App<FN, A>) {

        public fun <B> and(b: App<FN, B>): P2<FN, A, B> = P2(a, b)

        public fun <B, C> and(p: P2<FN, B, C>): P3<FN, A, B, C> = P3(a, p.a, p.b)

        public fun <B, C, D> and(p: P3<FN, B, C, D>): P4<FN, A, B, C, D> = P4(a, p.a, p.b, p.c)

        public fun <B, C, D, E> and(p: P4<FN, B, C, D, E>): P5<FN, A, B, C, D, E> = P5(a, p.a, p.b, p.c, p.d)

        public fun <B, C, D, E, F> and(p: P5<FN, B, C, D, E, F>): P6<FN, A, B, C, D, E, F> = P6(a, p.a, p.b, p.c, p.d, p.e)

        public fun <B, C, D, E, F, G> and(p: P6<FN, B, C, D, E, F, G>): P7<FN, A, B, C, D, E, F, G> = P7(a, p.a, p.b, p.c, p.d, p.e, p.f)

        public fun <B, C, D, E, F, G, H> and(p: P7<FN, B, C, D, E, F, G, H>): P8<FN, A, B, C, D, E, F, G, H> =
            P8(a, p.a, p.b, p.c, p.d, p.e, p.f, p.g)

        public fun <R> apply(instance: Applicative<FN, *>, function: Function<A, R>): App<FN, R> = apply(instance, instance.point(function))

        public fun <R> apply(instance: Applicative<FN, *>, function: App<FN, Function<A, R>>): App<FN, R> = instance.ap(function, a)
    }

    @JvmRecord
    public data class P2<FN : K1, A, B>(val a: App<FN, A>, val b: App<FN, B>) {

        public fun <C> and(c: App<FN, C>): P3<FN, A, B, C> = P3(a, b, c)

        public fun <C, D> and(p: P2<FN, C, D>): P4<FN, A, B, C, D> = P4(a, b, p.a, p.b)

        public fun <C, D, E> and(p: P3<FN, C, D, E>): P5<FN, A, B, C, D, E> = P5(a, b, p.a, p.b, p.c)

        public fun <C, D, E, F> and(p: P4<FN, C, D, E, F>): P6<FN, A, B, C, D, E, F> = P6(a, b, p.a, p.b, p.c, p.d)

        public fun <C, D, E, F, G> and(p: P5<FN, C, D, E, F, G>): P7<FN, A, B, C, D, E, F, G> = P7(a, b, p.a, p.b, p.c, p.d, p.e)

        public fun <C, D, E, F, G, H> and(p: P6<FN, C, D, E, F, G, H>): P8<FN, A, B, C, D, E, F, G, H> =
            P8(a, b, p.a, p.b, p.c, p.d, p.e, p.f)

        public fun <R> apply(instance: Applicative<FN, *>, function: BiFunction<A, B, R>): App<FN, R> = apply(instance, instance.point(function))

        public fun <R> apply(instance: Applicative<FN, *>, function: App<FN, BiFunction<A, B, R>>): App<FN, R> = instance.ap2(function, a, b)
    }

    @JvmRecord
    public data class P3<FN : K1, A, B, C>(val a: App<FN, A>, val b: App<FN, B>, val c: App<FN, C>) {

        public fun <D> and(d: App<FN, D>): P4<FN, A, B, C, D> = P4(a, b, c, d)

        public fun <D, E> and(p: P2<FN, D, E>): P5<FN, A, B, C, D, E> = P5(a, b, c, p.a, p.b)

        public fun <D, E, F> and(p: P3<FN, D, E, F>): P6<FN, A, B, C, D, E, F> = P6(a, b, c, p.a, p.b, p.c)

        public fun <D, E, F, G> and(p: P4<FN, D, E, F, G>): P7<FN, A, B, C, D, E, F, G> = P7(a, b, c, p.a, p.b, p.c, p.d)

        public fun <D, E, F, G, H> and(p: P5<FN, D, E, F, G, H>): P8<FN, A, B, C, D, E, F, G, H> = P8(a, b, c, p.a, p.b, p.c, p.d, p.e)

        public fun <R> apply(instance: Applicative<FN, *>, function: Function3<A, B, C, R>): App<FN, R> = apply(instance, instance.point(function))

        public fun <R> apply(instance: Applicative<FN, *>, function: App<FN, Function3<A, B, C, R>>): App<FN, R> = instance.ap3(function, a, b, c)
    }

    @JvmRecord
    public data class P4<FN : K1, A, B, C, D>(val a: App<FN, A>, val b: App<FN, B>, val c: App<FN, C>, val d: App<FN, D>) {

        public fun <E> and(e: App<FN, E>): P5<FN, A, B, C, D, E> = P5(a, b, c, d, e)

        public fun <E, F> and(p: P2<FN, E, F>): P6<FN, A, B, C, D, E, F> = P6(a, b, c, d, p.a, p.b)

        public fun <E, F, G> and(p: P3<FN, E, F, G>): P7<FN, A, B, C, D, E, F, G> = P7(a, b, c, d, p.a, p.b, p.c)

        public fun <E, F, G, H> and(p: P4<FN, E, F, G, H>): P8<FN, A, B, C, D, E, F, G, H> = P8(a, b, c, d, p.a, p.b, p.c, p.d)

        public fun <R> apply(instance: Applicative<FN, *>, function: Function4<A, B, C, D, R>): App<FN, R> =
            apply(instance, instance.point(function))

        public fun <R> apply(instance: Applicative<FN, *>, function: App<FN, Function4<A, B, C, D, R>>): App<FN, R> =
            instance.ap4(function, a, b, c, d)
    }

    @JvmRecord
    public data class P5<FN : K1, A, B, C, D, E>(val a: App<FN, A>, val b: App<FN, B>, val c: App<FN, C>, val d: App<FN, D>, val e: App<FN, E>) {

        public fun <F> and(f: App<FN, F>): P6<FN, A, B, C, D, E, F> = P6(a, b, c, d, e, f)

        public fun <F, G> and(p: P2<FN, F, G>): P7<FN, A, B, C, D, E, F, G> = P7(a, b, c, d, e, p.a, p.b)

        public fun <F, G, H> and(p: P3<FN, F, G, H>): P8<FN, A, B, C, D, E, F, G, H> = P8(a, b, c, d, e, p.a, p.b, p.c)

        public fun <R> apply(instance: Applicative<FN, *>, function: Function5<A, B, C, D, E, R>): App<FN, R> =
            apply(instance, instance.point(function))

        public fun <R> apply(instance: Applicative<FN, *>, function: App<FN, Function5<A, B, C, D, E, R>>): App<FN, R> =
            instance.ap5(function, a, b, c, d, e)
    }

    @JvmRecord
    public data class P6<FN : K1, A, B, C, D, E, F>(
        val a: App<FN, A>,
        val b: App<FN, B>,
        val c: App<FN, C>,
        val d: App<FN, D>,
        val e: App<FN, E>,
        val f: App<FN, F>
    ) {

        public fun <G> and(g: App<FN, G>): P7<FN, A, B, C, D, E, F, G> = P7(a, b, c, d, e, f, g)

        public fun <G, H> and(p: P2<FN, G, H>): P8<FN, A, B, C, D, E, F, G, H> = P8(a, b, c, d, e, f, p.a, p.b)

        public fun <R> apply(instance: Applicative<FN, *>, function: Function6<A, B, C, D, E, F, R>): App<FN, R> =
            apply(instance, instance.point(function))

        public fun <R> apply(instance: Applicative<FN, *>, function: App<FN, Function6<A, B, C, D, E, F, R>>): App<FN, R> =
            instance.ap6(function, a, b, c, d, e, f)
    }

    @JvmRecord
    public data class P7<FN : K1, A, B, C, D, E, F, G>(
        val a: App<FN, A>,
        val b: App<FN, B>,
        val c: App<FN, C>,
        val d: App<FN, D>,
        val e: App<FN, E>,
        val f: App<FN, F>,
        val g: App<FN, G>
    ) {

        public fun <H> and(h: App<FN, H>): P8<FN, A, B, C, D, E, F, G, H> = P8(a, b, c, d, e, f, g, h)

        public fun <R> apply(instance: Applicative<FN, *>, function: Function7<A, B, C, D, E, F, G, R>): App<FN, R> =
            apply(instance, instance.point(function))

        public fun <R> apply(instance: Applicative<FN, *>, function: App<FN, Function7<A, B, C, D, E, F, G, R>>): App<FN, R> =
            instance.ap7(function, a, b, c, d, e, f, g)
    }

    @JvmRecord
    public data class P8<FN : K1, A, B, C, D, E, F, G, H>(
        val a: App<FN, A>,
        val b: App<FN, B>,
        val c: App<FN, C>,
        val d: App<FN, D>,
        val e: App<FN, E>,
        val f: App<FN, F>,
        val g: App<FN, G>,
        val h: App<FN, H>
    ) {

        public fun <R> apply(instance: Applicative<FN, *>, function: Function8<A, B, C, D, E, F, G, H, R>): App<FN, R> =
            apply(instance, instance.point(function))

        public fun <R> apply(instance: Applicative<FN, *>, function: App<FN, Function8<A, B, C, D, E, F, G, H, R>>): App<FN, R> =
            instance.ap8(function, a, b, c, d, e, f, g, h)
    }

    @JvmRecord
    public data class P9<FN : K1, A, B, C, D, E, F, G, H, I>(
        val a: App<FN, A>,
        val b: App<FN, B>,
        val c: App<FN, C>,
        val d: App<FN, D>,
        val e: App<FN, E>,
        val f: App<FN, F>,
        val g: App<FN, G>,
        val h: App<FN, H>,
        val i: App<FN, I>
    ) {

        public fun <R> apply(instance: Applicative<FN, *>, function: Function9<A, B, C, D, E, F, G, H, I, R>): App<FN, R> =
            apply(instance, instance.point(function))

        public fun <R> apply(instance: Applicative<FN, *>, function: App<FN, Function9<A, B, C, D, E, F, G, H, I, R>>): App<FN, R> =
            instance.ap9(function, a, b, c, d, e, f, g, h, i)
    }

    @JvmRecord
    public data class P10<FN : K1, A, B, C, D, E, F, G, H, I, J>(
        val a: App<FN, A>,
        val b: App<FN, B>,
        val c: App<FN, C>,
        val d: App<FN, D>,
        val e: App<FN, E>,
        val f: App<FN, F>,
        val g: App<FN, G>,
        val h: App<FN, H>,
        val i: App<FN, I>,
        val j: App<FN, J>
    ) {

        public fun <R> apply(instance: Applicative<FN, *>, function: Function10<A, B, C, D, E, F, G, H, I, J, R>): App<FN, R> =
            apply(instance, instance.point(function))

        public fun <R> apply(instance: Applicative<FN, *>, function: App<FN, Function10<A, B, C, D, E, F, G, H, I, J, R>>): App<FN, R> =
            instance.ap10(function, a, b, c, d, e, f, g, h, i, j)
    }

    @JvmRecord
    public data class P11<FN : K1, A, B, C, D, E, F, G, H, I, J, K>(
        val a: App<FN, A>,
        val b: App<FN, B>,
        val c: App<FN, C>,
        val d: App<FN, D>,
        val e: App<FN, E>,
        val f: App<FN, F>,
        val g: App<FN, G>,
        val h: App<FN, H>,
        val i: App<FN, I>,
        val j: App<FN, J>,
        val k: App<FN, K>
    ) {

        public fun <R> apply(instance: Applicative<FN, *>, function: Function11<A, B, C, D, E, F, G, H, I, J, K, R>): App<FN, R> =
            apply(instance, instance.point(function))

        public fun <R> apply(instance: Applicative<FN, *>, function: App<FN, Function11<A, B, C, D, E, F, G, H, I, J, K, R>>): App<FN, R> =
            instance.ap11(function, a, b, c, d, e, f, g, h, i, j, k)
    }

    @JvmRecord
    public data class P12<FN : K1, A, B, C, D, E, F, G, H, I, J, K, L>(
        val a: App<FN, A>,
        val b: App<FN, B>,
        val c: App<FN, C>,
        val d: App<FN, D>,
        val e: App<FN, E>,
        val f: App<FN, F>,
        val g: App<FN, G>,
        val h: App<FN, H>,
        val i: App<FN, I>,
        val j: App<FN, J>,
        val k: App<FN, K>,
        val l: App<FN, L>
    ) {

        public fun <R> apply(instance: Applicative<FN, *>, function: Function12<A, B, C, D, E, F, G, H, I, J, K, L, R>): App<FN, R> =
            apply(instance, instance.point(function))

        public fun <R> apply(instance: Applicative<FN, *>, function: App<FN, Function12<A, B, C, D, E, F, G, H, I, J, K, L, R>>): App<FN, R> =
            instance.ap12(function, a, b, c, d, e, f, g, h, i, j, k, l)
    }

    @JvmRecord
    public data class P13<FN : K1, A, B, C, D, E, F, G, H, I, J, K, L, M>(
        val a: App<FN, A>,
        val b: App<FN, B>,
        val c: App<FN, C>,
        val d: App<FN, D>,
        val e: App<FN, E>,
        val f: App<FN, F>,
        val g: App<FN, G>,
        val h: App<FN, H>,
        val i: App<FN, I>,
        val j: App<FN, J>,
        val k: App<FN, K>,
        val l: App<FN, L>,
        val m: App<FN, M>
    ) {

        public fun <R> apply(instance: Applicative<FN, *>, function: Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, R>): App<FN, R> =
            apply(instance, instance.point(function))

        public fun <R> apply(instance: Applicative<FN, *>, function: App<FN, Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, R>>): App<FN, R> =
            instance.ap13(function, a, b, c, d, e, f, g, h, i, j, k, l, m)
    }

    @JvmRecord
    public data class P14<FN : K1, A, B, C, D, E, F, G, H, I, J, K, L, M, N>(
        val a: App<FN, A>,
        val b: App<FN, B>,
        val c: App<FN, C>,
        val d: App<FN, D>,
        val e: App<FN, E>,
        val f: App<FN, F>,
        val g: App<FN, G>,
        val h: App<FN, H>,
        val i: App<FN, I>,
        val j: App<FN, J>,
        val k: App<FN, K>,
        val l: App<FN, L>,
        val m: App<FN, M>,
        val n: App<FN, N>
    ) {

        public fun <R> apply(instance: Applicative<FN, *>, function: Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, R>): App<FN, R> =
            apply(instance, instance.point(function))

        public fun <R> apply(instance: Applicative<FN, *>, function: App<FN, Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, R>>): App<FN, R> =
            instance.ap14(function, a, b, c, d, e, f, g, h, i, j, k, l, m, n)
    }

    @JvmRecord
    public data class P15<FN : K1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O>(
        val a: App<FN, A>,
        val b: App<FN, B>,
        val c: App<FN, C>,
        val d: App<FN, D>,
        val e: App<FN, E>,
        val f: App<FN, F>,
        val g: App<FN, G>,
        val h: App<FN, H>,
        val i: App<FN, I>,
        val j: App<FN, J>,
        val k: App<FN, K>,
        val l: App<FN, L>,
        val m: App<FN, M>,
        val n: App<FN, N>,
        val o: App<FN, O>
    ) {

        public fun <R> apply(instance: Applicative<FN, *>, function: Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, R>): App<FN, R> =
            apply(instance, instance.point(function))

        public fun <R> apply(
            instance: Applicative<FN, *>,
            function: App<FN, Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, R>>
        ): App<FN, R> = instance.ap15(function, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)
    }

    @JvmRecord
    public data class P16<FN : K1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>(
        val a: App<FN, A>,
        val b: App<FN, B>,
        val c: App<FN, C>,
        val d: App<FN, D>,
        val e: App<FN, E>,
        val f: App<FN, F>,
        val g: App<FN, G>,
        val h: App<FN, H>,
        val i: App<FN, I>,
        val j: App<FN, J>,
        val k: App<FN, K>,
        val l: App<FN, L>,
        val m: App<FN, M>,
        val n: App<FN, N>,
        val o: App<FN, O>,
        val p: App<FN, P>
    ) {

        public fun <R> apply(instance: Applicative<FN, *>, function: Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, R>): App<FN, R> =
            apply(instance, instance.point(function))

        public fun <R> apply(
            instance: Applicative<FN, *>,
            function: App<FN, Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, R>>
        ): App<FN, R> = instance.ap16(function, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)
    }
}
