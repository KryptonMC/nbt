/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.visitor;

import org.jetbrains.annotations.NotNull;
import org.kryptonmc.nbt.TagType;

/**
 * A tag visitor designed for reading serialized tag data.
 *
 * <p>It's called streaming because it works on a data stream, usually some
 * form of serialized data.</p>
 */
public interface StreamingTagVisitor {

    /**
     * Visits an end tag.
     *
     * @return the result
     */
    @NotNull ValueResult visitEnd();

    /**
     * Visits a string value.
     *
     * @param value the value
     * @return the result
     */
    @NotNull ValueResult visit(final @NotNull String value);

    /**
     * Visits a byte value.
     *
     * @param value the value
     * @return the result
     */
    @NotNull ValueResult visit(final byte value);

    /**
     * Visits a short value.
     *
     * @param value the value
     * @return the result
     */
    @NotNull ValueResult visit(final short value);

    /**
     * Visits an int value.
     *
     * @param value the value
     * @return the result
     */
    @NotNull ValueResult visit(final int value);

    /**
     * Visits a long value.
     *
     * @param value the value
     * @return the result
     */
    @NotNull ValueResult visit(final long value);

    /**
     * Visits a float value.
     *
     * @param value the value
     * @return the result
     */
    @NotNull ValueResult visit(final float value);

    /**
     * Visits a double value.
     *
     * @param value the value
     * @return the result
     */
    @NotNull ValueResult visit(final double value);

    /**
     * Visits a byte array value.
     *
     * @param value the value
     * @return the result
     */
    @NotNull ValueResult visit(final byte@NotNull[] value);

    /**
     * Visits an int array value.
     *
     * @param value the value
     * @return the result
     */
    @NotNull ValueResult visit(final int@NotNull[] value);

    /**
     * Visits a long array value.
     *
     * @param value the value
     * @return the result
     */
    @NotNull ValueResult visit(final long@NotNull[] value);

    /**
     * Visits the start of a list with the given type and size.
     *
     * @param type the type of the list
     * @param size the size of the list
     * @return the result
     */
    @NotNull ValueResult visitList(final @NotNull TagType<?> type, final int size);

    /**
     * Visits an element in a list with the given type at the given index.
     *
     * @param type the type of the list
     * @param index the index of the element within the list
     * @return the result
     */
    @NotNull EntryResult visitElement(final @NotNull TagType<?> type, final int index);

    /**
     * Visits an entry in a container structure with the given type.
     *
     * @param type the type of the entry
     * @return the result
     */
    @NotNull EntryResult visitEntry(final @NotNull TagType<?> type);

    /**
     * Visits an entry in a container structure with the given type and name.
     *
     * @param type the type of the entry
     * @param name the name of the entry
     * @return the result
     */
    @NotNull EntryResult visitEntry(final @NotNull TagType<?> type, final @NotNull String name);

    /**
     * Visits the root entry with the given type.
     *
     * <p>This type of entry is different from the other two above, in that
     * this refers to any type of tag at the root of a hierarchy found when
     * reading data.</p>
     *
     * @param type the type of the entry
     * @return the result
     */
    @NotNull ValueResult visitRootEntry(final @NotNull TagType<?> type);

    /**
     * Visits the end of a container.
     *
     * @return the result
     */
    @NotNull ValueResult visitContainerEnd();

    /**
     * Indicates the result of a visit to an entry.
     */
    enum EntryResult {

        /**
         * We should enter the entry in to an underlying data type.
         */
        ENTER,

        /**
         * We should skip the entry.
         */
        SKIP,

        /**
         * We should skip the entry and all other entries after it in its
         * hierarchy level.
         *
         * <p>For example, if we have a compound that contains a list, and
         * visiting one of the list elements returns this, we will skip all
         * of the list elements, but not all of the elements in the parent
         * compound.</p>
         */
        BREAK,

        /**
         * We should halt the visiting process entirely, usually due to an
         * error.
         */
        HALT
    }

    /**
     * Indicates the result of a visit to a value.
     */
    enum ValueResult {

        /**
         * We should continue reading as normal.
         */
        CONTINUE,

        /**
         * We should ignore the value and continue reading.
         */
        BREAK,

        /**
         * We should halt the visiting process entirely, usually due to an
         * error.
         */
        HALT
    }
}
