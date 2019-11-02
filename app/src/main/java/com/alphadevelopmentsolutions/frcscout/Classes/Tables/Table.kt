package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import com.alphadevelopmentsolutions.frcscout.Classes.Database
import com.alphadevelopmentsolutions.frcscout.Classes.MasterContentValues
import com.alphadevelopmentsolutions.frcscout.Interfaces.ParentTableCompanion
import java.util.*

abstract class Table protected constructor(
    var tableName: String = DEFAULT_STRING,
    var localId: Long = DEFAULT_LONG,
    var serverId: Long = DEFAULT_LONG
)
{
    abstract fun load(database: Database): Boolean
    abstract fun save(database: Database): Int
    abstract fun delete(database: Database): Boolean
    abstract fun getValues(): MasterContentValues
    abstract override fun toString(): String

    companion object: ParentTableCompanion
    {
        val DEFAULT_STRING: String
            get() = ""

        val DEFAULT_INT: Int
            get() = -1

        val DEFAULT_LONG: Long
            get() = -1

        val DEFAULT_DOUBLE: Double
            get() = -1.0

        val DEFAULT_DATE: Date
            get() = Date()

        val DEFAULT_BOOLEAN: Boolean
            get() = false
    }
}
