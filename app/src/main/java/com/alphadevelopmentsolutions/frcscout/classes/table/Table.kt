package com.alphadevelopmentsolutions.frcscout.classes.table

import androidx.room.PrimaryKey
import com.alphadevelopmentsolutions.frcscout.classes.FormattableDate
import com.fasterxml.uuid.EthernetAddress
import com.fasterxml.uuid.Generators
import com.google.gson.annotations.SerializedName
import java.util.*

abstract class Table protected constructor (
    @PrimaryKey var id: UUID = DEFAULT_UUID,
    @SerializedName("is_draft") var isDraft: Boolean = false
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

        val DEFAULT_DATE: FormattableDate
            get()
            {
                return FormattableDate()
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
