/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
/**
 * The common module for Krypton NBT. This contains all the core functionality,
 * which includes the full tag API as well as reading and writing from the
 * standard binary form.
 */
module nbt.common {

    requires transitive org.jetbrains.annotations;
    requires org.pcollections;

    exports org.kryptonmc.nbt;
    exports org.kryptonmc.nbt.io;
    exports org.kryptonmc.nbt.util;
}