package com.alphadevelopmentsolutions.frcscout.classes.table

import androidx.room.PrimaryKey
import com.fasterxml.uuid.EthernetAddress
import com.fasterxml.uuid.Generators
import java.util.*

abstract class Table protected constructor (
    @PrimaryKey var id: UUID = DEFAULT_UUID
)
{

    abstract override fun toString(): String

    companion object
    {
        val DEFAULT_STRING: String
            get()
            {
                return ""
            }

        val DEFAULT_INT: Int
            get()
            {
                return -1
            }

        val DEFAULT_LONG: Long
            get()
            {
                return -1
            }

        val DEFAULT_DOUBLE: Double
            get()
            {
                return -1.0
            }

        val DEFAULT_DATE: Date
            get()
            {
                return Date()
            }

        val DEFAULT_BOOLEAN: Boolean
            get()
            {
                return false
            }

        @JvmStatic
        internal val DEFAULT_UUID: UUID
            get() = Generators.timeBasedGenerator(EthernetAddress.fromInterface()).generate()
    }
}
