package com.alphadevelopmentsolutions.frcscout.classes.table

import androidx.room.Entity
import java.util.*

@Entity(tableName = "scout_card_info")
class ScoutCardInfo(
        var yearId: UUID,
        var eventId: UUID,
        var matchId: UUID,
        var teamId: UUID,
        var completedBy: String = DEFAULT_STRING,
        var propertyValue: String = DEFAULT_STRING,
        var propertyKeyId: UUID) : Table()
{
    /**
     * @see Table.toString
     */
    override fun toString(): String
    {
        return "Team $teamId - Scout Card"
    }
}
