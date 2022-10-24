/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt;

import java.util.List;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

sealed interface ListCollectionTag<T extends @NotNull Tag> extends CollectionTag<@NotNull T>, List<@NotNull T>
        permits AbstractListCollectionTag, ByteArrayTag, IntArrayTag, LongArrayTag {

    @Override
    boolean isEmpty();

    @Override
    @NotNull Stream<@NotNull T> stream();
}
