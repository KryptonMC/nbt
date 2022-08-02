/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.stream

import java.io.DataOutputStream
import java.util.UUID
import org.kryptonmc.nbt.ByteArrayTag
import org.kryptonmc.nbt.ByteTag
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.DoubleTag
import org.kryptonmc.nbt.EndTag
import org.kryptonmc.nbt.FloatTag
import org.kryptonmc.nbt.IntArrayTag
import org.kryptonmc.nbt.IntTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.LongArrayTag
import org.kryptonmc.nbt.LongTag
import org.kryptonmc.nbt.ShortTag
import org.kryptonmc.nbt.StringTag

public class BinaryNBTWriter(private val output: DataOutputStream) : NBTWriter {

    private var stackSize = 1
    private var scopes = IntArray(32) { -1 }.apply { this[0] = NBTScope.COMPOUND }
    private var deferredName: String? = null

    override fun beginByteArray(size: Int) {
        require(size >= 0) { "Cannot write a byte array with a size less than 0!" }
        writeNameAndType(ByteArrayTag.ID)
        open(NBTScope.BYTE_ARRAY)
        output.writeInt(size)
    }

    override fun endByteArray() {
        close(NBTScope.BYTE_ARRAY)
    }

    override fun beginIntArray(size: Int) {
        require(size >= 0) { "Cannot write an integer array with a size less than 0!" }
        writeNameAndType(IntArrayTag.ID)
        open(NBTScope.INT_ARRAY)
        output.writeInt(size)
    }

    override fun endIntArray() {
        close(NBTScope.INT_ARRAY)
    }

    override fun beginLongArray(size: Int) {
        require(size >= 0) { "Cannot write a long array with a size less than 0!" }
        writeNameAndType(LongArrayTag.ID)
        open(NBTScope.LONG_ARRAY)
        output.writeInt(size)
    }

    override fun endLongArray() {
        close(NBTScope.LONG_ARRAY)
    }

    override fun beginList(elementType: Int, size: Int) {
        require(size >= 0) { "Cannot write a list with a size less than 0!" }
        check(elementType != 0 || size != 0) { "Invalid list! Element type must not be 0 for non-empty lists!" }
        writeNameAndType(ListTag.ID)
        open(NBTScope.LIST)
        output.writeByte(elementType)
        output.writeInt(size)
    }

    override fun endList() {
        close(NBTScope.LIST)
    }

    override fun beginCompound() {
        writeNameAndType(CompoundTag.ID)
        open(NBTScope.COMPOUND)
    }

    override fun endCompound() {
        close(NBTScope.COMPOUND)
        end()
    }

    override fun name(name: String) {
        val context = peekScope()
        check(context == NBTScope.COMPOUND) { "Nesting problem!" }
        deferredName = name
    }

    override fun value(value: String) {
        writeNameAndType(StringTag.ID)
        output.writeUTF(value)
    }

    override fun value(value: Byte) {
        writeNameAndType(ByteTag.ID)
        output.writeByte(value.toInt())
    }

    override fun value(value: Short) {
        writeNameAndType(ShortTag.ID)
        output.writeShort(value.toInt())
    }

    override fun value(value: Int) {
        writeNameAndType(IntTag.ID)
        output.writeInt(value)
    }

    override fun value(value: Long) {
        writeNameAndType(LongTag.ID)
        output.writeLong(value)
    }

    override fun value(value: Float) {
        writeNameAndType(FloatTag.ID)
        output.writeFloat(value)
    }

    override fun value(value: Double) {
        writeNameAndType(DoubleTag.ID)
        output.writeDouble(value)
    }

    override fun value(value: Boolean) {
        writeNameAndType(ByteTag.ID)
        val byte = if (value) 1 else 0
        output.writeByte(byte)
    }

    override fun value(value: UUID) {
        beginIntArray(4)
        output.writeInt((value.mostSignificantBits shr 32).toInt())
        output.writeInt(value.mostSignificantBits.toInt())
        output.writeInt((value.leastSignificantBits shr 32).toInt())
        output.writeInt(value.leastSignificantBits.toInt())
        endIntArray()
    }

    override fun end() {
        output.writeByte(EndTag.ID)
    }

    override fun close() {
        output.close()
    }

    private fun peekScope(): Int {
        check(stackSize != 0) { "Writer closed!" }
        return scopes[stackSize - 1]
    }

    private fun checkStack(): Boolean {
        if (stackSize != scopes.size) return false
        if (stackSize == 512) error("Depth too high! Maximum depth is 512!")
        scopes = scopes.copyOf(scopes.size * 2)
        return true
    }

    private fun pushScope(newTop: Int) {
        scopes[stackSize++] = newTop
    }

    private fun open(scope: Int) {
        checkStack()
        pushScope(scope)
    }

    private fun close(scope: Int) {
        val context = peekScope()
        check(context == scope) { "Nesting problem!" }
        check(deferredName == null) { "Dangling name: $deferredName!" }
        stackSize--
    }

    // Only writes the type and the name if the current scope is not a list
    private fun writeNameAndType(type: Int) {
        if (peekScope() != NBTScope.COMPOUND) return
        val name = requireNotNull(deferredName) { "All binary tags must be named!" }
        output.writeByte(type)
        output.writeUTF(name)
        deferredName = null
    }
}
