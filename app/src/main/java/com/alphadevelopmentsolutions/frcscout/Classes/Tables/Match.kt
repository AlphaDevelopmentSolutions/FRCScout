package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import com.alphadevelopmentsolutions.frcscout.Classes.Database
import com.alphadevelopmentsolutions.frcscout.Enums.AllianceColor
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.round

class Match(
        var id: Int,
        var date: Date,
        var eventId: String,
        var key: String,
        var matchType: Type,
        var setNumber: Int,
        var matchNumber: Int,
        var blueAllianceTeamOneId: Int,
        var blueAllianceTeamTwoId: Int,
        var blueAllianceTeamThreeId: Int,
        var redAllianceTeamOneId: Int,
        var redAllianceTeamTwoId: Int,
        var redAllianceTeamThreeId: Int,
        var blueAllianceScore: Int,
        var redAllianceScore: Int) : Table(TABLE_NAME, COLUMN_NAME_ID, CREATE_TABLE)
{

    companion object
    {

        val TABLE_NAME = "matches"
        val COLUMN_NAME_ID = "Id"
        val COLUMN_NAME_DATE = "Date"
        val COLUMN_NAME_EVENT_ID = "EventId"
        val COLUMN_NAME_KEY = "Key"
        val COLUMN_NAME_MATCH_TYPE = "MatchType"
        val COLUMN_NAME_MATCH_NUMBER = "MatchNumber"
        val COLUMN_NAME_SET_NUMBER = "SetNumber"
        val COLUMN_NAME_BLUE_ALLIANCE_TEAM_ONE_ID = "BlueAllianceTeamOneId"
        val COLUMN_NAME_BLUE_ALLIANCE_TEAM_TWO_ID = "BlueAllianceTeamTwoId"
        val COLUMN_NAME_BLUE_ALLIANCE_TEAM_THREE_ID = "BlueAllianceTeamThreeId"
        val COLUMN_NAME_RED_ALLIANCE_TEAM_ONE_ID = "RedAllianceTeamOneId"
        val COLUMN_NAME_RED_ALLIANCE_TEAM_TWO_ID = "RedAllianceTeamTwoId"
        val COLUMN_NAME_RED_ALLIANCE_TEAM_THREE_ID = "RedAllianceTeamThreeId"
        val COLUMN_NAME_BLUE_ALLIANCE_SCORE = "BlueAllianceScore"
        val COLUMN_NAME_RED_ALLIANCE_SCORE = "RedAllianceScore"

        val CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_DATE + " INTEGER," +
                COLUMN_NAME_EVENT_ID + " TEXT," +
                "\"" + COLUMN_NAME_KEY + "\" TEXT," +
                COLUMN_NAME_MATCH_TYPE + " TEXT," +
                COLUMN_NAME_SET_NUMBER + " INTEGER," +
                COLUMN_NAME_MATCH_NUMBER + " INTEGER," +
                COLUMN_NAME_BLUE_ALLIANCE_TEAM_ONE_ID + " INTEGER," +
                COLUMN_NAME_BLUE_ALLIANCE_TEAM_THREE_ID + " INTEGER," +
                COLUMN_NAME_BLUE_ALLIANCE_TEAM_TWO_ID + " INTEGER," +
                COLUMN_NAME_BLUE_ALLIANCE_SCORE + " INTEGER," +
                COLUMN_NAME_RED_ALLIANCE_SCORE + " INTEGER," +
                COLUMN_NAME_RED_ALLIANCE_TEAM_ONE_ID + " INTEGER," +
                COLUMN_NAME_RED_ALLIANCE_TEAM_TWO_ID + " INTEGER," +
                COLUMN_NAME_RED_ALLIANCE_TEAM_THREE_ID + " INTEGER)"

        /**
         * Clears all data from the classes table
         * @param database used to clear table
         */
        fun clearTable(database: Database)
        {
            clearTable(database, TABLE_NAME)
        }

        /**
         * Returns arraylist of teams with specified filters from database
         * @param event if specified, filters teams by event id
         * @param team if specified, filters teams by team id
         * @param database used to load teams
         * @return arraylist of teams
         */
        fun getObjects(event: Event?, match: Match?, team: Team?, database: Database, sortDirection: Database.SortDirection = Database.SortDirection.DESC): ArrayList<Match>
        {
            return database.getMatches(event, match, team, sortDirection)
        }
    }

    /**
     * Returns either the winning team or tie status from the match
     * @return Status enum
     */
    val matchStatus: Status
        get() = when
        {
            blueAllianceScore == redAllianceScore -> Status.TIE
            blueAllianceScore > redAllianceScore -> Status.BLUE
            else -> Status.RED
        }

    /**
     * This references the types defined by the blue alliance API
     * When assigning a type, use the TypeReference interface for a more
     * plain english aproach
     */
    enum class Type
    {
        qm,
        qf,
        sf,
        f;

        fun toString(match: Match): String
        {
            return when (this)
            {
                qm -> "Quals " + match.matchNumber

                qf -> "Quarters " + match.setNumber + " - " + match.matchNumber

                sf -> "Semis " + match.setNumber + " - " + match.matchNumber

                f -> "Finals " + match.matchNumber
            }
        }

        companion object
        {

            /**
             * Converts a string value to enum
             * @param matchType string enum name
             * @return enum converted from string
             */
            fun getTypeFromString(matchType: String): Type
            {
                if (matchType.toLowerCase() == Type.qm.name.toLowerCase())
                    return Type.qm
                if (matchType.toLowerCase() == Type.qf.name.toLowerCase())
                    return Type.qf
                return if (matchType.toLowerCase() == Type.sf.name.toLowerCase())
                    Type.sf
                else
                    Type.f
            }

            fun getTypes() : ArrayList<Type>
            {
                return arrayListOf(qm, qf, sf, f)
            }
        }
    }

    interface TypeReference
    {
        companion object
        {
            val QUALIFICATIONS = Type.qm
            val QUARTER_FINALS = Type.qf
            val SEMI_FINALS = Type.sf
            val FINALS = Type.f
        }
    }

    enum class Status
    {
        TIE,
        BLUE,
        RED
    }


    /**
     * Finds the alliance color of the teamnumber sent in the param
     * @param team to find
     * @return AllianceColor color of the alliance
     */
    fun getTeamAllianceColor(team: Team): AllianceColor
    {
        if (blueAllianceTeamOneId == team.id ||
                blueAllianceTeamTwoId == team.id ||
                blueAllianceTeamThreeId == team.id)
            return AllianceColor.BLUE
        else if (redAllianceTeamOneId == team.id ||
                redAllianceTeamTwoId == team.id ||
                redAllianceTeamThreeId == team.id)
            return AllianceColor.RED

        //default to blue
        return AllianceColor.NONE
    }

    /**
     * Gets all scout cards associated with the match
     * @param event if specified, filters scout cards by event id
     * @param team if specified, filters scout cards by team id
     * @param scoutCardInfo if specified, filters scout cards by scoutcard id
     * @param onlyDrafts boolean if you only want drafts
     * @param database used for loading cards
     * @return arraylist of scout cards
     */
    fun getScoutCards(event: Event?, team: Team?, scoutCardInfo: ScoutCardInfo?, onlyDrafts: Boolean, database: Database): ArrayList<ScoutCardInfo>?
    {
        return ScoutCardInfo.getObjects(event, this, team, null, scoutCardInfo, onlyDrafts, database)
    }

    /**
     * Gets all teams associated with the match
     * @param event if specified, filters teams by event id
     * @param team if specified, filters teams by team id
     * @param database used for loading cards
     * @return arraylist of scout cards
     */
    fun getTeams(event: Event?, team: Team?, database: Database): ArrayList<Team>?
    {
        return Team.getObjects(event, this, team, database)
    }

    /**
     * Calculates all stats for this [Match]
     * Highly recommended that this gets ran inside it's own thread
     * it will take a long time to process
     * @param year [Year] used to pull data
     * @param event [Event] used to pull data
     * @param scoutCardInfoKeys [ArrayList] list of [ScoutCardInfoKey] for stats
     * @param scoutCardInfos [ArrayList] list of [ScoutCardInfo] for stats
     * @param database [Database] used to pull data
     * @return [HashMap] of stats
     */
    fun getStats(year: Year?, event: Event?, scoutCardInfoKeys: ArrayList<ScoutCardInfoKey>?, scoutCardInfos: ArrayList<ScoutCardInfo>?, database: Database): HashMap<String, Double>
    {
        val statsHashMap = HashMap<String, Double>()
        val cardCount = HashMap<String, Int>()

        val scoutCardInfoKeys = scoutCardInfoKeys ?: ScoutCardInfoKey.getObjects(year, null, database)
        val scoutCardInfos = scoutCardInfos ?: ScoutCardInfo.getObjects(event, this, null, null, null, false, database)

        if(!scoutCardInfoKeys.isNullOrEmpty() && !scoutCardInfos.isNullOrEmpty())
        {
            val teamList = ArrayList<Int>().apply {

                add(blueAllianceTeamOneId)
                add(blueAllianceTeamTwoId)
                add(blueAllianceTeamThreeId)

                add(redAllianceTeamOneId)
                add(redAllianceTeamTwoId)
                add(redAllianceTeamThreeId)
            }

            //filter the scout cards for the teams included in this match
            val filteredScoutCardInfos = ArrayList<ScoutCardInfo>().apply {

                for(scoutCardInfo in scoutCardInfos)
                {
                    if(scoutCardInfo.matchId == key && teamList.contains(scoutCardInfo.teamId))
                    {
                        add(scoutCardInfo)
                    }
                }
            }

            //iterate through each scout card info key
            for(scoutCardInfoKey in scoutCardInfoKeys)
            {
                if(scoutCardInfoKey.includeInStats)
                {
                    if(!filteredScoutCardInfos.isNullOrEmpty())
                    {
                        for(scoutCardInfo in filteredScoutCardInfos)
                        {
                            if(scoutCardInfo.propertyKeyId == scoutCardInfoKey.serverId)
                            {
                                val stat = Integer.parseInt(scoutCardInfo.propertyValue)

                                statsHashMap[scoutCardInfoKey.toString()] = (statsHashMap[scoutCardInfoKey.toString()] ?: 0.0) + stat //add to the stat record

                                val runningCardCountTotal = (cardCount[scoutCardInfoKey.toString()] ?: 0)
                                cardCount[scoutCardInfoKey.toString()] = if(scoutCardInfoKey.nullZeros && stat == 0) runningCardCountTotal else runningCardCountTotal + 1 //keep a running total of the card count
                            }
                        }

                        //check if the record was never inserted and default to zero
                        if(statsHashMap[scoutCardInfoKey.toString()] == null)
                        {
                            statsHashMap[scoutCardInfoKey.toString()] = 0.0 //add to the stat record

                            val runningCardCountTotal = (cardCount[scoutCardInfoKey.toString()] ?: 0)
                            cardCount[scoutCardInfoKey.toString()] = if(scoutCardInfoKey.nullZeros) runningCardCountTotal else runningCardCountTotal + 1 //keep a running total of the card count
                        }
                    }

                    //default stats
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


    /**
     * Converts the match object into a string title
     * @return string title of match
     */
    override fun toString(): String
    {
        return matchType.toString(this)
    }

    //region Load, Save & Delete

    /**
     * Loads the Match from the database and populates all values
     * @param database used for interacting with the SQLITE db
     * @return boolean if successful
     */
    override fun load(database: Database): Boolean
    {
        //try to open the DB if it is not open
        if (!database.isOpen) database.open()

        if (database.isOpen)
        {
            val matches = getObjects(null, this, null, database)
            val match = if (matches.size > 0) matches[0] else null

            if (match != null)
            {
                date = match.date
                blueAllianceTeamOneId = match.blueAllianceTeamOneId
                blueAllianceTeamTwoId = match.blueAllianceTeamTwoId
                blueAllianceTeamThreeId = match.blueAllianceTeamThreeId
                date = match.date
                eventId = match.eventId
                key = match.key
                matchType = match.matchType
                setNumber = match.setNumber
                matchNumber = match.matchNumber
                blueAllianceScore = match.blueAllianceScore
                redAllianceScore = match.redAllianceScore
                redAllianceTeamOneId = match.redAllianceTeamOneId
                redAllianceTeamTwoId = match.redAllianceTeamTwoId
                redAllianceTeamThreeId = match.redAllianceTeamThreeId
                return true
            }
        }

        return false
    }

    /**
     * Saves the Match into the database
     * @param database used for interacting with the SQLITE db
     * @return int id of the saved Match
     */
    override fun save(database: Database): Int
    {
        var id = -1

        //try to open the DB if it is not open
        if (!database.isOpen)
            database.open()

        if (database.isOpen)
            id = database.setMatch(this).toInt()

        //set the id if the save was successful
        if (id > 0)
            this.id = id

        return id
    }

    /**
     * Deletes the Match from the database
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
            successful = database.deleteMatch(this)

        }

        return successful
    }

    //endregion
}
