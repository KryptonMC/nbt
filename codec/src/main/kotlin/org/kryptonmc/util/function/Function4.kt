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

public fun interface Function4<A, B, C, D, R> {

    public fun apply(a: A, b: B, c: C, d: D): R

    public fun curry(): Function<A, Function3<B, C, D, R>> = Function { a -> Function3 { b, c, d -> apply(a, b, c, d) } }

    public fun curry2(): BiFunction<A, B, BiFunction<C, D, R>> = BiFunction { a, b -> BiFunction { c, d -> apply(a, b, c, d) } }

    public fun curry3(): Function3<A, B, C, Function<D, R>> = Function3 { a, b, c -> Function { d -> apply(a, b, c, d) } }
}
