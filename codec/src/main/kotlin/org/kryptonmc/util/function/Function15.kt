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

public fun interface Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, R> {

    public fun apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O): R

    public fun curry(): Function<A, Function14<B, C, D, E, F, G, H, I, J, K, L, M, N, O, R>> =
        Function { a -> Function14 { b, c, d, e, f, g, h, i, j, k, l, m, n, o -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) } }

    public fun curry2(): BiFunction<A, B, Function13<C, D, E, F, G, H, I, J, K, L, M, N, O, R>> =
        BiFunction { a, b -> Function13 { c, d, e, f, g, h, i, j, k, l, m, n, o -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) } }

    public fun curry3(): Function3<A, B, C, Function12<D, E, F, G, H, I, J, K, L, M, N, O, R>> =
        Function3 { a, b, c -> Function12 { d, e, f, g, h, i, j, k, l, m, n, o -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) } }

    public fun curry4(): Function4<A, B, C, D, Function11<E, F, G, H, I, J, K, L, M, N, O, R>> =
        Function4 { a, b, c, d -> Function11 { e, f, g, h, i, j, k, l, m, n, o -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) } }

    public fun curry5(): Function5<A, B, C, D, E, Function10<F, G, H, I, J, K, L, M, N, O, R>> =
        Function5 { a, b, c, d, e -> Function10 { f, g, h, i, j, k, l, m, n, o -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) } }

    public fun curry6(): Function6<A, B, C, D, E, F, Function9<G, H, I, J, K, L, M, N, O, R>> =
        Function6 { a, b, c, d, e, f -> Function9 { g, h, i, j, k, l, m, n, o -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) } }

    public fun curry7(): Function7<A, B, C, D, E, F, G, Function8<H, I, J, K, L, M, N, O, R>> =
        Function7 { a, b, c, d, e, f, g -> Function8 { h, i, j, k, l, m, n, o -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) } }

    public fun curry8(): Function8<A, B, C, D, E, F, G, H, Function7<I, J, K, L, M, N, O, R>> =
        Function8 { a, b, c, d, e, f, g, h -> Function7 { i, j, k, l, m, n, o -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) } }

    public fun curry9(): Function9<A, B, C, D, E, F, G, H, I, Function6<J, K, L, M, N, O, R>> =
        Function9 { a, b, c, d, e, f, g, h, i -> Function6 { j, k, l, m, n, o -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) } }

    public fun curry10(): Function10<A, B, C, D, E, F, G, H, I, J, Function5<K, L, M, N, O, R>> =
        Function10 { a, b, c, d, e, f, g, h, i, j -> Function5 { k, l, m, n, o -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) } }

    public fun curry11(): Function11<A, B, C, D, E, F, G, H, I, J, K, Function4<L, M, N, O, R>> =
        Function11 { a, b, c, d, e, f, g, h, i, j, k -> Function4 { l, m, n, o -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) } }

    public fun curry12(): Function12<A, B, C, D, E, F, G, H, I, J, K, L, Function3<M, N, O, R>> =
        Function12 { a, b, c, d, e, f, g, h, i, j, k, l -> Function3 { m, n, o -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) } }

    public fun curry13(): Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, BiFunction<N, O, R>> =
        Function13 { a, b, c, d, e, f, g, h, i, j, k, l, m -> BiFunction { n, o -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) } }

    public fun curry14(): Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, Function<O, R>> =
        Function14 { a, b, c, d, e, f, g, h, i, j, k, l, m, n -> Function { o -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) } }
}
