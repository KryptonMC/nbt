/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.pcollections.OrderedPMap;
import org.pcollections.TreePVector;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class VisitingTests {

    @Test
    void visitNumbers() {
        assertEquals("END", new StringTagVisitor().visit(EndTag.INSTANCE));
        assertEquals("30b", new StringTagVisitor().visit(ByteTag.of((byte) 30)));
        assertEquals("48s", new StringTagVisitor().visit(ShortTag.of((short) 48)));
        assertEquals("1893", new StringTagVisitor().visit(IntTag.of(1893)));
        assertEquals("179257325L", new StringTagVisitor().visit(LongTag.of(179257325)));
        assertEquals("89.483f", new StringTagVisitor().visit(FloatTag.of(89.483F)));
        assertEquals("7832.94753256432d", new StringTagVisitor().visit(DoubleTag.of(7832.94753256432)));
    }

    @Test
    void visitArrays() {
        assertEquals("[B;1B,8B,3B,25B]", new StringTagVisitor().visit(ByteArrayTag.of(new byte[]{1, 8, 3, 25})));
        assertEquals("[I;5,93,28,47]", new StringTagVisitor().visit(IntArrayTag.of(new int[]{5, 93, 28, 47})));
        assertEquals("[L;84L,983L,42L,93L]", new StringTagVisitor().visit(LongArrayTag.of(new long[]{84, 983, 42, 93})));
    }

    @Test
    void visitStrings() {
        assertEquals("\"hello world\"", new StringTagVisitor().visit(StringTag.of("hello world")));
        assertEquals("\"hello_world\"", new StringTagVisitor().visit(StringTag.of("hello_world")));
        assertEquals("'hello\"world'", new StringTagVisitor().visit(StringTag.of("hello\"world")));
    }

    @Test
    void visitList() {
        final List<Tag> tags = List.of(StringTag.of("hello_world"), StringTag.of("goodbye_world"));
        assertEquals("[\"hello_world\",\"goodbye_world\"]", new StringTagVisitor().visit(ImmutableListTag.of(tags, StringTag.ID)));
    }

    @Test
    void visitCompound() {
        final var tags = OrderedPMap.<String, Tag>empty()
                .plus("hello_world", ByteTag.ONE)
                .plus("goodbye_world", LongTag.of(8));
        assertEquals("{hello_world:1b,goodbye_world:8L}", new StringTagVisitor().visit(ImmutableCompoundTag.of(tags)));
    }
}
