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
import java.io.ByteArrayOutputStream
import java.io.DataOutput
import java.io.DataOutputStream
import java.io.OutputStream

public interface TagWriter<in T : Tag> {

    public fun write(output: DataOutput, tag: T)

    public fun write(output: OutputStream, tag: T): Unit = write(DataOutputStream(output) as DataOutput, tag)

    public fun write(tag: T): OutputStream = ByteArrayOutputStream().apply { write(this, tag) }
}
