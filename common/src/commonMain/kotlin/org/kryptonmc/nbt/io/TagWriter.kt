package org.kryptonmc.nbt.io

import okio.BufferedSink
import org.kryptonmc.nbt.Tag

/**
 * A writer for writing tags of type [T].
 */
public interface TagWriter<in T : Tag> {

    /**
     * Writes the given [value] to the given [output].
     *
     * @param output the output to write to
     * @param value the value to write
     */
    public fun write(output: BufferedSink, value: T)
}
