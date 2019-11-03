package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import com.alphadevelopmentsolutions.frcscout.Classes.Database
import com.alphadevelopmentsolutions.frcscout.Classes.MasterContentValues
import com.alphadevelopmentsolutions.frcscout.Interfaces.ParentTableCompanion
import java.util.*

abstract class Table protected constructor(
    val tableName: String,
    var localId: Long,
    var serverId: Long
)
{
    /**
     * Loads the object from the [database] and populates all values
     * @param database used for interacting with the SQLite db
     * @return [Boolean] if successful
     */
    abstract fun load(database: Database): Boolean

    /**
     * Saves the object into the [database]
     * @param database used for interacting with the SQLite db
     * @return [Int] [localId] of the saved object
     */
    fun save(database: Database): Boolean
    {
        with(database.insertOrUpdateObject(this))
        {
            if(this > 0)
            {
                localId = this

                return true
            }
        }

        return false
    }

    /**
     * Deletes the object from the [database]
     * @param database used for interacting with the SQLite db
     * @return [Boolean] if successful
     */
    fun delete(database: Database): Boolean
    {
        return database.deleteObject(this)
    }

    protected abstract val childValues: MasterContentValues
    val tableValues: MasterContentValues
        get() = MasterContentValues().apply {
            put(COLUMN_NAME_LOCAL_ID, localId)
            put(COLUMN_NAME_SERVER_ID, serverId)
            putAll(childValues)
        }

    abstract override fun toString(): String

    fun loadParentValues(table: Table)
    {
        localId = table.localId
        serverId = table.serverId
    }

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