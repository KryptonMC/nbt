/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

import kotlin.jvm.JvmStatic

/**
 * An examiner that appends the results of the examination to a string.
 */
public class StringTagExaminer : TagExaminer<String> {

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
        for (i in 0 until tag.size) {
            if (i != 0) builder.append(',')
            builder.append(tag.get(i)).append('B')
        }
        builder.append(']')
    }

    override fun examineString(tag: StringTag) {
        builder.append(quoteAndEscape(tag.value))
    }

    override fun examineList(tag: ListTag) {
        builder.append('[')
        for (i in 0 until tag.size) {
            if (i != 0) builder.append(',')
            builder.append(StringTagExaminer().examine(tag.get(i)))
        }
        builder.append(']')
    }

    override fun examineCompound(tag: CompoundTag) {
        builder.append('{')
        tag.keys.sorted().forEach {
            if (builder.length != 1) builder.append(',')
            builder.append(escape(it)).append(':').append(StringTagExaminer().examine(tag.get(it)!!))
        }
        builder.append('}')
    }

    override fun examineIntArray(tag: IntArrayTag) {
        builder.append("[I;")
        for (i in 0 until tag.size) {
            if (i != 0) builder.append(',')
            builder.append(tag.get(i))
        }
        builder.append(']')
    }

    override fun examineLongArray(tag: LongArrayTag) {
        builder.append("[L;")
        for (i in 0 until tag.size) {
            if (i != 0) builder.append(',')
            builder.append(tag.get(i)).append('L')
        }
        builder.append(']')
    }

    private companion object {

        private val VALUE_REGEX = "[A-Za-z0-9._+-]+".toRegex()
        private const val NULL_CHARACTER = 0.toChar()
        private const val BACKSLASH = '\\'
        private const val SINGLE_QUOTE = '\''
        private const val DOUBLE_QUOTE = '"'

        @JvmStatic
        private fun escape(text: String): String {
            if (VALUE_REGEX.matches(text)) return text
            return quoteAndEscape(text)
        }

        @JvmStatic
        private fun quoteAndEscape(text: String): String {
            val builder = StringBuilder(" ")
            var quote = NULL_CHARACTER
            for (i in text.indices) {
                val current = text[i]
                if (current == BACKSLASH) {
                    builder.append(BACKSLASH)
                } else if (current == DOUBLE_QUOTE || current == SINGLE_QUOTE) {
                    if (quote == NULL_CHARACTER) quote = if (current == DOUBLE_QUOTE) SINGLE_QUOTE else DOUBLE_QUOTE
                    if (quote == current) builder.append('\\')
                }
                builder.append(current)
            }
            if (quote == NULL_CHARACTER) quote = DOUBLE_QUOTE
            builder.setCharAt(0, quote)
            builder.append(quote)
            return builder.toString()
        }
    }
}
