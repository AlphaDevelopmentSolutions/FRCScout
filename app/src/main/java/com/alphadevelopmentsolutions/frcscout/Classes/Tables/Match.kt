package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import com.alphadevelopmentsolutions.frcscout.Classes.Database
import com.alphadevelopmentsolutions.frcscout.Classes.MasterContentValues
import com.alphadevelopmentsolutions.frcscout.Classes.TableColumn
import com.alphadevelopmentsolutions.frcscout.Enums.AllianceColor
import com.alphadevelopmentsolutions.frcscout.Interfaces.ChildTableCompanion
import com.alphadevelopmentsolutions.frcscout.Interfaces.SQLiteDataTypes
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.round

class Match(
        localId: Long = DEFAULT_LONG,
        serverId: Long = DEFAULT_LONG,
        lastUpdated: Date = DEFAULT_DATE,
        var date: Date = DEFAULT_DATE,
        var eventId: String = DEFAULT_STRING,
        var key: String = DEFAULT_STRING,
        var matchType: Type = Type.qm,
        var setNumber: Int = DEFAULT_INT,
        var matchNumber: Int = DEFAULT_INT,
        var blueAllianceTeamOneId: Long = DEFAULT_LONG,
        var blueAllianceTeamTwoId: Long = DEFAULT_LONG,
        var blueAllianceTeamThreeId: Long = DEFAULT_LONG,
        var redAllianceTeamOneId: Long = DEFAULT_LONG,
        var redAllianceTeamTwoId: Long = DEFAULT_LONG,
        var redAllianceTeamThreeId: Long = DEFAULT_LONG,
        var blueAllianceScore: Int? = null,
        var redAllianceScore: Int? = null) : Table(TABLE_NAME, localId, serverId, lastUpdated)
{
    companion object: ChildTableCompanion
    {
        override val TABLE_NAME = "matches"
        const val COLUMN_NAME_DATE = "Date"
        const val COLUMN_NAME_EVENT_ID = "EventId"
        const val COLUMN_NAME_KEY = "Key"
        const val COLUMN_NAME_MATCH_TYPE = "MatchType"
        const val COLUMN_NAME_MATCH_NUMBER = "MatchNumber"
        const val COLUMN_NAME_SET_NUMBER = "SetNumber"
        const val COLUMN_NAME_BLUE_ALLIANCE_TEAM_ONE_ID = "BlueAllianceTeamOneId"
        const val COLUMN_NAME_BLUE_ALLIANCE_TEAM_TWO_ID = "BlueAllianceTeamTwoId"
        const val COLUMN_NAME_BLUE_ALLIANCE_TEAM_THREE_ID = "BlueAllianceTeamThreeId"
        const val COLUMN_NAME_RED_ALLIANCE_TEAM_ONE_ID = "RedAllianceTeamOneId"
        const val COLUMN_NAME_RED_ALLIANCE_TEAM_TWO_ID = "RedAllianceTeamTwoId"
        const val COLUMN_NAME_RED_ALLIANCE_TEAM_THREE_ID = "RedAllianceTeamThreeId"
        const val COLUMN_NAME_BLUE_ALLIANCE_SCORE = "BlueAllianceScore"
        const val COLUMN_NAME_RED_ALLIANCE_SCORE = "RedAllianceScore"

        override val childColumns: ArrayList<TableColumn>
            get() = ArrayList<TableColumn>().apply {
                add(TableColumn(COLUMN_NAME_DATE, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_EVENT_ID, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_KEY, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_MATCH_TYPE, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_SET_NUMBER, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_MATCH_NUMBER, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_BLUE_ALLIANCE_TEAM_ONE_ID, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_BLUE_ALLIANCE_TEAM_TWO_ID, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_BLUE_ALLIANCE_TEAM_THREE_ID, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_BLUE_ALLIANCE_SCORE, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_RED_ALLIANCE_SCORE, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_RED_ALLIANCE_TEAM_ONE_ID, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_RED_ALLIANCE_TEAM_TWO_ID, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_RED_ALLIANCE_TEAM_THREE_ID, SQLiteDataTypes.INTEGER))
            }

        /**
         * Returns [ArrayList] of [Match] with specified filters from [database]
         * @param event if specified, filters [Match] by [event] id
         * @param match if specified, filters [Match] by [match] id
         * @param team if specified, filters [Match] by [team] id
         * @param database used to load [Match]
         * @param sortDirection direction to sort [Match] records
         * @return [ArrayList] of [Match]
         */
        fun getObjects(event: Event?, match: Match?, team: Team?, database: Database, sortDirection: Database.SortDirection = Database.SortDirection.DESC): ArrayList<Match> {
            return ArrayList<Match>().apply {

                Type.getTypes().forEach {

                    val whereStatement = StringBuilder()
                    val whereArgs = ArrayList<String>()

                    //filter by object
                    if (event != null) {
                        whereStatement.append("$COLUMN_NAME_EVENT_ID = ?")
                        whereArgs.add(event.blueAllianceId)
                    }

                    //filter by object
                    if (match != null) {
                        whereStatement
                                .append(if (whereStatement.isNotEmpty()) " AND " else "")
                                .append("$COLUMN_NAME_KEY = ?")
                        whereArgs.add(match.key)
                    }

                    //filter by object
                    if (team != null) {
                        whereStatement
                                .append(if (whereStatement.isNotEmpty()) " AND " else "")
                                .append(team.serverId).append(" IN (")

                                .append("$COLUMN_NAME_BLUE_ALLIANCE_TEAM_ONE_ID, ")
                                .append("$COLUMN_NAME_BLUE_ALLIANCE_TEAM_TWO_ID, ")
                                .append("$COLUMN_NAME_BLUE_ALLIANCE_TEAM_THREE_ID, ")

                                .append("$COLUMN_NAME_RED_ALLIANCE_TEAM_ONE_ID, ")
                                .append("$COLUMN_NAME_RED_ALLIANCE_TEAM_TWO_ID, ")
                                .append("$COLUMN_NAME_RED_ALLIANCE_TEAM_THREE_ID)")
                    }

                    whereStatement
                            .append(if (whereStatement.isNotEmpty()) " AND " else "")
                            .append(" $COLUMN_NAME_MATCH_TYPE = ? ")
                    whereArgs.add(it.toString())

                    //add all object records to array list
                    with(database.getObjects(
                            TABLE_NAME,
                            whereStatement.toString(),
                            whereArgs,
                            null,
                            "$COLUMN_NAME_MATCH_NUMBER $sortDirection"))
                    {
                        if (this != null) {
                            while (moveToNext()) {
                                add(
                                        Match(
                                                getLong(COLUMN_NAME_LOCAL_ID),
                                                getLong(COLUMN_NAME_SERVER_ID),
                                                getDate(COLUMN_NAME_LAST_UPDATED),
                                                getDate(COLUMN_NAME_DATE),
                                                getString(COLUMN_NAME_EVENT_ID),
                                                getString(COLUMN_NAME_KEY),
                                                Type.getTypeFromString(getString(COLUMN_NAME_MATCH_TYPE)),
                                                getInt(COLUMN_NAME_SET_NUMBER),
                                                getInt(COLUMN_NAME_MATCH_NUMBER),
                                                getLong(COLUMN_NAME_BLUE_ALLIANCE_TEAM_ONE_ID),
                                                getLong(COLUMN_NAME_BLUE_ALLIANCE_TEAM_TWO_ID),
                                                getLong(COLUMN_NAME_BLUE_ALLIANCE_TEAM_THREE_ID),
                                                getLong(COLUMN_NAME_RED_ALLIANCE_TEAM_ONE_ID),
                                                getLong(COLUMN_NAME_RED_ALLIANCE_TEAM_TWO_ID),
                                                getLong(COLUMN_NAME_RED_ALLIANCE_TEAM_THREE_ID),
                                                getIntOrNull(COLUMN_NAME_BLUE_ALLIANCE_SCORE),
                                                getIntOrNull(COLUMN_NAME_RED_ALLIANCE_SCORE))
                                )
                            }

                            close()
                        }
                    }
                }
            }
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
            blueAllianceScore?: 0 > redAllianceScore?: 0 -> Status.BLUE
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
        if (blueAllianceTeamOneId == team.serverId ||
                blueAllianceTeamTwoId == team.serverId ||
                blueAllianceTeamThreeId == team.serverId)
            return AllianceColor.BLUE
        else if (redAllianceTeamOneId == team.serverId ||
                redAllianceTeamTwoId == team.serverId ||
                redAllianceTeamThreeId == team.serverId)
            return AllianceColor.RED

        //default to blue
        return AllianceColor.NONE
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
            val teamList = ArrayList<Long>().apply {

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
                if(scoutCardInfoKey.includeInStats == true)
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
                                cardCount[scoutCardInfoKey.toString()] = if(scoutCardInfoKey.nullZeros == true && stat == 0) runningCardCountTotal else runningCardCountTotal + 1 //keep a running total of the card count
                            }
                        }

                        //check if the record was never inserted and default to zero
                        if(statsHashMap[scoutCardInfoKey.toString()] == null)
                        {
                            statsHashMap[scoutCardInfoKey.toString()] = 0.0 //add to the stat record

                            val runningCardCountTotal = (cardCount[scoutCardInfoKey.toString()] ?: 0)
                            cardCount[scoutCardInfoKey.toString()] = if(scoutCardInfoKey.nullZeros == true) runningCardCountTotal else runningCardCountTotal + 1 //keep a running total of the card count
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
     * @see Table.load
     */
    override fun load(database: Database): Boolean
    {
        with(getObjects(null, this, null, database))
        {
            with(if (size > 0) this[0] else null)
            {
                if (this != null)
                {
                    this@Match.loadParentValues(this)
                    this@Match.date = date
                    this@Match.blueAllianceTeamOneId = blueAllianceTeamOneId
                    this@Match.blueAllianceTeamTwoId = blueAllianceTeamTwoId
                    this@Match.blueAllianceTeamThreeId = blueAllianceTeamThreeId
                    this@Match.date = date
                    this@Match.eventId = eventId
                    this@Match.key = key
                    this@Match.matchType = matchType
                    this@Match.setNumber = setNumber
                    this@Match.matchNumber = matchNumber
                    this@Match.blueAllianceScore = blueAllianceScore
                    this@Match.redAllianceScore = redAllianceScore
                    this@Match.redAllianceTeamOneId = redAllianceTeamOneId
                    this@Match.redAllianceTeamTwoId = redAllianceTeamTwoId
                    this@Match.redAllianceTeamThreeId = redAllianceTeamThreeId
                    return true
                }
            }
        }

        return false
    }

    /**
     * @see Table.childValues
     */
    override val childValues: MasterContentValues
        get() = MasterContentValues().apply {
            put(COLUMN_NAME_DATE, date)
            put(COLUMN_NAME_EVENT_ID, eventId)
            put(COLUMN_NAME_KEY, key)
            put(COLUMN_NAME_MATCH_TYPE, matchType.toString())
            put(COLUMN_NAME_MATCH_NUMBER, matchNumber)
            put(COLUMN_NAME_SET_NUMBER, setNumber)
            put(COLUMN_NAME_BLUE_ALLIANCE_TEAM_ONE_ID, blueAllianceTeamOneId)
            put(COLUMN_NAME_BLUE_ALLIANCE_TEAM_TWO_ID, blueAllianceTeamTwoId)
            put(COLUMN_NAME_BLUE_ALLIANCE_TEAM_THREE_ID, blueAllianceTeamThreeId)
            put(COLUMN_NAME_RED_ALLIANCE_TEAM_ONE_ID, redAllianceTeamOneId)
            put(COLUMN_NAME_RED_ALLIANCE_TEAM_TWO_ID, redAllianceTeamTwoId)
            put(COLUMN_NAME_RED_ALLIANCE_TEAM_THREE_ID, redAllianceTeamThreeId)
            put(COLUMN_NAME_BLUE_ALLIANCE_SCORE, blueAllianceScore)
            put(COLUMN_NAME_RED_ALLIANCE_SCORE, redAllianceScore)
        }

    /**
     * @see Table.toString
     */
    override fun toString(): String
    {
        return matchType.toString(this)
    }
}
