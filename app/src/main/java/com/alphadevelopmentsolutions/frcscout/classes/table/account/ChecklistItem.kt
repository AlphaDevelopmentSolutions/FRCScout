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
     * Gets the checklist item results from the database, sorted from newest to oldest
     * @param checklistItemResult if specified, filters checklist item results by checklist item result id
     * @param database used to load object
     * @param onlyDrafts if true, filters by draft
     * @return arraylist of results
     */
    fun getResults(checklistItemResult: ChecklistItemResult?, onlyDrafts: Boolean, database: Database): ArrayList<ChecklistItemResult>?
    {
        //get results from database
        return ChecklistItemResult.getObjects(this, checklistItemResult, onlyDrafts, database)
    }

    /**
     * @see Table.toString
     */
    override fun toString() = title
}
