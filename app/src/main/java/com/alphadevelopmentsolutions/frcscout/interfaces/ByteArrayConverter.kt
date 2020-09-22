package com.alphadevelopmentsolutions.frcscout.interfaces

import java.nio.ByteBuffer
import java.util.*

interface ByteArrayConverter {
    companion object {

        /**
         * Converts a [ByteArray] object to a new [UUID] object
         * @param byteArray [ByteArray] to convert
         * @return [UUID] object converted from [byteArray]
         */
        fun toUUID(byteArray: ByteArray): UUID {

            ByteBuffer.wrap(byteArray).let {
                return UUID(it.long, it.long)
            }
        }

        /**
         * Converts a [UUID] object to a new [ByteArray] object
         * @param uuid [UUID] to convert
         * @return [ByteArray] object converted from [UUID]
         */
        fun fromUUID(uuid: UUID): ByteArray {

            ByteBuffer.wrap(ByteArray(16)).let {

                it.putLong(uuid.mostSignificantBits)
                it.putLong(uuid.leastSignificantBits)

                return it.array()
            }
        }
    }
}