package org.kryptonmc.nbt.util

// https://github.com/openjdk/jdk/blob/cec6c068b03d890312e50b448fbc26102c635249/src/java.base/share/classes/java/lang/Integer.java#L98
private val DIGITS = charArrayOf(
    '0', '1', '2', '3', '4', '5',
    '6', '7', '8', '9', 'a', 'b',
    'c', 'd', 'e', 'f', 'g', 'h',
    'i', 'j', 'k', 'l', 'm', 'n',
    'o', 'p', 'q', 'r', 's', 't',
    'u', 'v', 'w', 'x', 'y', 'z'
)

// https://github.com/openjdk/jdk/blob/cec6c068b03d890312e50b448fbc26102c635249/src/java.base/share/classes/java/lang/Long.java#L415
internal fun formatUnsignedLong0(value: Long, shift: Int, buf: ByteArray, offset: Int, len: Int) {
    var charPos = offset + len
    val radix = 1 shl shift
    val mask = radix - 1
    var temp = value
    do {
        buf[--charPos] = DIGITS[value.toInt() and mask].code.toByte()
        temp = temp ushr shift
    } while (charPos > offset)
}
