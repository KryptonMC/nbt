/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.stream;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kryptonmc.nbt.ByteArrayTag;
import org.kryptonmc.nbt.ByteTag;
import org.kryptonmc.nbt.CompoundTag;
import org.kryptonmc.nbt.DoubleTag;
import org.kryptonmc.nbt.EndTag;
import org.kryptonmc.nbt.FloatTag;
import org.kryptonmc.nbt.IntArrayTag;
import org.kryptonmc.nbt.IntTag;
import org.kryptonmc.nbt.ListTag;
import org.kryptonmc.nbt.LongArrayTag;
import org.kryptonmc.nbt.LongTag;
import org.kryptonmc.nbt.ShortTag;
import org.kryptonmc.nbt.StringTag;

public final class BinaryNBTWriter implements NBTWriter {

    private final DataOutputStream output;
    private int stackSize = 1;
    private int[] scopes = new int[32];
    private @Nullable String deferredName;

    public BinaryNBTWriter(final @NotNull DataOutputStream output) {
        this.output = output;
        Arrays.fill(scopes, -1);
        scopes[0] = NBTScope.COMPOUND;
    }

    @Override
    public void beginByteArray(final int size) throws IOException {
        if (size < 0) throw new IllegalArgumentException("Cannot write a byte array with a size less than 0!");
        writeNameAndType(ByteArrayTag.ID);
        open(NBTScope.BYTE_ARRAY);
        output.writeInt(size);
    }

    @Override
    public void endByteArray() {
        close(NBTScope.BYTE_ARRAY);
    }

    @Override
    public void beginIntArray(final int size) throws IOException {
        if (size < 0) throw new IllegalArgumentException("Cannot write an integer array with a size less than 0!");
        writeNameAndType(IntArrayTag.ID);
        open(NBTScope.INT_ARRAY);
        output.writeInt(size);
    }

    @Override
    public void endIntArray() {
        close(NBTScope.INT_ARRAY);
    }

    @Override
    public void beginLongArray(final int size) throws IOException {
        if (size < 0) throw new IllegalArgumentException("Cannot write a long array with a size less than 0!");
        writeNameAndType(LongArrayTag.ID);
        open(NBTScope.LONG_ARRAY);
        output.writeInt(size);
    }

    @Override
    public void endLongArray() {
        close(NBTScope.LONG_ARRAY);
    }

    @Override
    public void beginList(final int elementType, final int size) throws IOException {
        if (size < 0) throw new IllegalArgumentException("Cannot write a list with a size less than 0!");
        if (elementType == 0 && size > 0) throw new IllegalStateException("Invalid list! Element type must not be 0 for non-empty lists!");
        writeNameAndType(ListTag.ID);
        open(NBTScope.LIST);
        output.writeByte(elementType);
        output.writeInt(size);
    }

    @Override
    public void endList() {
        close(NBTScope.LIST);
    }

    @Override
    public void beginCompound() throws IOException {
        writeNameAndType(CompoundTag.ID);
        open(NBTScope.COMPOUND);
    }

    @Override
    public void endCompound() throws IOException {
        close(NBTScope.COMPOUND);
        end();
    }

    @Override
    public void name(final @NotNull String name) {
        final int context = peekScope();
        if (context != NBTScope.COMPOUND) throw new IllegalStateException("Nesting problem!");
        deferredName = name;
    }

    @Override
    public void value(final boolean value) throws IOException {
        writeNameAndType(ByteTag.ID);
        output.writeByte(value ? 1 : 0);
    }

    @Override
    public void value(final byte value) throws IOException {
        writeNameAndType(ByteTag.ID);
        output.writeByte(value);
    }

    @Override
    public void value(final short value) throws IOException {
        writeNameAndType(ShortTag.ID);
        output.writeShort(value);
    }

    @Override
    public void value(final int value) throws IOException {
        writeNameAndType(IntTag.ID);
        output.writeInt(value);
    }

    @Override
    public void value(final long value) throws IOException {
        writeNameAndType(LongTag.ID);
        output.writeLong(value);
    }

    @Override
    public void value(final float value) throws IOException {
        writeNameAndType(FloatTag.ID);
        output.writeFloat(value);
    }

    @Override
    public void value(final double value) throws IOException {
        writeNameAndType(DoubleTag.ID);
        output.writeDouble(value);
    }

    @Override
    public void value(final @NotNull String value) throws IOException {
        writeNameAndType(StringTag.ID);
        output.writeUTF(value);
    }

    @Override
    public void end() throws IOException {
        output.writeByte(EndTag.ID);
    }

    @Override
    public void close() throws IOException {
        output.close();
    }

    private int peekScope() {
        if (stackSize == 0) throw new IllegalStateException("Writer closed!");
        return scopes[stackSize - 1];
    }

    private boolean checkStack() {
        if (stackSize != scopes.length) return false;
        if (stackSize == 512) throw new IllegalStateException("Depth too high! Maximum depth is 512!");
        scopes = Arrays.copyOf(scopes, scopes.length * 2);
        return true;
    }

    private void pushScope(final int newTop) {
        scopes[stackSize++] = newTop;
    }

    private void open(final int scope) {
        checkStack();
        pushScope(scope);
    }

    private void close(final int scope) {
        final int context = peekScope();
        if (context != scope) throw new IllegalStateException("Nesting problem!");
        if (deferredName != null) throw new IllegalStateException("Dangling name: " + deferredName + "!");
        stackSize--;
    }

    // Only writes the type and the name if the current scope is not a list
    private void writeNameAndType(final int type) throws IOException {
        if (peekScope() != NBTScope.COMPOUND) return;
        final String name = Objects.requireNonNull(deferredName, "All binary tags must be named!");
        output.writeByte(type);
        output.writeUTF(name);
        deferredName = null;
    }
}
