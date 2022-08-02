/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.stream

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
import java.io.DataInputStream

public class BinaryNBTReader(private val source: DataInputStream) : NBTReader {

    private var stackSize = 1
    private var scopes = IntArray(32) { -1 }.apply { this[0] = NBTScope.COMPOUND }
    private var deferredName: String? = null

    override fun beginByteArray(): Int {
        readNameAndType(ByteArrayTag.ID)
        open(NBTScope.BYTE_ARRAY)
        return source.readInt()
    }

    override fun endByteArray() {
        close(NBTScope.BYTE_ARRAY)
    }

    override fun beginIntArray(): Int {
        readNameAndType(IntArrayTag.ID)
        open(NBTScope.INT_ARRAY)
        return source.readInt()
    }

    override fun endIntArray() {
        close(NBTScope.INT_ARRAY)
    }

    override fun beginLongArray(): Int {
        readNameAndType(LongArrayTag.ID)
        open(NBTScope.LONG_ARRAY)
        return source.readInt()
    }

    override fun endLongArray() {
        close(NBTScope.LONG_ARRAY)
    }

    override fun beginList(elementType: Int): Int {
        readNameAndType(ListTag.ID)
        open(NBTScope.LIST)
        val readType = source.readByte().toInt()
        check(elementType == readType) { "Expected list of type $elementType, got $readType!" }
        return source.readInt()
    }

    override fun endList() {
        close(NBTScope.LIST)
    }

    override fun beginCompound() {
        readNameAndType(CompoundTag.ID)
        open(NBTScope.COMPOUND)
    }

    override fun endCompound() {
        close(NBTScope.COMPOUND)
        nextEnd()
    }

    override fun hasNext(): Boolean = source.available() != 0

    override fun nextByte(): Byte {
        readNameAndType(ByteTag.ID)
        return source.readByte()
    }

    override fun nextShort(): Short {
        readNameAndType(ShortTag.ID)
        return source.readShort()
    }

    override fun nextInt(): Int {
        readNameAndType(IntTag.ID)
        return source.readInt()
    }

    override fun nextLong(): Long {
        readNameAndType(LongTag.ID)
        return source.readLong()
    }

    override fun nextFloat(): Float {
        readNameAndType(FloatTag.ID)
        return Float.fromBits(source.readInt())
    }

    override fun nextDouble(): Double {
        readNameAndType(DoubleTag.ID)
        return Double.fromBits(source.readLong())
    }

    override fun nextName(): String {
        val context = peekScope()
        check(context == NBTScope.COMPOUND) { "Nesting problem!" }
        source.readByte()
        val name = source.readUTF()
        deferredName = name
        return name
    }

    override fun nextString(): String {
        readNameAndType(StringTag.ID)
        return source.readUTF()
    }

    override fun nextEnd() {
        val type = source.readByte()
        check(type == EndTag.ID.toByte()) { "Expected END, got $type!" }
    }

    override fun nextType(): Byte = source.readByte()

    override fun peekType(): Byte {
        source.mark(1)
        val type = source.readByte()
        source.reset()
        return type
    }

    override fun close() {
        source.close()
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

    private fun readNameAndType(type: Int) {
        if (peekScope() != NBTScope.COMPOUND) return
        if (deferredName == null) {
            val readType = source.readByte().toInt()
            check(type == readType) { "Expected $type, got ${readType}!" }
            deferredName = source.readUTF()
        }
        deferredName = null
    }
}
