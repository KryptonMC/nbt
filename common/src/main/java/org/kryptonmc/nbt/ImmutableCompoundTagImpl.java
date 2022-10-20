/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt;

import java.util.LinkedHashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.pcollections.OrderedPMap;
import org.pcollections.PMap;

final class ImmutableCompoundTagImpl extends AbstractCompoundTag<ImmutableCompoundTag> implements ImmutableCompoundTag {

    private final PMap<String, Tag> data;

    public ImmutableCompoundTagImpl(final PMap<String, Tag> data) {
        this.data = data;
    }

    @Override
    public @NotNull PMap<String, Tag> getData() {
        return data;
    }

    @Override
    public @NotNull ImmutableCompoundTag put(final @NotNull String name, final @NotNull Tag value) {
        return new ImmutableCompoundTagImpl(data.plus(name, value));
    }

    @Override
    public @NotNull ImmutableCompoundTag remove(final @NotNull String name) {
        return new ImmutableCompoundTagImpl(data.minus(name));
    }

    @Override
    public @NotNull ImmutableCompoundTag copy() {
        return this;
    }

    @Override
    public @NotNull Builder toBuilder() {
        return new Builder(new LinkedHashMap<>(data));
    }

    @Override
    public @NotNull MutableCompoundTag asMutable() {
        return new MutableCompoundTagImpl(new LinkedHashMap<>(data));
    }

    @Override
    public @NotNull ImmutableCompoundTag asImmutable() {
        return this;
    }

    static final class Builder extends AbstractCompoundTag.Builder<Builder, ImmutableCompoundTag> implements ImmutableCompoundTag.Builder {

        private Builder(final Map<String, Tag> data) {
            super(data);
        }

        public Builder() {
            this(new LinkedHashMap<>());
        }

        @Override
        public @NotNull ImmutableCompoundTag build() {
            return new ImmutableCompoundTagImpl(OrderedPMap.from(data));
        }
    }
}
