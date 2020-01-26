package com.alphadevelopmentsolutions.frcscout.classes.table

import androidx.room.Entity

@Entity(tableName = "users")
class User(
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
