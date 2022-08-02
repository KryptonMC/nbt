/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.nbt.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

/**
 * A utility class designed for calling methods that would usually require the
 * use of the spread operator in Kotlin, so we can avoid copying arrays
 * unnecessarily and just pass varargs as-is, just like we do in Java.
 */
@SuppressWarnings("UndocumentedPublicFunction")
public final class NoSpread {

    public static InputStream filesNewInputStream(final Path path, final OpenOption[] options) throws IOException {
        return Files.newInputStream(path, options);
    }

    public static OutputStream filesNewOutputStream(final Path path, final OpenOption[] options) throws IOException {
        return Files.newOutputStream(path, options);
    }
}
