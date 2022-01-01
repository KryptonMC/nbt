package org.kryptonmc.nbt

actual inline fun <reified T : Throwable> assertThrows(noinline action: () -> Unit): T = org.junit.jupiter.api.assertThrows<T>(action)
