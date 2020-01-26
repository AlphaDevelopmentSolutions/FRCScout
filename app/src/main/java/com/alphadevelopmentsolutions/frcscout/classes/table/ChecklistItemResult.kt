package com.alphadevelopmentsolutions.frcscout.classes.table

import androidx.room.Entity
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "checklist_item_results")
class ChecklistItemResult(
        var checklistItemId: Int = DEFAULT_INT,
        var matchId: String = DEFAULT_STRING,
        var status: String = DEFAULT_STRING,
        var completedBy: String? = null,
        var completedDate: Date? = null,
        var isDraft: Boolean) : Table()
{
    /**
     * Gets the completed date formated for MySQL timestamp
     * @return MySQL time stamp formatted date
     */
    val completedDateForSQL: String
        get()
        {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd H:mm:ss")

            return simpleDateFormat.format(completedDate)
        }


    /**
     * @see Table.toString
     */
    override fun toString(): String
    {
        return ""
    }
}
