package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import com.alphadevelopmentsolutions.frcscout.Classes.Database
import com.alphadevelopmentsolutions.frcscout.Classes.MasterContentValues
import com.alphadevelopmentsolutions.frcscout.Classes.TableColumn
import com.alphadevelopmentsolutions.frcscout.Interfaces.ChildTableCompanion
import com.alphadevelopmentsolutions.frcscout.Interfaces.SQLiteDataTypes
import java.text.SimpleDateFormat
import java.util.*

class ChecklistItemResult(
        localId: Long = DEFAULT_LONG,
        serverId: Long = DEFAULT_LONG,
        var checklistItemId: Int = DEFAULT_INT,
        var matchId: String = DEFAULT_STRING,
        var status: String = DEFAULT_STRING,
        var completedBy: String? = null,
        var completedDate: Date? = null,
        var isDraft: Boolean) : Table(TABLE_NAME, localId, serverId)
{
    companion object: ChildTableCompanion
    {
        override val TABLE_NAME = "checklist_item_results"

        const val COLUMN_NAME_CHECKLIST_ITEM_ID = "ChecklistItemId"
        const val COLUMN_NAME_MATCH_ID = "MatchId"
        const val COLUMN_NAME_STATUS = "Status"
        const val COLUMN_NAME_COMPLETED_BY = "CompletedBy"
        const val COLUMN_NAME_COMPLETED_DATE = "CompletedDate"

        override val childColumns: ArrayList<TableColumn>
            get() = ArrayList<TableColumn>().apply {
                add(TableColumn(COLUMN_NAME_CHECKLIST_ITEM_ID, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_MATCH_ID, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_STATUS, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_COMPLETED_BY, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_COMPLETED_DATE, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_IS_DRAFT, SQLiteDataTypes.INTEGER))
            }

        /**
         * Returns [ArrayList] of [ChecklistItemResult] with specified filters from [database]
         * @param checklistItem if specified, filters [ChecklistItemResult] by [checklistItem] id
         * @param checklistItemResult if specified, filters [ChecklistItemResult] by [checklistItemResult] id
         * @param onlyDrafts if specified, filters [ChecklistItemResult] by [isDraft]
         * @param database used to load [ChecklistItemResult]
         * @return [ArrayList] of [ChecklistItemResult]
         */
        fun getObjects(checklistItem: ChecklistItem?, checklistItemResult: ChecklistItemResult?, onlyDrafts: Boolean, database: Database): ArrayList<ChecklistItemResult> {
            return ArrayList<ChecklistItemResult>().apply {

                val whereStatement = StringBuilder()
                val whereArgs = ArrayList<String>()

                //filter by object
                if (checklistItem != null)
                {
                    whereStatement.append("$COLUMN_NAME_CHECKLIST_ITEM_ID = ?")
                    whereArgs.add(checklistItem.serverId.toString())
                }

                //filter by object
                if (checklistItemResult != null)
                {
                    whereStatement.append(if (whereStatement.isNotEmpty()) " AND " else "").append("$COLUMN_NAME_LOCAL_ID = ?")
                    whereArgs.add(checklistItemResult.localId.toString())
                }

                //filter by object
                if (onlyDrafts)
                {
                    whereStatement.append(if (whereStatement.isNotEmpty()) " AND " else "").append("$COLUMN_NAME_IS_DRAFT = 1")
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
                                    ChecklistItemResult(
                                            getLong(COLUMN_NAME_LOCAL_ID),
                                            getLong(COLUMN_NAME_SERVER_ID),
                                            getInt(COLUMN_NAME_CHECKLIST_ITEM_ID),
                                            getString(COLUMN_NAME_MATCH_ID),
                                            getString(COLUMN_NAME_STATUS),
                                            getStringOrNull(COLUMN_NAME_COMPLETED_BY),
                                            getDateOrNull(COLUMN_NAME_COMPLETED_DATE),
                                            getBoolean(COLUMN_NAME_IS_DRAFT)
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
     * @see Table.load
     */
    override fun load(database: Database): Boolean
    {
        with(getObjects(null, this, false, database))
        {
            with(if (size > 0) this[0] else null)
            {
                if (this != null)
                {
                    loadParentValues(this)
                    this@ChecklistItemResult.checklistItemId = checklistItemId
                    this@ChecklistItemResult.matchId = matchId
                    this@ChecklistItemResult.status = status
                    this@ChecklistItemResult.completedBy = completedBy
                    this@ChecklistItemResult.completedDate = completedDate
                    this@ChecklistItemResult.isDraft = isDraft
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
            put(COLUMN_NAME_CHECKLIST_ITEM_ID, checklistItemId)
            put(COLUMN_NAME_MATCH_ID, matchId)
            put(COLUMN_NAME_STATUS, status)
            put(COLUMN_NAME_COMPLETED_BY, completedBy)
            put(COLUMN_NAME_COMPLETED_DATE, completedDate)
            put(COLUMN_NAME_IS_DRAFT, isDraft)
        }

    /**
     * @see Table.toString
     */
    override fun toString(): String
    {
        return ""
    }

}
