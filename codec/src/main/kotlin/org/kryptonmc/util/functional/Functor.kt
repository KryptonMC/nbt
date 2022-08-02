/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.util.functional

import java.util.function.Function

public interface Functor<F : K1, Mu : Functor.Mu> : Kind1<F, Mu> {

    public fun <T, R> map(function: Function<T, R>, ts: App<F, T>): App<F, R>

    public interface Mu : Kind1.Mu
}
