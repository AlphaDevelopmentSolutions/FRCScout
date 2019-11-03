package com.alphadevelopmentsolutions.frcscout.Interfaces

import com.alphadevelopmentsolutions.frcscout.Classes.TableColumn

interface ParentTableCompanion
{
    val COLUMN_NAME_LOCAL_ID: String
        get() = "LocalId"

    val COLUMN_NAME_SERVER_ID: String
        get() = "Id"

    val COLUMN_NAME_LAST_UPDATED: String
        get() = "LastUpdated"

    val COLUMN_NAME_IS_DRAFT: String
        get() = "IsDraft"

    val parentColumns: ArrayList<TableColumn>
        get() = ArrayList<TableColumn>().apply {
            add(TableColumn(COLUMN_NAME_LOCAL_ID, SQLiteDataTypes.INTEGER))
            add(TableColumn(COLUMN_NAME_SERVER_ID, SQLiteDataTypes.INTEGER))
        }
}