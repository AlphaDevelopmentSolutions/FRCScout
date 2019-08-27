package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import com.alphadevelopmentsolutions.frcscout.Classes.Database
import java.util.*

class Event : Table
{

    var id: Int = 0
    var yearId: Int = 0

    var blueAllianceId: String? = null
    var name: String? = null
    var city: String? = null
    var stateProvince: String? = null
    var country: String? = null

    var startDate: Date? = null
    var endDate: Date? = null

    constructor(
            id: Int,
            yearId: Int,
            blueAllianceId: String,
            name: String,
            city: String,
            stateProvince: String,
            country: String,
            startDate: Date,
            endDate: Date) : super(TABLE_NAME, COLUMN_NAME_ID, CREATE_TABLE)
    {
        this.id = id
        this.yearId = yearId
        this.blueAllianceId = blueAllianceId
        this.name = name
        this.city = city
        this.stateProvince = stateProvince
        this.country = country
        this.startDate = startDate
        this.endDate = endDate
    }

    /**
     * Used for loading
     * @param id to load
     */
    constructor(id: Int, database: Database) : super(TABLE_NAME, COLUMN_NAME_ID, CREATE_TABLE)
    {
        this.id = id

        load(database)
    }

    companion object
    {

        val TABLE_NAME = "events"
        val COLUMN_NAME_ID = "Id"
        val COLUMN_NAME_YEAR_ID = "YearId"
        val COLUMN_NAME_BLUE_ALLIANCE_ID = "BlueAllianceId"
        val COLUMN_NAME_NAME = "Name"
        val COLUMN_NAME_CITY = "City"
        val COLUMN_NAME_STATEPROVINCE = "StateProvince"
        val COLUMN_NAME_COUNTRY = "Country"
        val COLUMN_NAME_START_DATE = "StartDate"
        val COLUMN_NAME_END_DATE = "EndDate"

        val CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_YEAR_ID + " INTEGER," +
                COLUMN_NAME_BLUE_ALLIANCE_ID + " TEXT," +
                COLUMN_NAME_NAME + " TEXT," +
                COLUMN_NAME_CITY + " TEXT," +
                COLUMN_NAME_STATEPROVINCE + " TEXT," +
                COLUMN_NAME_COUNTRY + " TEXT," +
                COLUMN_NAME_START_DATE + " INTEGER," +
                COLUMN_NAME_END_DATE + " INTEGER)"

        /**
         * Clears all data from the classes table
         * @param database used to clear table
         */
        fun clearTable(database: Database)
        {
            clearTable(database, TABLE_NAME)
        }

        /**
         * Returns arraylist of events with specified filters from database
         * @param year if specified, filters events by year id
         * @param event if specified, filters events by event id
         * @param database used to load events
         * @return arraylist of events
         */
        fun getObjects(year: Year?, event: Event?, team: Team?, database: Database): ArrayList<Event>?
        {
            return database.getEvents(year, event, team)
        }
    }

    /**
     * Gets matches from a specific event
     * @param match if specified, filters matches by match id
     * @param team if specified, filters matches by team id
     * @param database used to get matches
     * @return arraylist of matches
     */
    fun getMatches(match: Match?, team: Team?, database: Database): ArrayList<Match>?
    {
        return Match.getObjects(this, match, team, database)
    }

    /**
     * Gets teams from a specific event
     * @param match if specified, filters matches by match id
     * @param team if specified, filters teams by team id
     * @param database used to get teams
     * @return arraylist of teams
     */
    fun getTeams(match: Match?, team: Team?, database: Database): ArrayList<Team>?
    {
        return Team.getObjects(this, match, team, database)
    }

    /**
     * Gets event team list from a specific event
     * @param eventTeamList if specified, filters event team list by eventteamlist id
     * @param database used to get event team list
     * @return arraylist of teams
     */
    fun getEventTeamList(eventTeamList: EventTeamList?, database: Database): ArrayList<EventTeamList>?
    {
        return EventTeamList.getObjects(eventTeamList, this, database)
    }


    override fun toString(): String
    {
        return name!!
    }

    //endregion

    //region Load, Save & Delete

    /**
     * Loads the event from the database and populates all values
     * @param database used for interacting with the SQLITE db
     * @return boolean if successful
     */
    override fun load(database: Database): Boolean
    {
        //try to open the DB if it is not open
        if (!database.isOpen) database.open()

        if (database.isOpen)
        {
            val events = getObjects(null, this, null, database)
            val event = if (events!!.size > 0) events[0] else null

            if (event != null)
            {
                yearId = event.yearId
                blueAllianceId = event.blueAllianceId
                name = event.name
                city = event.city
                stateProvince = event.stateProvince
                country = event.country
                startDate = event.startDate
                endDate = event.endDate
                return true
            }
        }

        return false
    }

    /**
     * Saves the event into the database
     * @param database used for interacting with the SQLITE db
     * @return int id of the saved event
     */
    override fun save(database: Database): Int
    {
        var id = -1

        //try to open the DB if it is not open
        if (!database.isOpen)
            database.open()

        if (database.isOpen)
            id = database.setEvent(this).toInt()

        //set the id if the save was successful
        if (id > 0)
            this.id = id

        return id
    }

    /**
     * Deletes the event from the database
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
            successful = database.deleteEvent(this)
        }

        return successful
    }

    //endregion
}
