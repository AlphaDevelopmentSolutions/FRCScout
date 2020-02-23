package com.alphadevelopmentsolutions.frcscout.classes.table.account

import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.table.Table
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = TableName.CHECKLIST_ITEM)
class ChecklistItem(
        @SerializedName("year_id") var yearId: UUID,
        var title: String,
        var description: String
) : Table()
{
    /**
     * @see Table.toString
     */
    override fun toString() = title
}
