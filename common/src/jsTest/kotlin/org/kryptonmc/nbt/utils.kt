package org.kryptonmc.nbt

import okio.BufferedSource
import okio.NodeJsFileSystem
import okio.Path.Companion.toPath
import okio.buffer

actual fun getResource(name: String): BufferedSource = NodeJsFileSystem.source(
    "../../../../src/commonTest/resources/$name".toPath()
).buffer()
