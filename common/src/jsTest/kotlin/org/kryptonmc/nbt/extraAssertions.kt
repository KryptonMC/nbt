package org.kryptonmc.nbt

actual inline fun <reified T : Throwable> assertThrows(noinline action: () -> Unit): T {
    try {
        action()
    } catch (exception: Throwable) {
        if (exception !is T) throw AssertionError("Expected error thrown to be of type ${T::class.simpleName}, got exception of type " +
            "${exception::class.simpleName}!", exception)
        return exception
    }
    throw AssertionError("Expected exception of type ${T::class.simpleName} to be thrown, but no exception was thrown!")
}
