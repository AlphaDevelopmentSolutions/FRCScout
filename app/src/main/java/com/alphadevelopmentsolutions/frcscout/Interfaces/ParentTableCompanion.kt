package com.alphadevelopmentsolutions.frcscout.Interfaces

import android.database.sqlite.SQLiteDatabase
import com.alphadevelopmentsolutions.frcscout.Classes.Database
import com.alphadevelopmentsolutions.frcscout.Classes.TableColumn
import java.lang.StringBuilder

interface ParentTableCompanion
{
    val COLUMN_NAME_LOCAL_ID: String
        get() = "LocalId"

    val COLUMN_NAME_SERVER_ID: String
        get() = "ServerId"

    val COLUMN_NAME_IS_DRAFT: String
        get() = "IsDraft"

    val parentColumns: ArrayList<TableColumn>
        get() = ArrayList<TableColumn>().apply {
            add(TableColumn(COLUMN_NAME_LOCAL_ID, SQLiteDataTypes.NUMBER))
            add(TableColumn(COLUMN_NAME_SERVER_ID, SQLiteDataTypes.NUMBER))
            add(TableColumn(COLUMN_NAME_IS_DRAFT, SQLiteDataTypes.TEXT))
        }
}