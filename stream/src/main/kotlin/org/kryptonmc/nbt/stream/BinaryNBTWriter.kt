/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.stream

import okio.BufferedSink
import okio.utf8Size
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
import java.util.UUID

public class BinaryNBTWriter(private val sink: BufferedSink) : NBTWriter() {

    private var stackSize = 1
    private var scopes = IntArray(32) { -1 }.apply {
        this[0] = NBTScope.COMPOUND
    }
    private var deferredName: String? = null

    override fun beginByteArray(size: Int) {
        writeNameAndType(ByteArrayTag.ID)
        open(NBTScope.BYTE_ARRAY)
        sink.writeInt(size)
    }

    override fun endByteArray() {
        close(NBTScope.BYTE_ARRAY)
    }

    override fun beginIntArray(size: Int) {
        writeNameAndType(IntArrayTag.ID)
        open(NBTScope.INT_ARRAY)
        sink.writeInt(size)
    }

    override fun endIntArray() {
        close(NBTScope.INT_ARRAY)
    }

    override fun beginLongArray(size: Int) {
        writeNameAndType(LongArrayTag.ID)
        open(NBTScope.LONG_ARRAY)
        sink.writeInt(size)
    }

    override fun endLongArray() {
        close(NBTScope.LONG_ARRAY)
    }

    override fun beginList(elementType: Int, size: Int) {
        check(elementType != 0 || size != 0) { "Invalid list! Element type must not be 0 for non-empty lists!" }
        writeNameAndType(ListTag.ID)
        open(NBTScope.LIST)
        sink.writeByte(elementType)
        sink.writeInt(size)
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
        sink.writeShort(value.utf8Size().toInt())
        sink.writeUtf8(value)
    }

    override fun value(value: Byte) {
        writeNameAndType(ByteTag.ID)
        sink.writeByte(value.toInt())
    }

    override fun value(value: Short) {
        writeNameAndType(ShortTag.ID)
        sink.writeShort(value.toInt())
    }

    override fun value(value: Int) {
        writeNameAndType(IntTag.ID)
        sink.writeInt(value)
    }

    override fun value(value: Long) {
        writeNameAndType(LongTag.ID)
        sink.writeLong(value)
    }

    override fun value(value: Float) {
        writeNameAndType(FloatTag.ID)
        sink.writeInt(value.toBits())
    }

    override fun value(value: Double) {
        writeNameAndType(DoubleTag.ID)
        sink.writeLong(value.toBits())
    }

    override fun value(value: Boolean) {
        writeNameAndType(ByteTag.ID)
        sink.writeByte(if (value) 1 else 0)
    }

    override fun value(value: UUID) {
        beginIntArray(4)
        sink.writeInt((value.mostSignificantBits shr 32).toInt())
        sink.writeInt(value.mostSignificantBits.toInt())
        sink.writeInt((value.leastSignificantBits shr 32).toInt())
        sink.writeInt(value.leastSignificantBits.toInt())
        endIntArray()
    }

    override fun end() {
        sink.writeByte(EndTag.ID)
    }

    override fun close() {
        sink.close()
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
        sink.writeByte(type)
        sink.writeShort(name.length)
        sink.writeUtf8(name)
        deferredName = null
    }
}
