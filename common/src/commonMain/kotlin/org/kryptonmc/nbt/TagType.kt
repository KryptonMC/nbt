/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt

/**
 * A type of tag.
 *
 * @param name the name of the tag type
 * @param isValue if the tag is a value tag
 */
public data class TagType(val name: String, val isValue: Boolean = false)
