package com.alphadevelopmentsolutions.frcscout.classes.table

import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.enums.AllianceColor
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.round

@Entity(tableName = "matches")
class Match(
        var date: Date = DEFAULT_DATE,
        var eventId: UUID,
        var key: String = DEFAULT_STRING,
        var matchType: Type = Type.qm,
        var setNumber: Int = DEFAULT_INT,
        var matchNumber: Int = DEFAULT_INT,
        var blueAllianceTeamOneId: Int = DEFAULT_INT,
        var blueAllianceTeamTwoId: Int = DEFAULT_INT,
        var blueAllianceTeamThreeId: Int = DEFAULT_INT,
        var redAllianceTeamOneId: Int = DEFAULT_INT,
        var redAllianceTeamTwoId: Int = DEFAULT_INT,
        var redAllianceTeamThreeId: Int = DEFAULT_INT,
        var blueAllianceScore: Int? = null,
        var redAllianceScore: Int? = null) : Table()
{
    /**
     * Returns either the winning teamId or tie status from the matchId
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

            //filter the scout cards for the teams included in this matchId
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
     * @see Table.toString
     */
    override fun toString(): String
    {
        return matchType.toString(this)
    }
}
