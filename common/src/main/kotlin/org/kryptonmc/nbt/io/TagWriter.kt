/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.io

import org.kryptonmc.nbt.Tag
import java.io.DataOutput

/**
 * A writer for writing tags of type [T].
 */
public fun interface TagWriter<in T : Tag> {

    /**
     * Writes the given [value] to the given [output].
     *
     * @param output the output to write to
     * @param value the value to write
     */
    public fun write(output: DataOutput, value: T)
}
