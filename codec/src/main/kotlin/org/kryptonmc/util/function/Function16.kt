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

public fun interface Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, R> {

    public fun apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P): R

    public fun curry(): Function<A, Function15<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, R>> =
        Function { a -> Function15 { b, c, d, e, f, g, h, i, j, k, l, m, n, o, p -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) } }

    public fun curry2(): BiFunction<A, B, Function14<C, D, E, F, G, H, I, J, K, L, M, N, O, P, R>> =
        BiFunction { a, b -> Function14 { c, d, e, f, g, h, i, j, k, l, m, n, o, p -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) } }

    public fun curry3(): Function3<A, B, C, Function13<D, E, F, G, H, I, J, K, L, M, N, O, P, R>> =
        Function3 { a, b, c -> Function13 { d, e, f, g, h, i, j, k, l, m, n, o, p -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) } }

    public fun curry4(): Function4<A, B, C, D, Function12<E, F, G, H, I, J, K, L, M, N, O, P, R>> =
        Function4 { a, b, c, d -> Function12 { e, f, g, h, i, j, k, l, m, n, o, p -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) } }

    public fun curry5(): Function5<A, B, C, D, E, Function11<F, G, H, I, J, K, L, M, N, O, P, R>> =
        Function5 { a, b, c, d, e -> Function11 { f, g, h, i, j, k, l, m, n, o, p -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) } }

    public fun curry6(): Function6<A, B, C, D, E, F, Function10<G, H, I, J, K, L, M, N, O, P, R>> =
        Function6 { a, b, c, d, e, f -> Function10 { g, h, i, j, k, l, m, n, o, p -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) } }

    public fun curry7(): Function7<A, B, C, D, E, F, G, Function9<H, I, J, K, L, M, N, O, P, R>> =
        Function7 { a, b, c, d, e, f, g -> Function9 { h, i, j, k, l, m, n, o, p -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) } }

    public fun curry8(): Function8<A, B, C, D, E, F, G, H, Function8<I, J, K, L, M, N, O, P, R>> =
        Function8 { a, b, c, d, e, f, g, h -> Function8 { i, j, k, l, m, n, o, p -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) } }

    public fun curry9(): Function9<A, B, C, D, E, F, G, H, I, Function7<J, K, L, M, N, O, P, R>> =
        Function9 { a, b, c, d, e, f, g, h, i -> Function7 { j, k, l, m, n, o, p -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) } }

    public fun curry10(): Function10<A, B, C, D, E, F, G, H, I, J, Function6<K, L, M, N, O, P, R>> =
        Function10 { a, b, c, d, e, f, g, h, i, j -> Function6 { k, l, m, n, o, p -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) } }

    public fun curry11(): Function11<A, B, C, D, E, F, G, H, I, J, K, Function5<L, M, N, O, P, R>> =
        Function11 { a, b, c, d, e, f, g, h, i, j, k -> Function5 { l, m, n, o, p -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) } }

    public fun curry12(): Function12<A, B, C, D, E, F, G, H, I, J, K, L, Function4<M, N, O, P, R>> =
        Function12 { a, b, c, d, e, f, g, h, i, j, k, l -> Function4 { m, n, o, p -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) } }

    public fun curry13(): Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, Function3<N, O, P, R>> =
        Function13 { a, b, c, d, e, f, g, h, i, j, k, l, m -> Function3 { n, o, p -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) } }

    public fun curry14(): Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, BiFunction<O, P, R>> =
        Function14 { a, b, c, d, e, f, g, h, i, j, k, l, m, n -> BiFunction { o, p -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) } }

    public fun curry15(): Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Function<P, R>> =
        Function15 { a, b, c, d, e, f, g, h, i, j, k, l, m, n, o -> Function { p -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) } }
}
