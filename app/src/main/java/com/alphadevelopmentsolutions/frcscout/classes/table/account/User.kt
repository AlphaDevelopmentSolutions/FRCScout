package com.alphadevelopmentsolutions.frcscout.classes.table.account

import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.table.Table
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.google.gson.annotations.SerializedName

@Entity(tableName = TableName.USER)
class User(
        @SerializedName("first_name") var firstName: String,
        @SerializedName("last_name") var lastName: String? = null
) : Table()
{
    /**
     * @see Table.toString
     */
    override fun toString() = "$firstName $lastName"
}
