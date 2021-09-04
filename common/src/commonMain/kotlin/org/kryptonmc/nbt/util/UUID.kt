package org.kryptonmc.nbt.util

import kotlin.jvm.JvmStatic

public expect class UUID(
    mostSignificantBits: Long,
    leastSignificantBits: Long
) : Comparable<UUID> {

    public val mostSignificantBits: Long
    public val leastSignificantBits: Long

    public companion object {

        @JvmStatic
        public fun randomUUID(): UUID

        @JvmStatic
        public fun nameUUIDFromBytes(name: ByteArray): UUID

        @JvmStatic
        public fun fromString(string: String): UUID
    }
}
