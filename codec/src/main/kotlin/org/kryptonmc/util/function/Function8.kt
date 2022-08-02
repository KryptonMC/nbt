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

public fun interface Function8<A, B, C, D, E, F, G, H, R> {

    public fun apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H): R

    public fun curry(): Function<A, Function7<B, C, D, E, F, G, H, R>> =
        Function { a -> Function7 { b, c, d, e, f, g, h -> apply(a, b, c, d, e, f, g, h) } }

    public fun curry2(): BiFunction<A, B, Function6<C, D, E, F, G, H, R>> =
        BiFunction { a, b -> Function6 { c, d, e, f, g, h -> apply(a, b, c, d, e, f, g, h) } }

    public fun curry3(): Function3<A, B, C, Function5<D, E, F, G, H, R>> =
        Function3 { a, b, c -> Function5 { d, e, f, g, h -> apply(a, b, c, d, e, f, g, h) } }

    public fun curry4(): Function4<A, B, C, D, Function4<E, F, G, H, R>> =
        Function4 { a, b, c, d -> Function4 { e, f, g, h -> apply(a, b, c, d, e, f, g, h) } }

    public fun curry5(): Function5<A, B, C, D, E, Function3<F, G, H, R>> =
        Function5 { a, b, c, d, e -> Function3 { f, g, h -> apply(a, b, c, d, e, f, g, h) } }

    public fun curry6(): Function6<A, B, C, D, E, F, BiFunction<G, H, R>> =
        Function6 { a, b, c, d, e, f -> BiFunction { g, h -> apply(a, b, c, d, e, f, g, h) } }

    public fun curry7(): Function7<A, B, C, D, E, F, G, Function<H, R>> =
        Function7 { a, b, c, d, e, f, g -> Function { h -> apply(a, b, c, d, e, f, g, h) } }
}
