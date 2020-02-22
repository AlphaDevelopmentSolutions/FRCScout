package com.alphadevelopmentsolutions.frcscout.classes.table.account

import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.table.Table
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = TableName.SCOUT_CARD_INFO)
class ScoutCardInfo(
        @SerializedName("match_id") var matchId: UUID,
        @SerializedName("team_id") var teamId: UUID,
        @SerializedName("completed_by") var completedBy: UUID,
        var value: String,
        var keyId: UUID
) : Table()
{
    /**
     * @see Table.toString
     */
    override fun toString(): String
    {
        return "Team $teamId - Scout Card"
    }
}
