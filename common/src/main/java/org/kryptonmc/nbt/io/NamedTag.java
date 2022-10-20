/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.io;

import org.jetbrains.annotations.NotNull;
import org.kryptonmc.nbt.Tag;

/**
 * A holder for a tag with a name.
 *
 * @param name the name of the tag
 * @param tag the tag
 */
public record NamedTag(@NotNull String name, @NotNull Tag tag) {
}
