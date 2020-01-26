package com.alphadevelopmentsolutions.frcscout.classes.table

import androidx.room.Entity
import java.util.*

@Entity(tableName = "robot_info")
class RobotInfo(
        var yearId: UUID,
        var eventId: UUID,
        var teamId: UUID,
        var propertyValue: String= DEFAULT_STRING,
        var propertyKeyId: UUID) : Table()
{
    /**
     * @see Table.toString
     */
    override fun toString(): String
    {
        return "Team $teamId - Robot Info"
    }
}
