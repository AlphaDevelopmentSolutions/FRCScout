package com.alphadevelopmentsolutions.frcscout.classes.table

import androidx.room.Entity

@Entity(tableName = "robot_info")
class RobotInfo(
        var yearId: Int = DEFAULT_INT,
        var eventId: String = DEFAULT_STRING,
        var teamId: Int = DEFAULT_INT,
        var propertyValue: String= DEFAULT_STRING,
        var propertyKeyId: Int = DEFAULT_INT,
        var isDraft: Boolean = DEFAULT_BOOLEAN) : Table()
{
    /**
     * @see Table.toString
     */
    override fun toString(): String
    {
        return "Team $teamId - Robot Info"
    }
}
