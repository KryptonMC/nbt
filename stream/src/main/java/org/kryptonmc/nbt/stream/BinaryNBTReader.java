/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.stream;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
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

public final class BinaryNBTReader implements NBTReader {

    private final DataInputStream input;
    private int stackSize = 1;
    private int[] scopes = new int[32];
    private @Nullable String deferredName;

    public BinaryNBTReader(final @NotNull DataInputStream input) {
        this.input = input;
        Arrays.fill(scopes, -1);
        scopes[0] = NBTScope.COMPOUND;
    }

    @Override
    public byte peekType() throws IOException {
        input.mark(1);
        final byte type = input.readByte();
        input.reset();
        return type;
    }

    @Override
    public int beginByteArray() throws IOException {
        readNameAndType(ByteArrayTag.ID);
        open(NBTScope.BYTE_ARRAY);
        return input.readInt();
    }

    @Override
    public void endByteArray() {
        close(NBTScope.BYTE_ARRAY);
    }

    @Override
    public int beginIntArray() throws IOException {
        readNameAndType(IntArrayTag.ID);
        open(NBTScope.INT_ARRAY);
        return input.readInt();
    }

    @Override
    public void endIntArray() {
        close(NBTScope.INT_ARRAY);
    }

    @Override
    public int beginLongArray() throws IOException {
        readNameAndType(LongArrayTag.ID);
        open(NBTScope.LONG_ARRAY);
        return input.readInt();
    }

    @Override
    public void endLongArray() {
        close(NBTScope.LONG_ARRAY);
    }

    @Override
    public int beginList(final int elementType) throws IOException {
        readNameAndType(ListTag.ID);
        open(NBTScope.LIST);
        final int readType = input.readByte();
        if (elementType != readType) throw new IllegalStateException("Expected list of type " + elementType + ", got " + readType + "!");
        return input.readInt();
    }

    @Override
    public void endList() {
        close(NBTScope.LIST);
    }

    @Override
    public void beginCompound() throws IOException {
        readNameAndType(CompoundTag.ID);
        open(NBTScope.COMPOUND);
    }

    @Override
    public void endCompound() {
        close(NBTScope.COMPOUND);
    }

    @Override
    public boolean hasNext() throws IOException {
        return input.available() != 0;
    }

    @Override
    public byte nextType() throws IOException {
        return input.readByte();
    }

    @Override
    public @NotNull String nextName() throws IOException {
        final int context = peekScope();
        if (context != NBTScope.COMPOUND) throw new IllegalStateException("Nesting problem!");
        input.readByte();
        return deferredName = input.readUTF();
    }

    @Override
    public byte nextByte() throws IOException {
        readNameAndType(ByteTag.ID);
        return input.readByte();
    }

    @Override
    public short nextShort() throws IOException {
        readNameAndType(ShortTag.ID);
        return input.readShort();
    }

    @Override
    public int nextInt() throws IOException {
        readNameAndType(IntTag.ID);
        return input.readInt();
    }

    @Override
    public long nextLong() throws IOException {
        readNameAndType(LongTag.ID);
        return input.readLong();
    }

    @Override
    public float nextFloat() throws IOException {
        readNameAndType(FloatTag.ID);
        return input.readFloat();
    }

    @Override
    public double nextDouble() throws IOException {
        readNameAndType(DoubleTag.ID);
        return input.readDouble();
    }

    @Override
    public @NotNull String nextString() throws IOException {
        readNameAndType(StringTag.ID);
        return input.readUTF();
    }

    @Override
    public void nextEnd() throws IOException {
        final byte type = input.readByte();
        if (type != EndTag.ID) throw new IllegalStateException("Expected END, got " + type + "!");
    }

    @Override
    public void close() throws Exception {
        input.close();
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

    private void readNameAndType(final int type) throws IOException {
        if (peekScope() != NBTScope.COMPOUND) return;
        if (deferredName == null) {
            final int readType = input.readByte();
            if (type != readType) throw new IllegalStateException("Expected " + type + ", got " + readType + "!");
            deferredName = input.readUTF();
        }
        deferredName = null;
    }
}
