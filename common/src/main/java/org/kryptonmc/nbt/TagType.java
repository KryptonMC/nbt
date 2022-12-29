/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt;

import java.io.DataInput;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;
import org.kryptonmc.nbt.visitor.StreamingTagVisitor;

/**
 * A type of tag that can be used to perform certain operations on tags of the
 * given type {@link T}.
 *
 * @param <T> the actual tag type
 */
public interface TagType<T extends Tag> {

    /**
     * Creates a new tag type that is invalid, used for when a tag ID with no
     * actual representation is read.
     *
     * @param id the invalid tag ID
     * @return a new invalid tag type
     */
    static @NotNull TagType<@NotNull EndTag> createInvalid(final int id) {
        return new TagType<>() {
            @Override
            public @NotNull String name() {
                return "INVALID[" + id + ']';
            }

            @Override
            public @NotNull String prettyName() {
                return "UNKNOWN_" + id;
            }

            @Override
            public @NotNull EndTag load(final @NotNull DataInput input, final int depth) throws IOException {
                throw createException();
            }

            @Override
            public StreamingTagVisitor.@NotNull ValueResult parse(final @NotNull DataInput input,
                                                                  final @NotNull StreamingTagVisitor visitor) throws IOException {
                throw createException();
            }

            @Override
            public void skip(final @NotNull DataInput input, final int bytes) throws IOException {
                throw createException();
            }

            @Override
            public void skip(final @NotNull DataInput input) throws IOException {
                throw createException();
            }

            private IOException createException() {
                return new IOException("Invalid tag ID " + id + '!');
            }
        };
    }

    /**
     * Gets the name of the tag.
     *
     * @return the name
     */
    @NotNull String name();

    /**
     * Gets the pretty print name of the tag.
     *
     * @return the pretty print name
     */
    @NotNull String prettyName();

    /**
     * Returns whether tags of this type are considered "value" tags, meaning
     * they just hold a value, like numeric and string tags (true), or they are
     * composed of other types, such as arrays, lists, and compounds (false).
     *
     * @return whether tags of this type are value tags
     */
    default boolean isValue() {
        return false;
    }

    /**
     * Loads a tag of this type from the given input data, recursively loading
     * if required, using the given depth to keep track of how many levels deep
     * in a recursive load we are.
     *
     * @param input the input to read the tag from
     * @param depth the current depth of a recursive read
     * @return the loaded tag
     * @throws IOException if an I/O error occurs
     */
    @NotNull T load(final @NotNull DataInput input, final int depth) throws IOException;

    /**
     * Loads a tag of this type from the given input data and calls the
     * corresponding visit method on the given visitor with the loaded data.
     *
     * @param input the input to read the tag from
     * @param visitor the visitor to pass the loaded data to
     * @return the result of the visit
     * @throws IOException if an I/O error occurs
     */
    StreamingTagVisitor.@NotNull ValueResult parse(final @NotNull DataInput input, final @NotNull StreamingTagVisitor visitor) throws IOException;

    /**
     * Loads a tag of this type from the given input data and uses the visitor
     * to visit the data as a root entry.
     *
     * <p>The semantics vary based on the result of {@link StreamingTagVisitor#visitRootEntry}</p>:
     * <ul>
     *     <li>If the result is {@link StreamingTagVisitor.ValueResult#CONTINUE}, we call {@link #parse(DataInput, StreamingTagVisitor)}.</li>
     *     <li>If the result is {@link StreamingTagVisitor.ValueResult#HALT}, we stop visiting.</li>
     *     <li>If the result is {@link StreamingTagVisitor.ValueResult#BREAK}, we call {@link #skip(DataInput)}.</li>
     * </ul>
     *
     * @param input the input to read the tag from
     * @param visitor the visitor to visit
     * @throws IOException if an I/O error occurs
     */
    default void parseRoot(final @NotNull DataInput input, final @NotNull StreamingTagVisitor visitor) throws IOException {
        switch (visitor.visitRootEntry(this)) {
            case CONTINUE -> parse(input, visitor);
            case BREAK -> skip(input);
        }
    }

    /**
     * Skips the given amount of bytes in the given input.
     *
     * @param input the input to skip bytes in
     * @param bytes the amount of bytes to skip
     * @throws IOException if an I/O error occurs
     */
    void skip(final @NotNull DataInput input, final int bytes) throws IOException;

    /**
     * Skips the one byte in the given input.
     *
     * @param input the input to skip bytes in
     * @throws IOException if an I/O error occurs
     */
    void skip(final @NotNull DataInput input) throws IOException;

    /**
     * A specialized tag type with a statically known size in bytes.
     *
     * <p>This is used for value types, where their size is always known.
     * For example, we always know that a long value will be 8 bytes.</p>
     *
     * @param <T> the type of the tag
     */
    interface StaticSize<T extends Tag> extends TagType<T> {

        /**
         * Gets the statically known size in bytes.
         *
         * @return the size
         */
        int size();

        @Override
        default void skip(final @NotNull DataInput input, final int bytes) throws IOException {
            input.skipBytes(size() * bytes);
        }

        @Override
        default void skip(final @NotNull DataInput input) throws IOException {
            input.skipBytes(size());
        }
    }

    /**
     * A specialized tag type with a dynamic size.
     *
     * <p>This is used for non-value types and string, where their size is
     * dependent on the value itself. For example, an array could be 5 elements
     * or 50 elements long.</p>
     *
     * @param <T> the type of the tag
     */
    interface VariableSize<T extends Tag> extends TagType<T> {

        @Override
        default void skip(final @NotNull DataInput input, final int bytes) throws IOException {
            for (int i = 0; i < bytes; i++) {
                skip(input);
            }
        }
    }
}
