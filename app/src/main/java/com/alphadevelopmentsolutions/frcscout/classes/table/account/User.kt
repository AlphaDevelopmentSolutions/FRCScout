package com.alphadevelopmentsolutions.frcscout.classes.table.account

import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.table.Table

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
