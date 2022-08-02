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

public fun interface Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, R> {

    public fun apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M): R

    public fun curry(): Function<A, Function12<B, C, D, E, F, G, H, I, J, K, L, M, R>> =
        Function { a -> Function12 { b, c, d, e, f, g, h, i, j, k, l, m -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m) } }

    public fun curry2(): BiFunction<A, B, Function11<C, D, E, F, G, H, I, J, K, L, M, R>> =
        BiFunction { a, b -> Function11 { c, d, e, f, g, h, i, j, k, l, m -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m) } }

    public fun curry3(): Function3<A, B, C, Function10<D, E, F, G, H, I, J, K, L, M, R>> =
        Function3 { a, b, c -> Function10 { d, e, f, g, h, i, j, k, l, m -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m) } }

    public fun curry4(): Function4<A, B, C, D, Function9<E, F, G, H, I, J, K, L, M, R>> =
        Function4 { a, b, c, d -> Function9 { e, f, g, h, i, j, k, l, m -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m) } }

    public fun curry5(): Function5<A, B, C, D, E, Function8<F, G, H, I, J, K, L, M, R>> =
        Function5 { a, b, c, d, e -> Function8 { f, g, h, i, j, k, l, m -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m) } }

    public fun curry6(): Function6<A, B, C, D, E, F, Function7<G, H, I, J, K, L, M, R>> =
        Function6 { a, b, c, d, e, f -> Function7 { g, h, i, j, k, l, m -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m) } }

    public fun curry7(): Function7<A, B, C, D, E, F, G, Function6<H, I, J, K, L, M, R>> =
        Function7 { a, b, c, d, e, f, g -> Function6 { h, i, j, k, l, m -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m) } }

    public fun curry8(): Function8<A, B, C, D, E, F, G, H, Function5<I, J, K, L, M, R>> =
        Function8 { a, b, c, d, e, f, g, h -> Function5 { i, j, k, l, m -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m) } }

    public fun curry9(): Function9<A, B, C, D, E, F, G, H, I, Function4<J, K, L, M, R>> =
        Function9 { a, b, c, d, e, f, g, h, i -> Function4 { j, k, l, m -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m) } }

    public fun curry10(): Function10<A, B, C, D, E, F, G, H, I, J, Function3<K, L, M, R>> =
        Function10 { a, b, c, d, e, f, g, h, i, j -> Function3 { k, l, m -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m) } }

    public fun curry11(): Function11<A, B, C, D, E, F, G, H, I, J, K, BiFunction<L, M, R>> =
        Function11 { a, b, c, d, e, f, g, h, i, j, k -> BiFunction { l, m -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m) } }

    public fun curry12(): Function12<A, B, C, D, E, F, G, H, I, J, K, L, Function<M, R>> =
        Function12 { a, b, c, d, e, f, g, h, i, j, k, l -> Function { m -> apply(a, b, c, d, e, f, g, h, i, j, k, l, m) } }
}
