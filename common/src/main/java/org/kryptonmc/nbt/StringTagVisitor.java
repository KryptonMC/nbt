/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt;

import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

/**
 * A tag visitor that visits a tag and converts it in to standard SNBT form.
 */
public final class StringTagVisitor implements TagVisitor<@NotNull String> {

    private static final Pattern VALUE_REGEX = Pattern.compile("[A-Za-z0-9._+-]+");
    private static final char NULL_CHARACTER = 0;
    private static final char BACKSLASH = '\\';
    private static final char SINGLE_QUOTE = '\'';
    private static final char DOUBLE_QUOTE = '"';

    private final StringBuilder builder = new StringBuilder();

    /**
     * Creates a new string-based tag visitor.
     */
    public StringTagVisitor() {
    }

    @Override
    public @NotNull String visit(final @NotNull Tag tag) {
        tag.visit(this);
        return builder.toString();
    }

    @Override
    public void visitEnd(final @NotNull EndTag tag) {
        builder.append("END");
    }

    @Override
    public void visitByte(final @NotNull ByteTag tag) {
        builder.append(tag.value()).append('b');
    }

    @Override
    public void visitShort(final @NotNull ShortTag tag) {
        builder.append(tag.value()).append('s');
    }

    @Override
    public void visitInt(final @NotNull IntTag tag) {
        builder.append(tag.value());
    }

    @Override
    public void visitLong(final @NotNull LongTag tag) {
        builder.append(tag.value()).append('L');
    }

    @Override
    public void visitFloat(final @NotNull FloatTag tag) {
        builder.append(tag.value()).append('f');
    }

    @Override
    public void visitDouble(final @NotNull DoubleTag tag) {
        builder.append(tag.value()).append('d');
    }

    @Override
    public void visitByteArray(final @NotNull ByteArrayTag tag) {
        builder.append("[B;");
        for (int i = 0; i < tag.size(); i++) {
            if (i != 0) builder.append(',');
            builder.append(tag.getData()[i]).append('B');
        }
        builder.append(']');
    }

    @Override
    public void visitString(final @NotNull StringTag tag) {
        builder.append(quoteAndEscape(tag.value()));
    }

    @Override
    public void visitList(final @NotNull ListTag tag) {
        builder.append('[');
        for (int i = 0; i < tag.size(); i++) {
            if (i != 0) builder.append(',');
            builder.append(new StringTagVisitor().visit(tag.get(i)));
        }
        builder.append(']');
    }

    @Override
    public void visitCompound(final @NotNull CompoundTag tag) {
        builder.append('{');
        for (final var key : tag.getData().keySet()) {
            if (builder.length() != 1) builder.append(',');
            builder.append(escape(key)).append(':').append(new StringTagVisitor().visit(tag.get(key)));
        }
        builder.append('}');
    }

    @Override
    public void visitIntArray(final @NotNull IntArrayTag tag) {
        builder.append("[I;");
        for (int i = 0; i < tag.size(); i++) {
            if (i != 0) builder.append(',');
            builder.append(tag.getData()[i]);
        }
        builder.append(']');
    }

    @Override
    public void visitLongArray(final @NotNull LongArrayTag tag) {
        builder.append("[L;");
        for (int i = 0; i < tag.size(); i++) {
            if (i != 0) builder.append(',');
            builder.append(tag.getData()[i]).append('L');
        }
        builder.append(']');
    }

    private static String quoteAndEscape(final String text) {
        final StringBuilder builder = new StringBuilder(" ");
        char quote = NULL_CHARACTER;
        for (int i = 0; i < text.length(); i++) {
            final char current = text.charAt(i);
            if (current == BACKSLASH) {
                builder.append(BACKSLASH);
            } else if (current == DOUBLE_QUOTE || current == SINGLE_QUOTE) {
                if (quote == NULL_CHARACTER) quote = current == DOUBLE_QUOTE ? SINGLE_QUOTE : DOUBLE_QUOTE;
                if (quote == current) builder.append('\\');
            }
            builder.append(current);
        }
        if (quote == NULL_CHARACTER) quote = DOUBLE_QUOTE;
        builder.setCharAt(0, quote);
        builder.append(quote);
        return builder.toString();
    }

    private static String escape(final String text) {
        if (VALUE_REGEX.matcher(text).matches()) return text;
        return quoteAndEscape(text);
    }
}
