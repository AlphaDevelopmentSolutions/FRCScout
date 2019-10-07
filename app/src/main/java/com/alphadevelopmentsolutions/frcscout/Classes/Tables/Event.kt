package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import com.alphadevelopmentsolutions.frcscout.Classes.Database
import java.util.*
import kotlin.math.round

class Event(
        var id: Int = DEFAULT_INT,
        var yearId: Int = DEFAULT_INT,
        var blueAllianceId: String = DEFAULT_STRING,
        var name: String = DEFAULT_STRING,
        var city: String = DEFAULT_STRING,
        var stateProvince: String = DEFAULT_STRING,
        var country: String = DEFAULT_STRING,
        var startDate: Date = DEFAULT_DATE,
        var endDate: Date = DEFAULT_DATE) : Table(TABLE_NAME, COLUMN_NAME_ID, CREATE_TABLE)
{
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
        fun getObjects(year: Year?, event: Event?, team: Team?, database: Database): ArrayList<Event>
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
        return name
    }

    /**
     * Calculates all stats for this [Event]
     * Highly recommended that this gets ran inside it's own thread
     * it will take a long time to process
     * @param year [Year] used to pull data
     * @param scoutCardInfoKeys [ArrayList] list of [ScoutCardInfoKey] for stats
     * @param scoutCardInfos [ArrayList] list of [ScoutCardInfo] for stats
     * @param database [Database] used to pull data
     * @return [HashMap] of stats
     */
    fun getStats(year: Year?, scoutCardInfoKeys: ArrayList<ScoutCardInfoKey>?, scoutCardInfos: ArrayList<ScoutCardInfo>?, database: Database): HashMap<String, Double>
    {
        val statsHashMap = HashMap<String, Double>()
        val cardCount = HashMap<String, Int>()

        val scoutCardInfoKeys = scoutCardInfoKeys ?: ScoutCardInfoKey.getObjects(year, null, database)
        val scoutCardInfos = scoutCardInfos ?: ScoutCardInfo.getObjects(this, null, null, null, null, false, database)

        if(!scoutCardInfoKeys.isNullOrEmpty() && !scoutCardInfos.isNullOrEmpty())
        {
            for(scoutCardInfoKey in scoutCardInfoKeys)
            {
                if(scoutCardInfoKey.includeInStats == true)
                {
                    if(!scoutCardInfos.isNullOrEmpty())
                    {
                        for(scoutCardInfo in scoutCardInfos)
                        {
                            if(scoutCardInfo.propertyKeyId == scoutCardInfoKey.serverId)
                            {
                                val stat = Integer.parseInt(scoutCardInfo.propertyValue)

                                statsHashMap[scoutCardInfoKey.toString()] = (statsHashMap[scoutCardInfoKey.toString()] ?: 0.0) + stat //add to the stat record

                                val runningCardCountTotal = (cardCount[scoutCardInfoKey.toString()] ?: 0)
                                cardCount[scoutCardInfoKey.toString()] = if(scoutCardInfoKey.nullZeros == true && stat == 0) runningCardCountTotal else runningCardCountTotal + 1 //keep a running total of the card count
                            }
                        }

                        if(statsHashMap[scoutCardInfoKey.toString()] == null)
                        {
                            statsHashMap[scoutCardInfoKey.toString()] = 0.0 //add to the stat record

                            val runningCardCountTotal = (cardCount[scoutCardInfoKey.toString()] ?: 0)
                            cardCount[scoutCardInfoKey.toString()] = if(scoutCardInfoKey.nullZeros == true) runningCardCountTotal else runningCardCountTotal + 1 //keep a running total of the card count
                        }
                    }

                    //set default stats
                    else
                    {
                        statsHashMap[scoutCardInfoKey.toString()] = 0.0
                        cardCount[scoutCardInfoKey.toString()] = 0
                    }
                }
            }

            //calculate averages by iterating through each stat and card count
            for(stat in statsHashMap)
            {
                val cardCount = cardCount[stat.key]!!

                statsHashMap[stat.key] = if(cardCount != 0) round((statsHashMap[stat.key]!! / cardCount) * 100.00) / 100.00 else 0.0
            }
        }

        return statsHashMap
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
            val event = if (events.size > 0) events[0] else null

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
