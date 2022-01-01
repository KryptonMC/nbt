package org.kryptonmc.nbt

expect inline fun <reified T : Throwable> assertThrows(noinline action: () -> Unit): T
