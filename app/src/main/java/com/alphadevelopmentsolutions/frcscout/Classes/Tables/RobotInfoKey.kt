package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import com.alphadevelopmentsolutions.frcscout.Classes.Database
import java.util.*

class RobotInfoKey(
        var id: Int = DEFAULT_INT,
        var serverId: Int = DEFAULT_INT,
        var yearId: Int = DEFAULT_INT,
        var keyState: String = DEFAULT_STRING,
        var keyName: String = DEFAULT_STRING,
        var sortOrder: Int = DEFAULT_INT) : Table(TABLE_NAME, COLUMN_NAME_ID, CREATE_TABLE)
{
    companion object
    {
        val TABLE_NAME = "robot_info_keys"
        val COLUMN_NAME_ID = "LocalId"
        val COLUMN_NAME_SERVER_ID = "Id"
        val COLUMN_NAME_YEAR_ID = "YearId"
        val COLUMN_NAME_SORT_ORDER = "SortOrder"
        val COLUMN_NAME_KEY_STATE = "KeyState"
        val COLUMN_NAME_KEY_NAME = "KeyName"

        val CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_SERVER_ID + " INTEGER," +
                COLUMN_NAME_YEAR_ID + " INTEGER," +
                COLUMN_NAME_SORT_ORDER + " INTEGER," +
                COLUMN_NAME_KEY_STATE + " TEXT," +
                COLUMN_NAME_KEY_NAME + " TEXT)"

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
         * @param robotInfoKey if specified, filters by robotInfoKey id
         * @param database used to load
         * @return arraylist of robotInfoKeys
         */
        fun getObjects(year: Year?, robotInfoKey: RobotInfoKey?, database: Database): ArrayList<RobotInfoKey>
        {
            return database.getRobotInfoKeys(year, robotInfoKey)
        }
    }

    override fun toString(): String
    {
        return ""
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
            val robotInfoKeyList = getObjects(null, this, database)
            val robotInfoKey = if (robotInfoKeyList.size > 0) robotInfoKeyList[0] else null

            if (robotInfoKey != null)
            {
                serverId = robotInfoKey.serverId
                yearId = robotInfoKey.yearId
                sortOrder = robotInfoKey.sortOrder
                keyState = robotInfoKey.keyState
                keyName = robotInfoKey.keyName
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
            id = database.setRobotInfoKey(this).toInt()

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
            successful = database.deleteRobotInfoKey(this)

        }

        return successful
    }

    //endregion
}
