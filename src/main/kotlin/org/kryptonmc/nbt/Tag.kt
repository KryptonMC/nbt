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

public interface Tag {

    public val id: Int

    public val type: TagType

    public val reader: TagReader<Tag>

    public val writer: TagWriter<Tag>

    public fun <T> examine(examiner: TagExaminer<T>)

    public fun asString(): String = StringTagExaminer().examine(this)

    public fun copy(): Tag
}
