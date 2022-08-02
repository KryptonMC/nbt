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

public fun interface Function6<A, B, C, D, E, F, R> {

    public fun apply(a: A, b: B, c: C, d: D, e: E, f: F): R

    public fun curry(): Function<A, Function5<B, C, D, E, F, R>> =
        Function { a -> Function5 { b, c, d, e, f -> apply(a, b, c, d, e, f) } }

    public fun curry2(): BiFunction<A, B, Function4<C, D, E, F, R>> =
        BiFunction { a, b -> Function4 { c, d, e, f -> apply(a, b, c, d, e, f) } }

    public fun curry3(): Function3<A, B, C, Function3<D, E, F, R>> =
        Function3 { a, b, c -> Function3 { d, e, f -> apply(a, b, c, d, e, f) } }

    public fun curry4(): Function4<A, B, C, D, BiFunction<E, F, R>> =
        Function4 { a, b, c, d -> BiFunction { e, f -> apply(a, b, c, d, e, f) } }

    public fun curry5(): Function5<A, B, C, D, E, Function<F, R>> =
        Function5 { a, b, c, d, e -> Function { f -> apply(a, b, c, d, e, f) } }
}
