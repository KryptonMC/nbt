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

public fun interface Function7<A, B, C, D, E, F, G, R> {

    public fun apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G): R

    public fun curry(): Function6<A, B, C, D, E, F, Function<G, R>> =
        Function6 { a, b, c, d, e, f -> Function { g -> apply(a, b, c, d, e, f, g) } }

    public fun curry2(): Function5<A, B, C, D, E, BiFunction<F, G, R>> =
        Function5 { a, b, c, d, e -> BiFunction { f, g -> apply(a, b, c, d, e, f, g) } }

    public fun curry3(): Function4<A, B, C, D, Function3<E, F, G, R>> =
        Function4 { a, b, c, d -> Function3 { e, f, g -> apply(a, b, c, d, e, f, g) } }

    public fun curry4(): Function3<A, B, C, Function4<D, E, F, G, R>> =
        Function3 { a, b, c -> Function4 { d, e, f, g -> apply(a, b, c, d, e, f, g) } }

    public fun curry5(): BiFunction<A, B, Function5<C, D, E, F, G, R>> =
        BiFunction { a, b -> Function5 { c, d, e, f, g -> apply(a, b, c, d, e, f, g) } }

    public fun curry6(): Function<A, Function6<B, C, D, E, F, G, R>> =
        Function { a -> Function6 { b, c, d, e, f, g -> apply(a, b, c, d, e, f, g) } }
}
