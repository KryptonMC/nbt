/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.util;

/**
 * A primitive specialization of {@link java.util.function.BiConsumer} for
 * an object and a short.
 */
@FunctionalInterface
public interface ObjShortConsumer<T> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t the input object
     * @param s the input short
     */
    void accept(final T t, final short s);
}
