/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.util;

import org.jetbrains.annotations.NotNull;
import org.kryptonmc.nbt.ByteArrayTag;
import org.kryptonmc.nbt.ByteTag;
import org.kryptonmc.nbt.CompoundTag;
import org.kryptonmc.nbt.DoubleTag;
import org.kryptonmc.nbt.EndTag;
import org.kryptonmc.nbt.FloatTag;
import org.kryptonmc.nbt.IntArrayTag;
import org.kryptonmc.nbt.IntTag;
import org.kryptonmc.nbt.ListTag;
import org.kryptonmc.nbt.LongArrayTag;
import org.kryptonmc.nbt.LongTag;
import org.kryptonmc.nbt.ShortTag;
import org.kryptonmc.nbt.StringTag;
import org.kryptonmc.nbt.Tag;
import org.kryptonmc.nbt.TagType;

/**
 * A utility that provides a mapping between the integer tag types (IDs) and
 * the tag type objects ({@link TagType}).
 */
public final class Types {

    private static final TagType<? extends Tag>[] TYPES = new TagType<?>[]{
            EndTag.TYPE,
            ByteTag.TYPE,
            ShortTag.TYPE,
            IntTag.TYPE,
            LongTag.TYPE,
            FloatTag.TYPE,
            DoubleTag.TYPE,
            ByteArrayTag.TYPE,
            StringTag.TYPE,
            ListTag.TYPE,
            CompoundTag.TYPE,
            IntArrayTag.TYPE,
            LongArrayTag.TYPE
    };

    /**
     * Gets the {@link TagType} for the given integer type ID.
     *
     * @param type the type ID
     * @return the corresponding tag type
     * @throws IllegalArgumentException if the provided type ID is not valid
     */
    public static @NotNull TagType<? extends Tag> of(final int type) {
        if (type < 0 || type >= TYPES.length) throw new IllegalArgumentException("Invalid tag type ID: " + type);
        return TYPES[type];
    }

    private Types() {
        throw new AssertionError("This class cannot be instantiated!");
    }
}
