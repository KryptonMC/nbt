/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt;

import org.jetbrains.annotations.NotNull;

non-sealed interface ScopedTag<T extends ScopedTag<T>> extends Tag {

    @Override
    @NotNull TagType<T> type();

    @Override
    @NotNull T copy();
}
