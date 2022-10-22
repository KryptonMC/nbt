/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;

final class ImmutableListTagImpl extends AbstractListTag<ImmutableListTag> implements ImmutableListTag {

    private final PSequence<Tag> data;
    private final int elementType;

    public ImmutableListTagImpl(final PSequence<Tag> data, final int elementType) {
        this.data = data;
        this.elementType = elementType;
    }

    @Override
    public int elementType() {
        return elementType;
    }

    @Override
    public @NotNull PSequence<Tag> getData() {
        return data;
    }

    @Override
    public @NotNull ImmutableListTag add(final @NotNull Tag tag) {
        if (tag.id() == EndTag.ID) addUnsupported(tag.id(), elementType());
        if (elementType() == EndTag.ID) return new ImmutableListTagImpl(data.plus(tag), tag.id());
        if (elementType() != tag.id()) addUnsupported(tag.id(), elementType());
        return new ImmutableListTagImpl(data.plus(tag), elementType());
    }

    @Override
    public boolean tryAdd(final int index, final @NotNull Tag tag) {
        // This tag is immutable, so this always returns false.
        return false;
    }

    @Override
    public boolean tryAdd(final @NotNull Tag tag) {
        // This tag is immutable, so this always returns false.
        return false;
    }

    @Override
    public @NotNull ImmutableListTag remove(final int index) {
        // Optimization: If we only have one element, we can only remove that element, which will result in an empty list,
        // therefore we return the empty list.
        if (data.isEmpty() || (data.size() == 1 && index == 0)) return (ImmutableListTag) EMPTY;
        return new ImmutableListTagImpl(data.minus(index), elementType());
    }

    @Override
    public @NotNull ImmutableListTag remove(final @NotNull Tag tag) {
        // Optimization: If we only have one element, we can only remove that element, which will result in an empty list,
        // therefore we return the empty list.
        if (data.isEmpty() || (data.size() == 1 && tag.equals(data.get(0)))) return (ImmutableListTag) EMPTY;
        return new ImmutableListTagImpl(data.minus(tag), elementType());
    }

    private static void addUnsupported(final int tagType, final int elementType) {
        throw new UnsupportedOperationException("Cannot add tag of type " + tagType + " to list of type " + elementType + "!");
    }

    @Override
    public @NotNull ImmutableListTag set(final int index, final @NotNull Tag tag) {
        return new ImmutableListTagImpl(data.with(index, tag), elementType());
    }

    @Override
    public @NotNull ImmutableListTag copy() {
        return this;
    }

    @Override
    public @NotNull ImmutableListTag.Builder toBuilder() {
        return new Builder(new ArrayList<>(data), elementType());
    }

    @Override
    public @NotNull MutableListTag asMutable() {
        return new MutableListTagImpl(new ArrayList<>(data), elementType());
    }

    @Override
    public @NotNull ImmutableListTag asImmutable() {
        return this;
    }

    static final class Builder extends AbstractListTag.Builder<Builder, ImmutableListTag> implements ImmutableListTag.Builder {

        private Builder(final List<Tag> data, final int elementType) {
            super(data, elementType);
        }

        public Builder(final int elementType) {
            this(new ArrayList<>(), elementType);
        }

        @Override
        public @NotNull ImmutableListTag build() {
            return new ImmutableListTagImpl(TreePVector.from(data), elementType);
        }
    }
}
