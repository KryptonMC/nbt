package org.kryptonmc.nbt.io

import okio.BufferedSink
import okio.BufferedSource
import okio.buffer
import okio.deflate
import okio.gzip
import okio.inflate

internal actual fun BufferedSource.gzip(): BufferedSource = gzip().buffer()

internal actual fun BufferedSource.zlib(): BufferedSource = inflate().buffer()

internal actual fun BufferedSink.gzip(): BufferedSink = gzip().buffer()

internal actual fun BufferedSink.zlib(): BufferedSink = deflate().buffer()
