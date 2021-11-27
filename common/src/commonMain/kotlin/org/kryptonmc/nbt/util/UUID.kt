package org.kryptonmc.nbt.util

import kotlin.jvm.JvmStatic

/**
 * A simple multi-platform UUID, aimed at use within this library only.
 */
public expect class UUID(
    mostSignificantBits: Long,
    leastSignificantBits: Long
) : Comparable<UUID> {

    /**
     * The most significant bits of this UUID.
     */
    public val mostSignificantBits: Long

    /**
     * The least significant bits of this UUID.
     */
    public val leastSignificantBits: Long

    public companion object {

        /**
         * Generates a random UUID.
         *
         * @return a random UUID
         */
        @JvmStatic
        public fun randomUUID(): UUID

        /**
         * Creates a new UUID from the given [name] encoded in bytes.
         *
         * @param name the name to convert
         * @return a new UUID from the name
         */
        @JvmStatic
        public fun nameUUIDFromBytes(name: ByteArray): UUID

        /**
         * Converts the given [string] to its UUID wrapper.
         *
         * @param string the string UUID to convert
         * @return a new UUID from the string
         */
        @JvmStatic
        public fun fromString(string: String): UUID
    }
}
