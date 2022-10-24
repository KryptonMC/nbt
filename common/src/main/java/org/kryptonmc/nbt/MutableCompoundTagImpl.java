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

final class MutableCompoundTagImpl extends AbstractCompoundTag<MutableCompoundTag> implements MutableCompoundTag {

    private final Map<String, Tag> data;

    public MutableCompoundTagImpl(final Map<String, Tag> data) {
        this.data = data;
    }

    @Override
    public @NotNull Map<String, Tag> getData() {
        return data;
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public @NotNull MutableCompoundTag put(final @NotNull String name, final @NotNull Tag value) {
        data.put(name, value);
        return this;
    }

    @Override
    public @NotNull MutableCompoundTag remove(final @NotNull String name) {
        data.remove(name);
        return this;
    }

    @Override
    public @NotNull MutableCompoundTag copy() {
        return new MutableCompoundTagImpl(deepCopy(data));
    }

    private static Map<String, Tag> deepCopy(final Map<String, Tag> data) {
        final var result = new LinkedHashMap<String, Tag>();
        for (final var entry : data.entrySet()) {
            result.put(entry.getKey(), entry.getValue().copy());
        }
        return result;
    }

    @Override
    public @NotNull Builder toBuilder() {
        return new Builder(new LinkedHashMap<>(data));
    }

    @Override
    public @NotNull MutableCompoundTag asMutable() {
        return this;
    }

    @Override
    public @NotNull ImmutableCompoundTag asImmutable() {
        return new ImmutableCompoundTagImpl(OrderedPMap.from(data));
    }

    static final class Builder extends AbstractCompoundTag.Builder<Builder, MutableCompoundTag> implements MutableCompoundTag.Builder {

        private Builder(final Map<String, Tag> data) {
            super(data);
        }

        public Builder() {
            this(new LinkedHashMap<>());
        }

        @Override
        public @NotNull MutableCompoundTag build() {
            return new MutableCompoundTagImpl(data);
        }
    }
}
