package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import androidx.room.Entity

@Entity(tableName = "robots")
class Robot(
        var id: Int = DEFAULT_INT,
        var name: String = DEFAULT_STRING,
        var teamNumber: Int = DEFAULT_INT) : Table()
{
    /**
     * @see Table.toString
     */
    override fun toString(): String
    {
        return name
    }
}
