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
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;
import org.kryptonmc.nbt.util.Types;
import org.pcollections.TreePVector;

final class MutableListTagImpl extends AbstractListTag<MutableListTag> implements MutableListTag {

    private final List<Tag> data;
    private int elementType;

    public MutableListTagImpl(final List<Tag> data, final int elementType) {
        this.data = data;
        this.elementType = elementType;
    }

    @Override
    public int elementType() {
        return elementType;
    }

    @Override
    public @NotNull List<Tag> getData() {
        return data;
    }

    @Override
    public @NotNull MutableListTag add(final @NotNull Tag tag) {
        if (tag.id() == EndTag.ID) throw new UnsupportedOperationException("Cannot add end tag to list!");
        checkUpdateType(tag, elementType);
        data.add(tag);
        return this;
    }

    @Override
    public @NotNull MutableListTag addAll(final @NotNull Collection<? extends Tag> tags) {
        for (final var tag : tags) {
            if (!canAdd(tag)) addUnsupported(tag.id(), elementType());
            data.add(tag);
        }
        return this;
    }

    @Override
    public boolean tryAdd(final int index, final @NotNull Tag tag) {
        if (tag.id() == EndTag.ID || !updateType(tag)) return false;
        data.add(index, tag);
        return true;
    }

    @Override
    public boolean tryAdd(final @NotNull Tag tag) {
        if (tag.id() == EndTag.ID || !updateType(tag)) return false;
        return data.add(tag);
    }

    @Override
    public @NotNull MutableListTag remove(final int index) {
        data.remove(index);
        if (data.isEmpty()) elementType = EndTag.ID;
        return this;
    }

    @Override
    public @NotNull MutableListTag remove(final @NotNull Tag tag) {
        data.remove(tag);
        if (data.isEmpty()) elementType = EndTag.ID;
        return this;
    }

    @Override
    public @NotNull MutableListTag removeAll(final @NotNull Collection<? extends Tag> tags) {
        data.removeAll(tags);
        return this;
    }

    @Override
    public @NotNull MutableListTag removeIf(final @NotNull Predicate<? super Tag> predicate) {
        data.removeIf(predicate);
        return this;
    }

    @Override
    public @NotNull MutableListTag set(final int index, final @NotNull Tag tag) {
        checkUpdateType(tag, elementType);
        data.set(index, tag);
        return this;
    }

    private boolean updateType(final @NotNull Tag tag) {
        if (elementType == 0) {
            elementType = tag.id();
            return true;
        }
        return elementType == tag.id();
    }

    private void checkUpdateType(final @NotNull Tag tag, final int elementType) {
        if (!updateType(tag)) throw new UnsupportedOperationException("Cannot add tag of type " + tag.id() + " to list of type " + elementType + "!");
    }

    @Override
    public void clear() {
        data.clear();
        elementType = EndTag.ID;
    }

    @Override
    public @NotNull MutableListTag copy() {
        return new MutableListTagImpl(deepCopy(data, elementType), elementType);
    }

    private static List<Tag> deepCopy(final List<Tag> data, final int elementType) {
        if (Types.of(elementType).isValue()) return new ArrayList<>(data);
        final var result = new ArrayList<Tag>();
        for (final var tag : data) {
            result.add(tag.copy());
        }
        return result;
    }

    @Override
    public @NotNull Builder toBuilder() {
        return new Builder(new ArrayList<>(data), elementType);
    }

    @Override
    public @NotNull MutableListTag asMutable() {
        return this;
    }

    @Override
    public @NotNull ImmutableListTag asImmutable() {
        return new ImmutableListTagImpl(TreePVector.from(data), elementType);
    }

    @Override
    public String toString() {
        return "MutableListTagImpl{data=" + data + ", elementType=" + elementType + '}';
    }

    static final class Builder extends AbstractListTag.Builder<Builder, MutableListTag> implements MutableListTag.Builder {

        private Builder(final List<Tag> data, final int elementType) {
            super(data, elementType);
        }

        public Builder(final int elementType) {
            this(new ArrayList<>(), elementType);
        }

        @Override
        public @NotNull MutableListTag build() {
            return new MutableListTagImpl(data, elementType);
        }
    }
}
