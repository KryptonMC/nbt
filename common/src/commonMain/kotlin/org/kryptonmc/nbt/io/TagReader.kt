package org.kryptonmc.nbt.io

import okio.BufferedSource
import org.kryptonmc.nbt.Tag

public interface TagReader<out T : Tag> {

    public fun read(input: BufferedSource, depth: Int): T
}
