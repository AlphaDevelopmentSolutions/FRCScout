package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import com.alphadevelopmentsolutions.frcscout.Classes.Database
import com.alphadevelopmentsolutions.frcscout.Classes.MasterContentValues
import com.alphadevelopmentsolutions.frcscout.Classes.TableColumn
import com.alphadevelopmentsolutions.frcscout.Interfaces.ChildTableCompanion
import com.alphadevelopmentsolutions.frcscout.Interfaces.SQLiteDataTypes
import java.util.*

class ChecklistItem(
        localId: Long = DEFAULT_LONG,
        serverId: Long = DEFAULT_LONG,
        var title: String = DEFAULT_STRING,
        var description: String) : Table(TABLE_NAME, localId, serverId)
{
    companion object: ChildTableCompanion
    {
        override val TABLE_NAME = "checklist_items"

        const val COLUMN_NAME_TITLE = "Title"
        const val COLUMN_NAME_DESCRIPTION = "Description"

        override val childColumns: ArrayList<TableColumn>
            get() = ArrayList<TableColumn>().apply {
                add(TableColumn(COLUMN_NAME_TITLE, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_DESCRIPTION, SQLiteDataTypes.TEXT))
            }

        /**
         * Returns [ArrayList] of [ChecklistItem] with specified filters from [database]
         * @param checklistItem if specified, filters [ChecklistItem] by [checklistItem] id
         * @param database used to load [ChecklistItem]
         * @return [ArrayList] of [ChecklistItem]
         */
        fun getObjects(checklistItem: ChecklistItem?, database: Database): ArrayList<ChecklistItem> {
            return ArrayList<ChecklistItem>().apply {

                val whereStatement = StringBuilder()
                val whereArgs = ArrayList<String>()

                //filter by object
                if (checklistItem != null)
                {
                    whereStatement.append("$COLUMN_NAME_LOCAL_ID = ?")
                    whereArgs.add(checklistItem.localId.toString())
                }

                //add all object records to array list
                with(database.getObjects(
                        TABLE_NAME,
                        whereStatement.toString(),
                        whereArgs))
                {
                    if (this != null) {
                        while (moveToNext()) {
                            add(
                                    ChecklistItem(
                                            getLong(COLUMN_NAME_LOCAL_ID),
                                            getLong(COLUMN_NAME_SERVER_ID),
                                            getString(COLUMN_NAME_TITLE),
                                            getString(COLUMN_NAME_DESCRIPTION)
                                    )
                                )
                        }

                        close()
                    }
                }
            }
        }
    }

    /**
     * Gets the checklist item results from the database, sorted from newest to oldest
     * @param checklistItemResult if specified, filters checklist item results by checklist item result id
     * @param database used to load object
     * @param onlyDrafts if true, filters by draft
     * @return [ArrayList] of [ChecklistItemResult]
     */
    fun getResults(checklistItemResult: ChecklistItemResult?, onlyDrafts: Boolean, database: Database): ArrayList<ChecklistItemResult>?
    {
        //get results from database
        return ChecklistItemResult.getObjects(this, checklistItemResult, onlyDrafts, database)
    }


    /**
     * @see Table.load
     */
    override fun load(database: Database): Boolean
    {
        with(getObjects(this, database))
        {
            with(if (size > 0) this[0] else null)
            {
                if (this != null)
                {
                    loadParentValues(this)
                    this@ChecklistItem.title = title
                    this@ChecklistItem.description = description
                    return true
                }
            }
        }

        return false
    }

    /**
     * @see Table.childValues
     */
    override val childValues: MasterContentValues
        get() = MasterContentValues().apply {
            put(COLUMN_NAME_TITLE, title)
            put(COLUMN_NAME_DESCRIPTION, description)
        }

    /**
     * @see Table.toString
     */
    override fun toString(): String
    {
        return title
    }
}
