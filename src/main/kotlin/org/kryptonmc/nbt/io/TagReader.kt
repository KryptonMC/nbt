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
import java.io.DataInput
import java.io.DataInputStream
import java.io.InputStream

interface TagReader<out T : Tag> {

    fun read(input: DataInput, depth: Int): T

    fun read(input: InputStream, depth: Int): T = read(DataInputStream(input) as DataInput, depth)
}
