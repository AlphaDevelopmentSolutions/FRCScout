package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import androidx.room.Entity

@Entity(tableName = "event_team_list")
class EventTeamList(
        var id: Int = DEFAULT_INT,
        var teamId: Int = DEFAULT_INT,
        var eventId: String = DEFAULT_STRING) : Table()
{
    /**
     * @see Table.toString
     */
    override fun toString(): String
    {
        return ""
    }
}
