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
     * @see Table.toString
     */
    override fun toString(): String
    {
        return matchType.toString(this)
    }
}
