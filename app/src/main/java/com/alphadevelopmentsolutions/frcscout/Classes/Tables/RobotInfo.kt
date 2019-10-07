package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import com.alphadevelopmentsolutions.frcscout.Classes.Database
import java.util.*

class RobotInfo(
        var id: Int = DEFAULT_INT,
        var yearId: Int = DEFAULT_INT,
        var eventId: String = DEFAULT_STRING,
        var teamId: Int = DEFAULT_INT,
        var propertyValue: String= DEFAULT_STRING,
        var propertyKeyId: Int = DEFAULT_INT,
        var isDraft: Boolean = DEFAULT_BOOLEAN) : Table(TABLE_NAME, COLUMN_NAME_ID, CREATE_TABLE)
{
    companion object
    {
        val TABLE_NAME = "robot_info"
        val COLUMN_NAME_ID = "Id"
        val COLUMN_NAME_YEAR_ID = "YearId"
        val COLUMN_NAME_EVENT_ID = "EventId"
        val COLUMN_NAME_TEAM_ID = "TeamId"

        val COLUMN_NAME_PROPERTY_VALUE = "PropertyValue"
        val COLUMN_NAME_PROPERTY_KEY_ID = "PropertyKeyId"

        val COLUMN_NAME_IS_DRAFT = "IsDraft"

        val CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_YEAR_ID + " INTEGER," +
                COLUMN_NAME_EVENT_ID + " TEXT," +
                COLUMN_NAME_TEAM_ID + " INTEGER," +

                COLUMN_NAME_PROPERTY_VALUE + " TEXT," +
                COLUMN_NAME_PROPERTY_KEY_ID + " INTEGER," +

                COLUMN_NAME_IS_DRAFT + " INTEGER)"

        /**
         * Clears all data from the classes table
         * @param database used to clear table
         * @param clearDrafts boolean if you want to include drafts in the clear
         */
        fun clearTable(database: Database, clearDrafts: Boolean = false)
        {
            clearTable(database, TABLE_NAME, clearDrafts)
        }

        /**
         * Returns arraylist of pit cards with specified filters from database
         * @param year if specified, filters by year id
         * @param event if specified, filters by event id
         * @param team if specified, filters by team id
         * @param robotInfo if specified, filters by robotinfo id
         * @param onlyDrafts if true, filters by draft
         * @param database used to load
         * @return arraylist of robotInfo
         */
        fun getObjects(year: Year?, event: Event?, team: Team?, robotInfoKey: RobotInfoKey?, robotInfo: RobotInfo?, onlyDrafts: Boolean, database: Database): ArrayList<RobotInfo>
        {
            return database.getRobotInfo(year, event, team, robotInfoKey, robotInfo, onlyDrafts)
        }
    }

    override fun toString(): String
    {
        return "Team $teamId - Robot Info"
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
            val robotInfoList = getObjects(null, null, null, null, this, false, database)
            val robotInfo = if (robotInfoList.size > 0) robotInfoList[0] else null

            if (robotInfo != null)
            {
                yearId = robotInfo.yearId
                eventId = robotInfo.eventId
                teamId = robotInfo.teamId

                propertyValue = robotInfo.propertyValue
                propertyKeyId = robotInfo.propertyKeyId

                isDraft = robotInfo.isDraft
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
            id = database.setRobotInfo(this).toInt()

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
            successful = database.deleteRobotInfo(this)

        }

        return successful
    }

    //endregion
}
