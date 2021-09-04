package org.kryptonmc.nbt

import okio.BufferedSource

expect fun getResource(name: String): BufferedSource
