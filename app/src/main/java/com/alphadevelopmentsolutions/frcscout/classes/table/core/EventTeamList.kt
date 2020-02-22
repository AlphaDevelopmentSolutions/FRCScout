package com.alphadevelopmentsolutions.frcscout.classes.table.core

import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.table.Table
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = TableName.EVENT_TEAM_LIST)
class EventTeamList(
        @SerializedName("team_id") var teamId: UUID,
        @SerializedName("event_id") var eventId: UUID
) : Table()
{
    /**
     * @see Table.toString
     */
    override fun toString() = ""
}
