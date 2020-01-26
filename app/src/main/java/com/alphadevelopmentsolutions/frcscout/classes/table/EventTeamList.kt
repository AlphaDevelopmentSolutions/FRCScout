package com.alphadevelopmentsolutions.frcscout.classes.table

import androidx.room.Entity
import java.util.*

@Entity(tableName = "event_team_list")
class EventTeamList(
        var teamId: UUID,
        var eventId: UUID) : Table()
{
    /**
     * @see Table.toString
     */
    override fun toString(): String
    {
        return ""
    }
}
