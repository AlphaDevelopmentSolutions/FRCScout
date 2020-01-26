package com.alphadevelopmentsolutions.frcscout.classes.table

import androidx.room.Entity

@Entity(tableName = "robot_info_keys")
class RobotInfoKey(
        var serverId: Int = DEFAULT_INT,
        var yearId: Int = DEFAULT_INT,
        var keyState: String = DEFAULT_STRING,
        var keyName: String = DEFAULT_STRING,
        var sortOrder: Int = DEFAULT_INT) : Table()
{
    /**
     * @see Table.toString
     */
    override fun toString(): String
    {
        return ""
    }
}
