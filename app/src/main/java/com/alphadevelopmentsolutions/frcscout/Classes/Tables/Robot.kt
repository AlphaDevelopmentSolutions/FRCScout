package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import com.alphadevelopmentsolutions.frcscout.Classes.Database

class Robot(
        var id: Int = DEFAULT_INT,
        var name: String = DEFAULT_STRING,
        var teamNumber: Int = DEFAULT_INT) : Table(TABLE_NAME, COLUMN_NAME_ID, CREATE_TABLE)
{
    companion object
    {
        val TABLE_NAME = "robots"
        val COLUMN_NAME_ID = "Id"
        val COLUMN_NAME_NAME = "Name"
        val COLUMN_NAME_TEAM_NUMBER = "TeamId"

        val CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_NAME + " TEXT," +
                COLUMN_NAME_TEAM_NUMBER + " INTEGER)"

        /**
         * Clears all data from the classes table
         * @param database used to clear table
         */
        fun clearTable(database: Database)
        {
            clearTable(database, TABLE_NAME)
        }
    }

    override fun toString(): String
    {
        return name
    }

    //region Load, Save & Delete

    /**
     * Loads the robot from the database and populates all values
     * @param database used for interacting with the SQLITE db
     * @return boolean if successful
     */
    override fun load(database: Database): Boolean
    {
        //try to open the DB if it is not open
        if (!database.isOpen) database.open()

        if (database.isOpen)
        {
            val robot = database.getRobot(this)


            if (robot != null)
            {
                name = robot.name
                teamNumber = robot.teamNumber
                return true
            }
        }

        return false
    }

    /**
     * Saves the robot into the database
     * @param database used for interacting with the SQLITE db
     * @return boolean if successful
     */
    override fun save(database: Database): Int
    {
        var id = -1

        //try to open the DB if it is not open
        if (!database.isOpen)
            database.open()

        if (database.isOpen)
            id = database.setRobot(this).toInt()

        //set the id if the save was successful
        if (id > 0)
            this.id = id

        return id
    }

    /**
     * Deletes robot team from the database
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
            successful = database.deleteRobot(this)

        }

        return successful
    }

    //endregion
}
