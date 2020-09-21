package com.alphadevelopmentsolutions.frcscout.classes.table

import com.alphadevelopmentsolutions.frcscout.classes.Database
import java.text.SimpleDateFormat
import java.util.*

class ChecklistItemResult(
        var id: Int = DEFAULT_INT,
        var checklistItemId: Int = DEFAULT_INT,
        var matchId: String = DEFAULT_STRING,
        var status: String = DEFAULT_STRING,
        var completedBy: String? = null,
        var completedDate: Date? = null,
        var isDraft: Boolean) : Table(TABLE_NAME, COLUMN_NAME_ID, CREATE_TABLE)
{
    companion object
    {
        val TABLE_NAME = "checklist_item_results"
        val COLUMN_NAME_ID = "Id"
        val COLUMN_NAME_CHECKLIST_ITEM_ID = "ChecklistItemId"
        val COLUMN_NAME_MATCH_ID = "MatchId"
        val COLUMN_NAME_STATUS = "Status"
        val COLUMN_NAME_COMPLETED_BY = "CompletedBy"
        val COLUMN_NAME_COMPLETED_DATE = "CompletedDate"
        val COLUMN_NAME_IS_DRAFT = "IsDraft"

        val CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_CHECKLIST_ITEM_ID + " INTEGER," +
                COLUMN_NAME_MATCH_ID + " TEXT," +
                COLUMN_NAME_STATUS + " TEXT," +
                COLUMN_NAME_COMPLETED_BY + " TEXT," +
                COLUMN_NAME_COMPLETED_DATE + " INTEGER," +
                COLUMN_NAME_IS_DRAFT + " INTEGER)"

        /**
         * Clears all data from the classes table
         * @param database used to clear table
         * @param clearDrafts boolean if you want to include drafts in the clear
         */
        fun clearTable(database: Database, clearDrafts: Boolean = false)
        {
            clearTable(database, TABLE_NAME, clearDrafts)
        }

        /**
         * Returns arraylist of checklist items with specified filters from database
         * @param checklistItem if specified, filters checklist items by checklistItem id
         * @param checklistItemResult if specified, filters checklist items by checklistItemResult id
         * @param onlyDrafts if true, filters checklist items by draft
         * @param database used to load checklist items
         * @return arraylist of checklist items
         */
        fun getObjects(checklistItem: ChecklistItem?, checklistItemResult: ChecklistItemResult?, onlyDrafts: Boolean, database: Database): ArrayList<ChecklistItemResult>?
        {
            return database.getChecklistItemResults(checklistItem, checklistItemResult, onlyDrafts)
        }
    }


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


    override fun toString(): String
    {
        return ""
    }

    //region Load, Save & Delete

    /**
     * Loads the object from the database and populates all values
     * @param database used for interacting with the SQLITE db
     * @return boolean if successful
     */
    override fun load(database: Database): Boolean
    {
        //try to open the DB if it is not open
        if (!database.isOpen) database.open()

        if (database.isOpen)
        {
            val checklistItemResults = getObjects(null, this, false, database)
            val checklistItemResult = if (checklistItemResults!!.size > 0) checklistItemResults[0] else null

            if (checklistItemResult != null)
            {
                checklistItemId = checklistItemResult.checklistItemId
                matchId = checklistItemResult.matchId
                status = checklistItemResult.status
                completedBy = checklistItemResult.completedBy
                completedDate = checklistItemResult.completedDate
                isDraft = checklistItemResult.isDraft
                return true
            }
        }

        return false
    }

    /**
     * Saves the object into the database
     * @param database used for interacting with the SQLITE db
     * @return int id of the saved object
     */
    override fun save(database: Database): Int
    {
        var id = -1

        //try to open the DB if it is not open
        if (!database.isOpen)
            database.open()

        if (database.isOpen)
            id = database.setChecklistItemResult(this).toInt()

        //set the id if the save was successful
        if (id > 0)
            this.id = id

        return id
    }

    /**
     * Deletes the object from the database
     * @param database used for interacting with the SQLITE db
     * @return boolean if successful
     */
    override fun delete(database: Database): Boolean
    {
        var successful = false

        //try to open the DB if it is not open
        if (!database.isOpen) database.open()

        if (database.isOpen)
        {
            successful = database.deleteChecklistItemResult(this)
        }

        return successful
    }

    //endregion
}
