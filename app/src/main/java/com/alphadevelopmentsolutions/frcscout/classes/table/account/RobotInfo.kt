package com.alphadevelopmentsolutions.frcscout.classes.table.account

import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.table.Table
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = TableName.ROBOT_INFO)
class RobotInfo(
        @SerializedName("event_id") var eventId: UUID,
        @SerializedName("team_id") var teamId: UUID,
        @SerializedName("key_id") var keyId: UUID,
        var value: String
) : Table()
{
    /**
     * @see Table.toString
     */
    override fun toString() = value
}
