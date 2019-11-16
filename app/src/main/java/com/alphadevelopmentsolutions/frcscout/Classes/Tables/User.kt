package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import androidx.room.Entity

@Entity(tableName = "users")
class User(
        var id: Int = DEFAULT_INT,
        var firstName: String = DEFAULT_STRING,
        var lastName: String = DEFAULT_STRING) : Table()
{
    /**
     * @see Table.toString
     */
    override fun toString(): String
    {
        return "$firstName $lastName"
    }
}
