/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

import org.kryptonmc.nbt.io.TagReader
import org.kryptonmc.nbt.io.TagWriter
import org.kryptonmc.snbt.StringTagExaminer

interface Tag {

    val id: Int

    val type: TagType

    val reader: TagReader<out Tag>

    val writer: TagWriter<out Tag>

    fun <T> examine(examiner: TagExaminer<T>)

    fun asString() = StringTagExaminer().examine(this)

    fun copy(): Tag
}
