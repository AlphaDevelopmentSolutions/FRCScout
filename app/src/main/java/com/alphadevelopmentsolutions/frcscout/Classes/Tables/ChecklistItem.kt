package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import com.alphadevelopmentsolutions.frcscout.Classes.Database

import java.util.ArrayList

class ChecklistItem(
        var id: Int,
        var serverId: Int,
        var title: String,
        var description: String) : Table(TABLE_NAME, COLUMN_NAME_ID, CREATE_TABLE)
{

    companion object
    {

        val TABLE_NAME = "checklist_items"
        val COLUMN_NAME_ID = "LocalId"
        val COLUMN_NAME_SERVER_ID = "Id"
        val COLUMN_NAME_TITLE = "Title"
        val COLUMN_NAME_DESCRIPTION = "Description"

        val CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_SERVER_ID + " INTEGER," +
                COLUMN_NAME_TITLE + " TEXT," +
                COLUMN_NAME_DESCRIPTION + " TEXT)"

        /**
         * Clears all data from the classes table
         * @param database used to clear table
         */
        fun clearTable(database: Database)
        {
            database.clearTable(TABLE_NAME)
        }

        /**
         * Returns arraylist of checklist items with specified filters from database
         * @param checklistItem if specified, filters checklist items by checklistitem id
         * @param database used to load checklist items
         * @return arraylist of checklist items
         */
        fun getChecklistItems(checklistItem: ChecklistItem?, database: Database): ArrayList<ChecklistItem>?
        {
            return database.getChecklistItems(checklistItem)
        }
    }

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
        return ChecklistItemResult.getChecklistItemResults(this, checklistItemResult, onlyDrafts, database)
    }



    override fun toString(): String
    {
        return title
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
            val checklistItems = getChecklistItems(this, database)
            val checklistItem = if (checklistItems!!.size > 0) checklistItems[0] else null

            if (checklistItem != null)
            {
                serverId = checklistItem.serverId
                title = checklistItem.title
                description = checklistItem.description
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
            id = database.setChecklistItem(this).toInt()

        //set the id if the save was successful
        if (id > 0)
            id = id

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
            successful = database.deleteChecklistItem(this)
        }

        return successful
    }

    //endregion
}
