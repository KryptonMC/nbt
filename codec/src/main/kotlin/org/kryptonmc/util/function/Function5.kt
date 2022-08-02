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

public fun interface Function5<A, B, C, D, E, R> {

    public fun apply(a: A, b: B, c: C, d: D, e: E): R

    public fun curry(): Function<A, Function4<B, C, D, E, R>> = Function { a -> Function4 { b, c, d, e -> apply(a, b, c, d, e) } }

    public fun curry2(): BiFunction<A, B, Function3<C, D, E, R>> = BiFunction { a, b -> Function3 { c, d, e -> apply(a, b, c, d, e) } }

    public fun curry3(): Function3<A, B, C, BiFunction<D, E, R>> = Function3 { a, b, c -> BiFunction { d, e -> apply(a, b, c, d, e) } }

    public fun curry4(): Function4<A, B, C, D, Function<E, R>> = Function4 { a, b, c, d -> Function { e -> apply(a, b, c, d, e) } }
}
