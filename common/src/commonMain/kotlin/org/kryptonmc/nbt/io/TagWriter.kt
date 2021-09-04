package org.kryptonmc.nbt.io

import okio.BufferedSink
import org.kryptonmc.nbt.Tag

public interface TagWriter<in T : Tag> {

    public fun write(output: BufferedSink, value: T)
}
