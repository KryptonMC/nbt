/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

class StringTagExaminer : TagExaminer<String> {

    private val builder = StringBuilder()

    override fun examine(tag: Tag): String {
        tag.examine(this)
        return builder.toString()
    }

    override fun examineEnd(tag: EndTag) {
        builder.append("END")
    }

    override fun examineByte(tag: ByteTag) {
        builder.append(tag.value).append('b')
    }

    override fun examineShort(tag: ShortTag) {
        builder.append(tag.value).append('s')
    }

    override fun examineInt(tag: IntTag) {
        builder.append(tag.value)
    }

    override fun examineLong(tag: LongTag) {
        builder.append(tag.value).append('L')
    }

    override fun examineFloat(tag: FloatTag) {
        builder.append(tag.value).append('f')
    }

    override fun examineDouble(tag: DoubleTag) {
        builder.append(tag.value).append('d')
    }

    override fun examineByteArray(tag: ByteArrayTag) {
        builder.append("[B;")
        val data = tag.data
        for (i in data.indices) {
            if (i != 0) builder.append(',')
            builder.append(data[i]).append('B')
        }
        builder.append(']')
    }

    override fun examineString(tag: StringTag) {
        builder.append(tag.value.quoteAndEscape())
    }

    override fun examineList(tag: ListTag) {
        builder.append('[')
        for (i in tag.indices) {
            if (i != 0) builder.append(',')
            builder.append(StringTagExaminer().examine(tag[i]))
        }
        builder.append(']')
    }

    override fun examineCompound(tag: CompoundTag) {
        builder.append('{')
        val keys = tag.keys.toMutableList().apply { sort() }
        keys.forEach {
            if (builder.length != 1) builder.append(',')
            builder.append(it.escape()).append(':').append(StringTagExaminer().examine(tag[it]!!))
        }
        builder.append('}')
    }

    override fun examineIntArray(tag: IntArrayTag) {
        builder.append("[I;")
        val data = tag.data
        for (i in data.indices) {
            if (i != 0) builder.append(',')
            builder.append(data[i])
        }
        builder.append(']')
    }

    override fun examineLongArray(tag: LongArrayTag) {
        builder.append("[L;")
        val data = tag.data
        for (i in data.indices) {
            if (i != 0) builder.append(',')
            builder.append(data[i])
        }
        builder.append(']')
    }
}

private val VALUE_REGEX = "[A-Za-z0-9._+-]+".toRegex()

private fun String.escape() = if (VALUE_REGEX matches this) this else quoteAndEscape()

private fun String.quoteAndEscape(): String {
    val builder = StringBuilder()
    var quote = 0.toChar()
    for (i in indices) {
        val current = this[i]
        if (current == '\\') {
            builder.append('\\')
        } else if (current == '"' || current == '\'') {
            if (quote == 0.toChar()) quote = if (current == '"') '\'' else '"'
            if (quote == current) builder.append('\\')
        }
        builder.append(current)
    }
    if (quote == 0.toChar()) quote = '"'
    builder.setCharAt(0, quote)
    builder.append(quote)
    return builder.toString()
}
