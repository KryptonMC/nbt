package org.kryptonmc.nbt.util

// Shift amounts (see https://en.wikipedia.org/wiki/MD5#Algorithm)
private val PER_ROUND_SHIFT_AMOUNTS = intArrayOf(
    7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22,
    5,  9, 14, 20, 5, 9, 14, 20, 5,  9, 14, 20, 5,  9, 14, 20,
    4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23,
    6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21
)

/**
 * Pre-computed lookup table, where every value is computed from:
 * `floor(2^32 * abs(sin(i + 1)))`
 *
 * See [here](https://en.wikipedia.org/wiki/MD5#Algorithm) for details.
 */
private val TABLE = intArrayOf(
    0xd76aa478.toInt(), 0xe8c7b756.toInt(), 0x242070db, 0xc1bdceee.toInt(),
    0xf57c0faf.toInt(), 0x4787c62a, 0xa8304613.toInt(), 0xfd469501.toInt(),
    0x698098d8, 0x8b44f7af.toInt(), 0xffff5bb1.toInt(), 0x895cd7be.toInt(),
    0x6b901122, 0xfd987193.toInt(), 0xa679438e.toInt(), 0x49b40821,
    0xf61e2562.toInt(), 0xc040b340.toInt(), 0x265e5a51, 0xe9b6c7aa.toInt(),
    0xd62f105d.toInt(), 0x02441453, 0xd8a1e681.toInt(), 0xe7d3fbc8.toInt(),
    0x21e1cde6, 0xc33707d6.toInt(), 0xf4d50d87.toInt(), 0x455a14ed,
    0xa9e3e905.toInt(), 0xfcefa3f8.toInt(), 0x676f02d9, 0x8d2a4c8a.toInt(),
    0xfffa3942.toInt(), 0x8771f681.toInt(), 0x6d9d6122, 0xfde5380c.toInt(),
    0xa4beea44.toInt(), 0x4bdecfa9, 0xf6bb4b60.toInt(), 0xbebfbc70.toInt(),
    0x289b7ec6, 0xeaa127fa.toInt(), 0xd4ef3085.toInt(), 0x04881d05,
    0xd9d4d039.toInt(), 0xe6db99e5.toInt(), 0x1fa27cf8, 0xc4ac5665.toInt(),
    0xf4292244.toInt(), 0x432aff97, 0xab9423a7.toInt(), 0xfc93a039.toInt(),
    0x655b59c3, 0x8f0ccc92.toInt(), 0xffeff47d.toInt(), 0x85845dd1.toInt(),
    0x6fa87e4f, 0xfe2ce6e0.toInt(), 0xa3014314.toInt(), 0x4e0811a1,
    0xf7537e82.toInt(), 0xbd3af235.toInt(), 0x2ad7d2bb, 0xeb86d391.toInt()
)
// See https://en.wikipedia.org/wiki/MD5#Algorithm
private const val A = 0x67452301
private const val B = 0xefcdab89.toInt()
private const val C = 0x98badcfe.toInt()
private const val D = 0x10325476

// Took this algorithm from https://rosettacode.org/wiki/MD5/Implementation#Kotlin
internal fun md5Hash(input: ByteArray): ByteArray {
    val inputLenBytes = input.size
    val numBlocks = ((inputLenBytes + 8) ushr 6) + 1
    val totalLen = numBlocks shl 6
    val paddingBytes = ByteArray(totalLen - inputLenBytes)
    paddingBytes[0] = 0x80.toByte()
    var inputLenBits = (inputLenBytes shl 3).toLong()
    for (i in 0..7) {
        paddingBytes[paddingBytes.size - 8 + i] = inputLenBits.toByte()
        inputLenBits = inputLenBits ushr 8
    }

    var a = A
    var b = B
    var c = C
    var d = D
    val buffer = IntArray(16)

    for (i in 0 until numBlocks) {
        var index = i shl 6
        for (j in 0..63) {
            val temp = if (index < inputLenBytes) input[index] else paddingBytes[index - inputLenBytes]
            buffer[j ushr 2] = (temp.toInt() shl 24) or (buffer[j ushr 2] ushr 8)
            index++
        }

        val originalA = a
        val originalB = b
        val originalC = c
        val originalD = d
        for (j in 0..63) {
            val div16 = j ushr 4
            var f = 0
            var bufferIndex = j
            when (div16) {
                0 -> f = (b and c) or (b.inv() and d)
                1 -> {
                    f = (b and d) or (c and d.inv())
                    bufferIndex = (bufferIndex * 5 + 1) and 0x0F
                }
                2 -> {
                    f = b xor c xor d
                    bufferIndex = (bufferIndex * 3 + 5) and 0x0F
                }
                3 -> {
                    f = c xor (b or d.inv())
                    bufferIndex = (bufferIndex * 7) and 0x0F
                }
            }
            val temp = b + rotateLeft(a + f + buffer[bufferIndex] + TABLE[j], PER_ROUND_SHIFT_AMOUNTS[(div16 shl 2) or (j and 3)])
            a = d
            d = c
            c = b
            b = temp
        }
        a += originalA
        b += originalB
        c += originalC
        d += originalD
    }

    val md5 = ByteArray(16)
    var count = 0
    for (i in 0..3) {
        var n = if (i == 0) a else (if (i == 1) b else (if (i == 2) c else d))
        for (j in 0..3) {
            md5[count++] = n.toByte()
            n = n ushr 8
        }
    }
    return md5
}

// This is a carbon copy of java.lang.Integer.rotateLeft
private fun rotateLeft(i: Int, distance: Int) = (i shl distance) or (i ushr -distance)
