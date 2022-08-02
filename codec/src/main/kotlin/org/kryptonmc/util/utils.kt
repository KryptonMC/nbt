/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.util

import org.kryptonmc.nbt.ByteArrayTag
import org.kryptonmc.nbt.ByteTag
import org.kryptonmc.nbt.IntArrayTag
import org.kryptonmc.nbt.IntTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.LongArrayTag
import org.kryptonmc.nbt.LongTag
import org.kryptonmc.nbt.Tag
import java.util.Spliterators
import java.util.stream.Stream
import java.util.stream.StreamSupport

@JvmSynthetic
internal fun Tag.isCollection(): Boolean = this is ListTag || this is ByteArrayTag || this is IntArrayTag || this is LongArrayTag

@JvmSynthetic
internal fun Tag.stream(): Stream<Tag> = when (this) {
    is ListTag -> data.stream()
    is ByteArrayTag -> StreamSupport.stream(Spliterators.spliterator(data.map(ByteTag::of), 0), false)
    is IntArrayTag -> StreamSupport.stream(Spliterators.spliterator(data.map(IntTag::of), 0), false)
    is LongArrayTag -> StreamSupport.stream(Spliterators.spliterator(data.map(LongTag::of), 0), false)
    else -> error("Cannot get a stream for a non-collection tag!")
}
