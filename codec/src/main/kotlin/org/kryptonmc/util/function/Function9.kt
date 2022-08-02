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

public fun interface Function9<A, B, C, D, E, F, G, H, I, R> {

    public fun apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I): R

    public fun curry(): Function<A, Function8<B, C, D, E, F, G, H, I, R>> =
        Function { a -> Function8 { b, c, d, e, f, g, h, i -> apply(a, b, c, d, e, f, g, h, i) } }

    public fun curry2(): BiFunction<A, B, Function7<C, D, E, F, G, H, I, R>> =
        BiFunction { a, b -> Function7 { c, d, e, f, g, h, i -> apply(a, b, c, d, e, f, g, h, i) } }

    public fun curry3(): Function3<A, B, C, Function6<D, E, F, G, H, I, R>> =
        Function3 { a, b, c -> Function6 { d, e, f, g, h, i -> apply(a, b, c, d, e, f, g, h, i) } }

    public fun curry4(): Function4<A, B, C, D, Function5<E, F, G, H, I, R>> =
        Function4 { a, b, c, d -> Function5 { e, f, g, h, i -> apply(a, b, c, d, e, f, g, h, i) } }

    public fun curry5(): Function5<A, B, C, D, E, Function4<F, G, H, I, R>> =
        Function5 { a, b, c, d, e -> Function4 { f, g, h, i -> apply(a, b, c, d, e, f, g, h, i) } }

    public fun curry6(): Function6<A, B, C, D, E, F, Function3<G, H, I, R>> =
        Function6 { a, b, c, d, e, f -> Function3 { g, h, i -> apply(a, b, c, d, e, f, g, h, i) } }

    public fun curry7(): Function7<A, B, C, D, E, F, G, BiFunction<H, I, R>> =
        Function7 { a, b, c, d, e, f, g -> BiFunction { h, i -> apply(a, b, c, d, e, f, g, h, i) } }

    public fun curry8(): Function8<A, B, C, D, E, F, G, H, Function<I, R>> =
        Function8 { a, b, c, d, e, f, g, h -> Function { i -> apply(a, b, c, d, e, f, g, h, i) } }
}
