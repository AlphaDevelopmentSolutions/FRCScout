package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import com.alphadevelopmentsolutions.frcscout.Classes.Database
import java.util.*

class EventTeamList(
        var id: Int = DEFAULT_INT,
        var teamId: Int = DEFAULT_INT,
        var eventId: String = DEFAULT_STRING) : Table(TABLE_NAME, COLUMN_NAME_ID, CREATE_TABLE)
{
    companion object
    {
        val TABLE_NAME = "event_team_list"
        val COLUMN_NAME_ID = "Id"
        val COLUMN_NAME_TEAM_ID = "TeamId"
        val COLUMN_NAME_EVENT_ID = "EventId"

        val CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_TEAM_ID + " INTEGER," +
                COLUMN_NAME_EVENT_ID + " TEXT)"

        /**
         * Clears all data from the classes table
         * @param database used to clear table
         */
        fun clearTable(database: Database)
        {
            clearTable(database, TABLE_NAME)
        }

        /**
         * Returns arraylist of event team list with specified filters from database
         * @param eventTeamList if specified, filters event team list by eventTeamList id
         * @param event if specified, filters event team list by event id
         * @param database used to load event team list
         * @return arraylist of event team list
         */
        fun getObjects(eventTeamList: EventTeamList?, event: Event?, database: Database): ArrayList<EventTeamList>?
        {
            return database.getEventTeamLists(eventTeamList, event)
        }
    }

    override fun toString(): String
    {
        return ""
    }


    //endregion

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
            val eventTeamLists = getObjects(this, null, database)
            val eventTeamList = if (eventTeamLists!!.size > 0) eventTeamLists[0] else null

            if (eventTeamList != null)
            {
                teamId = eventTeamList.teamId
                eventId = eventTeamList.eventId
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
            id = database.setEventTeamList(this).toInt()

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
            successful = database.deleteEventTeamList(this)

        }

        return successful
    }

    //endregion
}
