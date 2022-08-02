/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.util.functional

/**
 * This may look like a mess and make no sense, but what it allows us to di is
 * incredible. When we're building new compound codecs, we can call these group
 * functions and allow us to determine the correct applicable function with the
 * right amount of parameters, all of the correct type, when we want to call
 * the constructor.
 */
public interface Kind1<FN : K1, Mu : Kind1.Mu> {

    public fun <A> group(a: App<FN, A>): Products.P1<FN, A> = Products.P1(a)

    public fun <A, B> group(a: App<FN, A>, b: App<FN, B>): Products.P2<FN, A, B> = Products.P2(a, b)

    public fun <A, B, C> group(a: App<FN, A>, b: App<FN, B>, c: App<FN, C>): Products.P3<FN, A, B, C> = Products.P3(a, b, c)

    public fun <A, B, C, D> group(a: App<FN, A>, b: App<FN, B>, c: App<FN, C>, d: App<FN, D>): Products.P4<FN, A, B, C, D> = Products.P4(a, b, c, d)

    public fun <A, B, C, D, E> group(a: App<FN, A>, b: App<FN, B>, c: App<FN, C>, d: App<FN, D>, e: App<FN, E>): Products.P5<FN, A, B, C, D, E> =
        Products.P5(a, b, c, d, e)

    public fun <A, B, C, D, E, F> group(
        a: App<FN, A>,
        b: App<FN, B>,
        c: App<FN, C>,
        d: App<FN, D>,
        e: App<FN, E>,
        f: App<FN, F>
    ): Products.P6<FN, A, B, C, D, E, F> = Products.P6(a, b, c, d, e, f)

    public fun <A, B, C, D, E, F, G> group(
        a: App<FN, A>,
        b: App<FN, B>,
        c: App<FN, C>,
        d: App<FN, D>,
        e: App<FN, E>,
        f: App<FN, F>,
        g: App<FN, G>
    ): Products.P7<FN, A, B, C, D, E, F, G> = Products.P7(a, b, c, d, e, f, g)

    public fun <A, B, C, D, E, F, G, H> group(
        a: App<FN, A>,
        b: App<FN, B>,
        c: App<FN, C>,
        d: App<FN, D>,
        e: App<FN, E>,
        f: App<FN, F>,
        g: App<FN, G>,
        h: App<FN, H>
    ): Products.P8<FN, A, B, C, D, E, F, G, H> = Products.P8(a, b, c, d, e, f, g, h)

    public fun <A, B, C, D, E, F, G, H, I> group(
        a: App<FN, A>,
        b: App<FN, B>,
        c: App<FN, C>,
        d: App<FN, D>,
        e: App<FN, E>,
        f: App<FN, F>,
        g: App<FN, G>,
        h: App<FN, H>,
        i: App<FN, I>
    ): Products.P9<FN, A, B, C, D, E, F, G, H, I> = Products.P9(a, b, c, d, e, f, g, h, i)

    public fun <A, B, C, D, E, F, G, H, I, J> group(
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
    ): Products.P10<FN, A, B, C, D, E, F, G, H, I, J> = Products.P10(a, b, c, d, e, f, g, h, i, j)

    public fun <A, B, C, D, E, F, G, H, I, J, K> group(
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
    ): Products.P11<FN, A, B, C, D, E, F, G, H, I, J, K> = Products.P11(a, b, c, d, e, f, g, h, i, j, k)

    public fun <A, B, C, D, E, F, G, H, I, J, K, L> group(
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
    ): Products.P12<FN, A, B, C, D, E, F, G, H, I, J, K, L> = Products.P12(a, b, c, d, e, f, g, h, i, j, k, l)

    public fun <A, B, C, D, E, F, G, H, I, J, K, L, M> group(
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
    ): Products.P13<FN, A, B, C, D, E, F, G, H, I, J, K, L, M> = Products.P13(a, b, c, d, e, f, g, h, i, j, k, l, m)

    public fun <A, B, C, D, E, F, G, H, I, J, K, L, M, N> group(
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
    ): Products.P14<FN, A, B, C, D, E, F, G, H, I, J, K, L, M, N> = Products.P14(a, b, c, d, e, f, g, h, i, j, k, l, m, n)

    public fun <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> group(
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
    ): Products.P15<FN, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> = Products.P15(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)

    public fun <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> group(
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
    ): Products.P16<FN, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> = Products.P16(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)

    public interface Mu : K1

    public companion object {

        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        public fun <F : K1, Proof : Mu> unbox(proofBox: App<Proof, F>): Kind1<F, Proof> = proofBox as Kind1<F, Proof>
    }
}
