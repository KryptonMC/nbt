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

public fun interface Function3<A, B, C, R> {

    public fun apply(a: A, b: B, c: C): R

    public fun curry(): Function<A, BiFunction<B, C, R>> = Function { a -> BiFunction { b, c -> apply(a, b, c) } }

    public fun curry2(): BiFunction<A, B, Function<C, R>> = BiFunction { a, b -> Function { c -> apply(a, b, c) } }
}
