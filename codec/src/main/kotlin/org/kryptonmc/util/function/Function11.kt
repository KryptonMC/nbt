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

public fun interface Function11<A, B, C, D, E, F, G, H, I, J, K, R> {

    public fun apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K): R

    public fun curry(): Function<A, Function10<B, C, D, E, F, G, H, I, J, K, R>> =
        Function { a -> Function10 { b, c, d, e, f, g, h, i, j, k -> apply(a, b, c, d, e, f, g, h, i, j, k) } }

    public fun curry2(): BiFunction<A, B, Function9<C, D, E, F, G, H, I, J, K, R>> =
        BiFunction { a, b -> Function9 { c, d, e, f, g, h, i, j, k -> apply(a, b, c, d, e, f, g, h, i, j, k) } }

    public fun curry3(): Function3<A, B, C, Function8<D, E, F, G, H, I, J, K, R>> =
        Function3 { a, b, c -> Function8 { d, e, f, g, h, i, j, k -> apply(a, b, c, d, e, f, g, h, i, j, k) } }

    public fun curry4(): Function4<A, B, C, D, Function7<E, F, G, H, I, J, K, R>> =
        Function4 { a, b, c, d -> Function7 { e, f, g, h, i, j, k -> apply(a, b, c, d, e, f, g, h, i, j, k) } }

    public fun curry5(): Function5<A, B, C, D, E, Function6<F, G, H, I, J, K, R>> =
        Function5 { a, b, c, d, e -> Function6 { f, g, h, i, j, k -> apply(a, b, c, d, e, f, g, h, i, j, k) } }

    public fun curry6(): Function6<A, B, C, D, E, F, Function5<G, H, I, J, K, R>> =
        Function6 { a, b, c, d, e, f -> Function5 { g, h, i, j, k -> apply(a, b, c, d, e, f, g, h, i, j, k) } }

    public fun curry7(): Function7<A, B, C, D, E, F, G, Function4<H, I, J, K, R>> =
        Function7 { a, b, c, d, e, f, g -> Function4 { h, i, j, k -> apply(a, b, c, d, e, f, g, h, i, j, k) } }

    public fun curry8(): Function8<A, B, C, D, E, F, G, H, Function3<I, J, K, R>> =
        Function8 { a, b, c, d, e, f, g, h -> Function3 { i, j, k -> apply(a, b, c, d, e, f, g, h, i, j, k) } }

    public fun curry9(): Function9<A, B, C, D, E, F, G, H, I, BiFunction<J, K, R>> =
        Function9 { a, b, c, d, e, f, g, h, i -> BiFunction { j, k -> apply(a, b, c, d, e, f, g, h, i, j, k) } }

    public fun curry10(): Function10<A, B, C, D, E, F, G, H, I, J, Function<K, R>> =
        Function10 { a, b, c, d, e, f, g, h, i, j -> Function { k -> apply(a, b, c, d, e, f, g, h, i, j, k) } }
}
