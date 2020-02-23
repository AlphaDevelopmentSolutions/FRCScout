package com.alphadevelopmentsolutions.frcscout.classes.table.account

import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.table.Table
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = TableName.ROBOT_INFO_KEY)
class RobotInfoKey(
        @SerializedName("year_id") var yearId: UUID,
        var state: String,
        var name: String,
        var order: Int
) : Table()
{
    /**
     * @see Table.toString
     */
    override fun toString() = ""
}
