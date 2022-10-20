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

/**
 * A visitor that can be used to visit tags.
 *
 * @param <T> the type of the result
 */
public interface TagVisitor<T> {

    /**
     * Visits the given tag and returns the completed result of the visit.
     *
     * @param tag the tag to visit
     * @return the result of the visit
     */
    @NotNull T visit(final @NotNull Tag tag);

    /**
     * Visits the end tag.
     *
     * @param tag the tag to visit
     */
    void visitEnd(final @NotNull EndTag tag);

    /**
     * Visits the given byte tag.
     *
     * @param tag the tag to visit
     */
    void visitByte(final @NotNull ByteTag tag);

    /**
     * Visits the given short tag.
     *
     * @param tag the tag to visit
     */
    void visitShort(final @NotNull ShortTag tag);

    /**
     * Visits the given integer tag.
     *
     * @param tag the tag to visit
     */
    void visitInt(final @NotNull IntTag tag);

    /**
     * Visits the given long tag.
     *
     * @param tag the tag to visit
     */
    void visitLong(final @NotNull LongTag tag);

    /**
     * Visits the given float tag.
     *
     * @param tag the tag to visit
     */
    void visitFloat(final @NotNull FloatTag tag);

    /**
     * Visits the given double tag.
     *
     * @param tag the tag to visit
     */
    void visitDouble(final @NotNull DoubleTag tag);

    /**
     * Visits the given string tag.
     *
     * @param tag the tag to visit
     */
    void visitString(final @NotNull StringTag tag);

    /**
     * Visits the given byte array tag.
     *
     * @param tag the tag to visit
     */
    void visitByteArray(final @NotNull ByteArrayTag tag);

    /**
     * Visits the given integer array tag.
     *
     * @param tag the tag to visit
     */
    void visitIntArray(final @NotNull IntArrayTag tag);

    /**
     * Visits the given long array tag.
     *
     * @param tag the tag to visit
     */
    void visitLongArray(final @NotNull LongArrayTag tag);

    /**
     * Visits the given list tag.
     *
     * @param tag the tag to visit
     */
    void visitList(final @NotNull ListTag tag);

    /**
     * Visits the given compound tag.
     *
     * @param tag the tag to visit
     */
    void visitCompound(final @NotNull CompoundTag tag);
}
