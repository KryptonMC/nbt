/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.util.function

import java.util.function.BiFunction
import java.util.function.Function

public fun interface Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, R> {

    public fun apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N): R

    public fun curry(): Function<A, Function13<B, C, D, E, F, G, H, I, J, K, L, M, N, R>> =
        Function { a -> Function13 { b, c, d, e, f, g, h, i, j, k, l, m, n -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n) } }

    public fun curry2(): BiFunction<A, B, Function12<C, D, E, F, G, H, I, J, K, L, M, N, R>> =
        BiFunction { a, b -> Function12 { c, d, e, f, g, h, i, j, k, l, m, n -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n) } }

    public fun curry3(): Function3<A, B, C, Function11<D, E, F, G, H, I, J, K, L, M, N, R>> =
        Function3 { a, b, c -> Function11 { d, e, f, g, h, i, j, k, l, m, n -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n) } }

    public fun curry4(): Function4<A, B, C, D, Function10<E, F, G, H, I, J, K, L, M, N, R>> =
        Function4 { a, b, c, d -> Function10 { e, f, g, h, i, j, k, l, m, n -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n) } }

    public fun curry5(): Function5<A, B, C, D, E, Function9<F, G, H, I, J, K, L, M, N, R>> =
        Function5 { a, b, c, d, e -> Function9 { f, g, h, i, j, k, l, m, n -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n) } }

    public fun curry6(): Function6<A, B, C, D, E, F, Function8<G, H, I, J, K, L, M, N, R>> =
        Function6 { a, b, c, d, e, f -> Function8 { g, h, i, j, k, l, m, n -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n) } }

    public fun curry7(): Function7<A, B, C, D, E, F, G, Function7<H, I, J, K, L, M, N, R>> =
        Function7 { a, b, c, d, e, f, g -> Function7 { h, i, j, k, l, m, n -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n) } }

    public fun curry8(): Function8<A, B, C, D, E, F, G, H, Function6<I, J, K, L, M, N, R>> =
        Function8 { a, b, c, d, e, f, g, h -> Function6 { i, j, k, l, m, n -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n) } }

    public fun curry9(): Function9<A, B, C, D, E, F, G, H, I, Function5<J, K, L, M, N, R>> =
        Function9 { a, b, c, d, e, f, g, h, i -> Function5 { j, k, l, m, n -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n) } }

    public fun curry10(): Function10<A, B, C, D, E, F, G, H, I, J, Function4<K, L, M, N, R>> =
        Function10 { a, b, c, d, e, f, g, h, i, j -> Function4 { k, l, m, n -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n) } }

    public fun curry11(): Function11<A, B, C, D, E, F, G, H, I, J, K, Function3<L, M, N, R>> =
        Function11 { a, b, c, d, e, f, g, h, i, j, k -> Function3 { l, m, n -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n) } }

    public fun curry12(): Function12<A, B, C, D, E, F, G, H, I, J, K, L, BiFunction<M, N, R>> =
        Function12 { a, b, c, d, e, f, g, h, i, j, k, l -> BiFunction { m, n -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n) } }

    public fun curry13(): Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, Function<N, R>> =
        Function13 { a, b, c, d, e, f, g, h, i, j, k, l, m -> Function { n -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n) } }
}
