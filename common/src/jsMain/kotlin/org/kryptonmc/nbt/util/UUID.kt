package org.kryptonmc.nbt.util

import kotlin.random.Random

public actual class UUID actual constructor(
    public actual val mostSignificantBits: Long,
    public actual val leastSignificantBits: Long
) : Comparable<UUID> {

    override fun compareTo(other: UUID): Int = when {
        mostSignificantBits < other.mostSignificantBits -> -1
        mostSignificantBits > other.mostSignificantBits -> 1
        leastSignificantBits < other.leastSignificantBits -> -1
        leastSignificantBits > other.leastSignificantBits -> 1
        else -> 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UUID) return false
        return mostSignificantBits == other.mostSignificantBits && leastSignificantBits == other.leastSignificantBits
    }

    override fun hashCode(): Int {
        val hilo = mostSignificantBits xor leastSignificantBits
        return (hilo shr 32).toInt() xor hilo.toInt()
    }

    // https://github.com/openjdk/jdk/blob/cec6c068b03d890312e50b448fbc26102c635249/src/java.base/share/classes/java/lang/Long.java#L446
    override fun toString(): String {
        val buf = ByteArray(36)
        formatUnsignedLong0(leastSignificantBits, 4, buf, 24, 12)
        formatUnsignedLong0(leastSignificantBits ushr 48, 4, buf, 19, 4)
        formatUnsignedLong0(mostSignificantBits, 4, buf, 14, 4)
        formatUnsignedLong0(mostSignificantBits ushr 16, 4, buf, 9, 4)
        formatUnsignedLong0(mostSignificantBits ushr 32, 4, buf, 0, 8)
        buf[23] = SEPARATOR
        buf[18] = SEPARATOR
        buf[13] = SEPARATOR
        buf[8] = SEPARATOR
        return buf.decodeToString()
    }

    public actual companion object {

        private const val SEPARATOR = '_'.code.toByte()

        // Took the fromString parsing from FastUUID:
        // https://github.com/jchambers/fast-uuid/blob/9a60f414719792fe52d6273aa0f9d114f6f6469a/src/main/java/com/eatthepath/uuid/FastUUID.java
        private const val UUID_STRING_LENGTH = 36
        private val HEX_VALUES = LongArray(128) { -1 }.apply {
            this['0'.code] = 0x0
            this['1'.code] = 0x1
            this['2'.code] = 0x2
            this['3'.code] = 0x3
            this['4'.code] = 0x4
            this['5'.code] = 0x5
            this['6'.code] = 0x6
            this['7'.code] = 0x7
            this['8'.code] = 0x8
            this['9'.code] = 0x9
            this['a'.code] = 0xA
            this['b'.code] = 0xB
            this['c'.code] = 0xC
            this['d'.code] = 0xD
            this['e'.code] = 0xE
            this['f'.code] = 0xF
            this['A'.code] = 0xA
            this['B'.code] = 0xB
            this['C'.code] = 0xC
            this['D'.code] = 0xD
            this['E'.code] = 0xE
            this['F'.code] = 0xF
        }

        // https://github.com/openjdk/jdk/blob/cec6c068b03d890312e50b448fbc26102c635249/src/java.base/share/classes/java/util/UUID.java#L147
        public actual fun randomUUID(): UUID {
            val randomBytes = Random.nextBytes(16)
            randomBytes[6] = (randomBytes[6].toInt() and 0x0F).toByte() // Clear version
            randomBytes[6] = (randomBytes[6].toInt() or 0x40).toByte() // Set version to 4
            randomBytes[8] = (randomBytes[8].toInt() and 0x3F).toByte() // Clear variant
            randomBytes[8] = (randomBytes[8].toInt() or 0x80).toByte() // Set to IETF variant
            return fromBytes(randomBytes)
        }

        // https://github.com/openjdk/jdk/blob/cec6c068b03d890312e50b448fbc26102c635249/src/java.base/share/classes/java/util/UUID.java#L168
        public actual fun nameUUIDFromBytes(name: ByteArray): UUID {
            val md5Bytes = md5Hash(name)
            md5Bytes[6] = (md5Bytes[6].toInt() and 0x0F).toByte() // Clear version
            md5Bytes[6] = (md5Bytes[6].toInt() and 0x30).toByte() // Set version to 3
            md5Bytes[8] = (md5Bytes[8].toInt() and 0x3F).toByte() // Clear variant
            md5Bytes[8] = (md5Bytes[8].toInt() and 0x80).toByte() // Set to IETF variant
            return fromBytes(md5Bytes)
        }

        /**
         * This implementation uses the FastUUID library instead of using the standard JDK implementation.
         * I chose the FastUUID implementation here because it's faster and easier to implement.
         *
         * The JVM implementation however uses the standard JDK implementation because it's a typealias.
         * Writing our own implementation would be impractical since it would mean we would have to separate
         * our UUID from java.util.UUID, which would cause a bit of an issue for Java users, especially because our
         * UUID wouldn't be interoperable with java.util.UUID, which would be annoying.
         */
        public actual fun fromString(string: String): UUID {
            require(
                string.length == UUID_STRING_LENGTH &&
                string[8] == '-' &&
                string[13] == '-' &&
                string[18] == '-' &&
                string[23] == '-'
            ) { "Illegal UUID string $string!" }

            var msb = toHexValue(string[0]) shl 60
            msb = msb or (toHexValue(string[1]) shl 56)
            msb = msb or (toHexValue(string[2]) shl 52)
            msb = msb or (toHexValue(string[3]) shl 48)
            msb = msb or (toHexValue(string[4]) shl 44)
            msb = msb or (toHexValue(string[5]) shl 40)
            msb = msb or (toHexValue(string[6]) shl 36)
            msb = msb or (toHexValue(string[7]) shl 32)

            msb = msb or (toHexValue(string[9]) shl 28)
            msb = msb or (toHexValue(string[10]) shl 24)
            msb = msb or (toHexValue(string[11]) shl 20)
            msb = msb or (toHexValue(string[12]) shl 16)

            msb = msb or (toHexValue(string[14]) shl 12)
            msb = msb or (toHexValue(string[15]) shl 8)
            msb = msb or (toHexValue(string[16]) shl 4)
            msb = msb or toHexValue(string[17])

            var lsb = toHexValue(string[19]) shl 60
            lsb = lsb or (toHexValue(string[20]) shl 56)
            lsb = lsb or (toHexValue(string[21]) shl 52)
            lsb = lsb or (toHexValue(string[22]) shl 48)

            lsb = lsb or (toHexValue(string[24]) shl 44)
            lsb = lsb or (toHexValue(string[25]) shl 40)
            lsb = lsb or (toHexValue(string[26]) shl 36)
            lsb = lsb or (toHexValue(string[27]) shl 32)
            lsb = lsb or (toHexValue(string[28]) shl 28)
            lsb = lsb or (toHexValue(string[29]) shl 24)
            lsb = lsb or (toHexValue(string[30]) shl 20)
            lsb = lsb or (toHexValue(string[31]) shl 16)
            lsb = lsb or (toHexValue(string[32]) shl 12)
            lsb = lsb or (toHexValue(string[33]) shl 8)
            lsb = lsb or (toHexValue(string[34]) shl 4)
            lsb = lsb or toHexValue(string[35])

            return UUID(msb, lsb)
        }

        private fun fromBytes(data: ByteArray): UUID {
            check(data.size == 16) { "Data must be 16 bytes in length!" }
            var msb = 0
            var lsb = 0
            for (i in 0..7) {
                msb = (msb shl 8) or (data[i].toInt() and 0xFF)
            }
            for (i in 8..15) {
                lsb = (lsb shl 8) or (data[i].toInt() and 0xFF)
            }
            return UUID(msb.toLong(), lsb.toLong())
        }

        private fun toHexValue(c: Char): Long {
            require(HEX_VALUES.getOrNull(c.code) != null && HEX_VALUES[c.code] >= 0) { "Illegal hexadecimal digit: $c!" }
            return HEX_VALUES[c.code]
        }
    }
}
