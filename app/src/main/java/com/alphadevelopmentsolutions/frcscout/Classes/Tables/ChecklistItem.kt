package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import androidx.room.Entity
import java.util.*

@Entity(tableName = "checklist_items")
class ChecklistItem(
        var id: Int = DEFAULT_INT,
        var serverId: Int = DEFAULT_INT,
        var title: String = DEFAULT_STRING,
        var description: String) : Table()
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
    override fun toString(): String
    {
        return title
    }
}
