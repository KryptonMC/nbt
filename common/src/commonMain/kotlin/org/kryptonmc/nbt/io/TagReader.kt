package org.kryptonmc.nbt.io

import okio.BufferedSource
import org.kryptonmc.nbt.Tag

/**
 * A reader for reading tags of type [T].
 */
public fun interface TagReader<out T : Tag> {

    /**
     * Reads the tag from the given [input].
     *
     * @param input the input
     * @param depth the depth, used for keeping track of recursive reading
     * @return the resulting read tag
     */
    public fun read(input: BufferedSource, depth: Int): T
}
