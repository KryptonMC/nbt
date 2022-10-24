/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt;

import java.util.AbstractList;
import org.jetbrains.annotations.NotNull;

abstract non-sealed class AbstractListCollectionTag<T extends @NotNull Tag> extends AbstractList<@NotNull T> implements ListCollectionTag<@NotNull T> {

    @Override
    public final boolean tryAdd(final int index, final @NotNull T tag) {
        add(index, tag);
        return true;
    }

    @Override
    public final boolean tryAdd(final @NotNull T tag) {
        return add(tag);
    }
}
