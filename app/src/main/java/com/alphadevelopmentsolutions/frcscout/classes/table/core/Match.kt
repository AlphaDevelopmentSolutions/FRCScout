package com.alphadevelopmentsolutions.frcscout.classes.table.core

import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.FormattableDate
import com.alphadevelopmentsolutions.frcscout.classes.table.account.ScoutCardInfo
import com.alphadevelopmentsolutions.frcscout.classes.table.account.ScoutCardInfoKey
import com.alphadevelopmentsolutions.frcscout.classes.table.Table
import com.alphadevelopmentsolutions.frcscout.enums.AllianceColor
import com.alphadevelopmentsolutions.frcscout.enums.MatchStatus
import com.alphadevelopmentsolutions.frcscout.enums.MatchType
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.round

@Entity(tableName = TableName.MATCH)
class Match(
        @SerializedName("event_id") var eventId: UUID,
        var key: String,
        @SerializedName("match_type") var matchType: MatchType,
        @SerializedName("set_number") var setNumber: Int,
        @SerializedName("match_number") var matchNumber: Int,
        @SerializedName("blue_alliance_team_one_id") var blueAllianceTeamOneId: UUID,
        @SerializedName("blue_alliance_team_two_id") var blueAllianceTeamTwoId: UUID,
        @SerializedName("blue_alliance_team_three_id") var blueAllianceTeamThreeId: UUID,
        @SerializedName("red_alliance_team_one_id") var redAllianceTeamOneId: UUID,
        @SerializedName("red_alliance_team_two_id") var redAllianceTeamTwoId: UUID,
        @SerializedName("red_alliance_team_three_id") var redAllianceTeamThreeId: UUID,
        @SerializedName("blue_alliance_score") var blueAllianceScore: Int? = null,
        @SerializedName("red_alliance_score") var redAllianceScore: Int? = null,
        var time: FormattableDate? = null
) : Table()
{
    /**
     * Returns either the winning teamId or tie status from the matchId
     * @return Status enum
     */
    val matchStatus: MatchStatus
        get() =
            when {
                blueAllianceScore == redAllianceScore -> MatchStatus.TIE
                blueAllianceScore?: 0 > redAllianceScore?: 0 -> MatchStatus.BLUE
                else -> MatchStatus.RED
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
                            if(scoutCardInfo.keyId == scoutCardInfoKey.serverId)
                            {
                                val stat = Integer.parseInt(scoutCardInfo.value)

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
