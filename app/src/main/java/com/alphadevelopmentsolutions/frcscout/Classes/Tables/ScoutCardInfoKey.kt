package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import com.alphadevelopmentsolutions.frcscout.Classes.Database
import java.util.*

class ScoutCardInfoKey(
        var id: Int,
        var serverId: Int,
        var yearId: Int,
        var keyState: String,
        var keyName: String,
        var sortOrder: Int,
        var minValue: Int?,
        var maxValue: Int?,
        var nullZeros: Boolean,
        var includeInStats: Boolean,
        var dataType: DataTypes) : Table(TABLE_NAME, COLUMN_NAME_ID, CREATE_TABLE)
{
    companion object
    {
        val TABLE_NAME = "scout_card_info_keys"
        val COLUMN_NAME_ID = "LocalId"
        val COLUMN_NAME_SERVER_ID = "Id"
        val COLUMN_NAME_YEAR_ID = "YearId"
        val COLUMN_NAME_KEY_STATE = "KeyState"
        val COLUMN_NAME_KEY_NAME = "KeyName"
        val COLUMN_NAME_SORT_ORDER = "SortOrder"
        val COLUMN_NAME_MIN_VALUE = "MinValue"
        val COLUMN_NAME_MAX_VALUE = "MaxValue"
        val COLUMN_NAME_NULL_ZEROS = "NullZeros"
        val COLUMN_NAME_INCLUDE_IN_STATS = "IncludeInStats"
        val COLUMN_NAME_DATA_TYPE = "DataType"

        val CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_SERVER_ID + " INTEGER," +
                COLUMN_NAME_YEAR_ID + " INTEGER," +
                COLUMN_NAME_KEY_STATE + " TEXT," +
                COLUMN_NAME_KEY_NAME + " TEXT," +
                COLUMN_NAME_SORT_ORDER + " INTEGER," +
                COLUMN_NAME_MIN_VALUE + " INTEGER," +
                COLUMN_NAME_MAX_VALUE + " INTEGER," +
                COLUMN_NAME_NULL_ZEROS + " INTEGER," +
                COLUMN_NAME_INCLUDE_IN_STATS + " INTEGER," +
                COLUMN_NAME_DATA_TYPE + " TEXT)"

        /**
         * Clears all data from the classes table
         * @param database used to clear table
         */
        fun clearTable(database: Database)
        {
            clearTable(database, TABLE_NAME)
        }

        /**
         * Returns arraylist of pit cards with specified filters from database
         * @param year if specified, filters by year id
         * @param scoutCardInfoKey if specified, filters by scoutCardInfoKey id
         * @param database used to load
         * @return arraylist of robotInfoKeys
         */
        fun getObjects(year: Year?, scoutCardInfoKey: ScoutCardInfoKey?, database: Database): ArrayList<ScoutCardInfoKey>?
        {
            return database.getScoutCardInfoKeys(year, scoutCardInfoKey)
        }
    }

    enum class DataTypes
    {
        BOOL,
        INT,
        TEXT;

        companion object
        {
            /**
             * Parses the string into a datatype format
             * @param type string of type to parse
             * @return datatype
             */
            fun parseString(type: String): DataTypes
            {
                return when
                {
                    type.toUpperCase() == BOOL.name -> BOOL
                    type.toUpperCase() == INT.name -> INT
                    type.toUpperCase() == TEXT.name -> TEXT
                    else -> BOOL
                }

            }
        }
    }

    override fun toString(): String
    {
        return "$keyState $keyName"
    }

    //region Load, Save & Delete

    /**
     * Loads the object from the database and populates all values
     * @param database used for interacting with the SQLITE db
     * @return boolean if successful
     */
    override fun load(database: Database): Boolean
    {
        //try to open the DB if it is not open
        if (!database.isOpen) database.open()

        if (database.isOpen)
        {
            val scoutCardInfoKeys = getObjects(null, this, database)
            val scoutCardInfoKey = if (scoutCardInfoKeys!!.size > 0) scoutCardInfoKeys[0] else null

            if (scoutCardInfoKey != null)
            {
                serverId = scoutCardInfoKey.serverId

                yearId = scoutCardInfoKey.yearId

                sortOrder = scoutCardInfoKey.sortOrder

                keyState = scoutCardInfoKey.keyState
                keyName = scoutCardInfoKey.keyName

                minValue = scoutCardInfoKey.minValue
                maxValue = scoutCardInfoKey.minValue

                nullZeros = scoutCardInfoKey.nullZeros
                includeInStats = scoutCardInfoKey.includeInStats

                dataType = scoutCardInfoKey.dataType
                return true
            }
        }

        return false
    }

    /**
     * Saves the object into the database
     * @param database used for interacting with the SQLITE db
     * @return int id of the saved object
     */
    override fun save(database: Database): Int
    {
        var id = -1

        //try to open the DB if it is not open
        if (!database.isOpen)
            database.open()

        if (database.isOpen)
            id = database.setScoutCardInfoKey(this).toInt()

        //set the id if the save was successful
        if (id > 0)
            this.id = id

        return id
    }

    /**
     * Deletes the object from the database
     * @param database used for interacting with the SQLITE db
     * @return boolean if successful
     */
    override fun delete(database: Database): Boolean
    {
        var successful = false

        //try to open the DB if it is not open
        if (!database.isOpen) database.open()

        if (database.isOpen)
        {
            successful = database.deleteScoutCardInfoKey(this)

        }

        return successful
    }

    //endregion
}
