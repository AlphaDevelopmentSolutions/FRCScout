package com.alphadevelopmentsolutions.frcscout.classes.table

import androidx.room.Entity
import java.util.*

@Entity(tableName = "robot_info_keys")
class RobotInfoKey(
        var yearId: UUID,
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