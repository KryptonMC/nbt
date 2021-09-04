package org.kryptonmc.nbt.stream

import kotlin.test.fail

actual fun <R> assertDoesNotThrow(block: () -> R): R {
    return try {
        block()
    } catch (exception: Throwable) {
        fail("Unexpected exception thrown: ${exception::class.simpleName}")
    }
}
