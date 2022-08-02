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

public fun interface Function12<A, B, C, D, E, F, G, H, I, J, K, L, R> {

    public fun apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L): R

    public fun curry(): Function<A, Function11<B, C, D, E, F, G, H, I, J, K, L, R>> =
        Function { a -> Function11 { b, c, d, e, f, g, h, i, j, k, l -> apply(a, b, c, d, e, f, g, h, i, j, k, l) } }

    public fun curry2(): BiFunction<A, B, Function10<C, D, E, F, G, H, I, J, K, L, R>> =
        BiFunction { a, b -> Function10 { c, d, e, f, g, h, i, j, k, l -> apply(a, b, c, d, e, f, g, h, i, j, k, l) } }

    public fun curry3(): Function3<A, B, C, Function9<D, E, F, G, H, I, J, K, L, R>> =
        Function3 { a, b, c -> Function9 { d, e, f, g, h, i, j, k, l -> apply(a, b, c, d, e, f, g, h, i, j, k, l) } }

    public fun curry4(): Function4<A, B, C, D, Function8<E, F, G, H, I, J, K, L, R>> =
        Function4 { a, b, c, d -> Function8 { e, f, g, h, i, j, k, l -> apply(a, b, c, d, e, f, g, h, i, j, k, l) } }

    public fun curry5(): Function5<A, B, C, D, E, Function7<F, G, H, I, J, K, L, R>> =
        Function5 { a, b, c, d, e -> Function7 { f, g, h, i, j, k, l -> apply(a, b, c, d, e, f, g, h, i, j, k, l) } }

    public fun curry6(): Function6<A, B, C, D, E, F, Function6<G, H, I, J, K, L, R>> =
        Function6 { a, b, c, d, e, f -> Function6 { g, h, i, j, k, l -> apply(a, b, c, d, e, f, g, h, i, j, k, l) } }

    public fun curry7(): Function7<A, B, C, D, E, F, G, Function5<H, I, J, K, L, R>> =
        Function7 { a, b, c, d, e, f, g -> Function5 { h, i, j, k, l -> apply(a, b, c, d, e, f, g, h, i, j, k, l) } }

    public fun curry8(): Function8<A, B, C, D, E, F, G, H, Function4<I, J, K, L, R>> =
        Function8 { a, b, c, d, e, f, g, h -> Function4 { i, j, k, l -> apply(a, b, c, d, e, f, g, h, i, j, k, l) } }

    public fun curry9(): Function9<A, B, C, D, E, F, G, H, I, Function3<J, K, L, R>> =
        Function9 { a, b, c, d, e, f, g, h, i -> Function3 { j, k, l -> apply(a, b, c, d, e, f, g, h, i, j, k, l) } }

    public fun curry10(): Function10<A, B, C, D, E, F, G, H, I, J, BiFunction<K, L, R>> =
        Function10 { a, b, c, d, e, f, g, h, i, j -> BiFunction { k, l -> apply(a, b, c, d, e, f, g, h, i, j, k, l) } }

    public fun curry11(): Function11<A, B, C, D, E, F, G, H, I, J, K, Function<L, R>> =
        Function11 { a, b, c, d, e, f, g, h, i, j, k -> Function { l -> apply(a, b, c, d, e, f, g, h, i, j, k, l) } }
}
