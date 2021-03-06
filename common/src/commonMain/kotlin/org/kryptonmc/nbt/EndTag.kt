/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

import okio.BufferedSink
import okio.BufferedSource
import org.kryptonmc.nbt.io.TagReader
import org.kryptonmc.nbt.io.TagWriter
import kotlin.jvm.JvmField

/**
 * The tag representing the end of a compound tag.
 */
public object EndTag : Tag {

    public const val ID: Int = 0
    @JvmField
    public val TYPE: TagType = TagType("TAG_End", true)
    @JvmField
    public val READER: TagReader<EndTag> = object : TagReader<EndTag> {

        override fun read(input: BufferedSource, depth: Int) = EndTag
    }
    @JvmField
    public val WRITER: TagWriter<EndTag> = object : TagWriter<EndTag> {

        override fun write(output: BufferedSink, value: EndTag) = Unit
    }

    override val id: Int = ID
    override val type: TagType = TYPE

    override fun write(output: BufferedSink): Unit = Unit

    override fun <T> examine(examiner: TagExaminer<T>): Unit = examiner.examineEnd(this)

    override fun copy(): EndTag = this

    override fun toString(): String = "EndTag"
}
