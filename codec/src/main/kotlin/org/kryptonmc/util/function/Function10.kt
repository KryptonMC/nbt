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

public fun interface Function10<A, B, C, D, E, F, G, H, I, J, R> {

    public fun apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J): R

    public fun curry(): java.util.function.Function<A, Function9<B, C, D, E, F, G, H, I, J, R>> =
        Function { a -> Function9 { b, c, d, e, f, g, h, i, j -> apply(a, b, c, d, e, f, g, h, i, j) } }

    public fun curry2(): BiFunction<A, B, Function8<C, D, E, F, G, H, I, J, R>> =
        BiFunction { a, b -> Function8 { c, d, e, f, g, h, i, j -> apply(a, b, c, d, e, f, g, h, i, j) } }

    public fun curry3(): Function3<A, B, C, Function7<D, E, F, G, H, I, J, R>> =
        Function3 { a, b, c -> Function7 { d, e, f, g, h, i, j -> apply(a, b, c, d, e, f, g, h, i, j) } }

    public fun curry4(): Function4<A, B, C, D, Function6<E, F, G, H, I, J, R>> =
        Function4 { a, b, c, d -> Function6 { e, f, g, h, i, j -> apply(a, b, c, d, e, f, g, h, i, j) } }

    public fun curry5(): Function5<A, B, C, D, E, Function5<F, G, H, I, J, R>> =
        Function5 { a, b, c, d, e -> Function5 { f, g, h, i, j -> apply(a, b, c, d, e, f, g, h, i, j) } }

    public fun curry6(): Function6<A, B, C, D, E, F, Function4<G, H, I, J, R>> =
        Function6 { a, b, c, d, e, f -> Function4 { g, h, i, j -> apply(a, b, c, d, e, f, g, h, i, j) } }

    public fun curry7(): Function7<A, B, C, D, E, F, G, Function3<H, I, J, R>> =
        Function7 { a, b, c, d, e, f, g -> Function3 { h, i, j -> apply(a, b, c, d, e, f, g, h, i, j) } }

    public fun curry8(): Function8<A, B, C, D, E, F, G, H, BiFunction<I, J, R>> =
        Function8 { a, b, c, d, e, f, g, h -> BiFunction { i, j -> apply(a, b, c, d, e, f, g, h, i, j) } }

    public fun curry9(): Function9<A, B, C, D, E, F, G, H, I, Function<J, R>> =
        Function9 { a, b, c, d, e, f, g, h, i -> Function { j -> apply(a, b, c, d, e, f, g, h, i, j) } }
}
