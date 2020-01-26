package com.alphadevelopmentsolutions.frcscout.classes.Tables

import androidx.room.Entity

@Entity(tableName = "scout_card_info")
class ScoutCardInfo(
        var id: Int = DEFAULT_INT,
        var yearId: Int = DEFAULT_INT,
        var eventId: String = DEFAULT_STRING,
        var matchId: String = DEFAULT_STRING,
        var teamId: Int = DEFAULT_INT,
        var completedBy: String = DEFAULT_STRING,
        var propertyValue: String = DEFAULT_STRING,
        var propertyKeyId: Int = DEFAULT_INT,
        var isDraft: Boolean = DEFAULT_BOOLEAN) : Table()
{
    /**
     * @see Table.toString
     */
    override fun toString(): String
    {
        return "Team $teamId - Scout Card"
    }
}
