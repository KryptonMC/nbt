package org.kryptonmc.nbt.io

import okio.BufferedSink
import okio.BufferedSource
import kotlin.jvm.JvmSynthetic

@JvmSynthetic
internal expect fun BufferedSource.gzip(): BufferedSource

@JvmSynthetic
internal expect fun BufferedSource.zlib(): BufferedSource

@JvmSynthetic
internal expect fun BufferedSink.gzip(): BufferedSink

@JvmSynthetic
internal expect fun BufferedSink.zlib(): BufferedSink
