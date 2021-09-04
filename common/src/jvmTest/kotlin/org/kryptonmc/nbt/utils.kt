package org.kryptonmc.nbt

import okio.BufferedSource
import okio.buffer
import okio.source

fun getResource(name: String): BufferedSource = Thread.currentThread().contextClassLoader
    .getResourceAsStream(name)!!
    .source()
    .buffer()
