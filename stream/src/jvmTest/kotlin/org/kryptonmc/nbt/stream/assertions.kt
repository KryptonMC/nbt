package org.kryptonmc.nbt.stream

actual fun <R> assertDoesNotThrow(block: () -> R): R = org.junit.jupiter.api.assertDoesNotThrow(block)
