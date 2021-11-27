/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

/**
 * An examiner that can be used to examine tags.
 *
 * This follows the visitor pattern.
 */
public interface TagExaminer<T> {

    /**
     * Examines the given [tag] and returns the completed result of the
     * examination.
     *
     * @param tag the tag to examine
     * @return the result of the examination
     */
    public fun examine(tag: Tag): T

    /**
     * Examines the end tag.
     *
     * @param tag the tag to examine
     */
    public fun examineEnd(tag: EndTag)

    /**
     * Examines the given byte [tag].
     *
     * @param tag the tag to examine
     */
    public fun examineByte(tag: ByteTag)

    /**
     * Examines the given short [tag].
     *
     * @param tag the tag to examine
     */
    public fun examineShort(tag: ShortTag)

    /**
     * Examines the given integer [tag].
     *
     * @param tag the tag to examine
     */
    public fun examineInt(tag: IntTag)

    /**
     * Examines the given long [tag].
     *
     * @param tag the tag to examine
     */
    public fun examineLong(tag: LongTag)

    /**
     * Examines the given float [tag].
     *
     * @param tag the tag to examine
     */
    public fun examineFloat(tag: FloatTag)

    /**
     * Examines the given double [tag].
     *
     * @param tag the tag to examine
     */
    public fun examineDouble(tag: DoubleTag)

    /**
     * Examines the given byte array [tag].
     *
     * @param tag the tag to examine
     */
    public fun examineByteArray(tag: ByteArrayTag)

    /**
     * Examines the given string [tag].
     *
     * @param tag the tag to examine
     */
    public fun examineString(tag: StringTag)

    /**
     * Examines the given list [tag].
     *
     * @param tag the tag to examine
     */
    public fun examineList(tag: ListTag)

    /**
     * Examines the given compound [tag].
     *
     * @param tag the tag to examine
     */
    public fun examineCompound(tag: CompoundTag)

    /**
     * Examines the given integer array [tag].
     *
     * @param tag the tag to examine
     */
    public fun examineIntArray(tag: IntArrayTag)

    /**
     * Examines the given long array [tag].
     *
     * @param tag the tag to examine
     */
    public fun examineLongArray(tag: LongArrayTag)
}
