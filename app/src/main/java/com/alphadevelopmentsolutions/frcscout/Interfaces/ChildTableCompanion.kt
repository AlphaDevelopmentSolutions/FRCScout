package com.alphadevelopmentsolutions.frcscout.Interfaces

import android.database.sqlite.SQLiteDatabase
import com.alphadevelopmentsolutions.frcscout.Classes.Database
import com.alphadevelopmentsolutions.frcscout.Classes.TableColumn
import java.lang.StringBuilder

interface ChildTableCompanion : ParentTableCompanion
{
    fun clearTable(database: Database, clearDrafts: Boolean? = null)
    {
        database.clearTable(TABLE_NAME, clearDrafts)
    }

    fun createTable(database: SQLiteDatabase)
    {
        database.execSQL("CREATE TABLE $TABLE_NAME " +
                "(" +
                with(columns)
                {
                    var columnsString = StringBuilder()

                    this.forEach {
                        columnsString.append("${it.columnName} ${it.columnType}${if(columnsString.isEmpty()) " PRIMARY KEY" else ""}${if(iterator().hasNext()) "," else ""}")
                    }

                    columnsString.toString()
                } +
                ")")
    }

    val TABLE_NAME: String

    val childColumns: ArrayList<TableColumn>

    val columns: ArrayList<TableColumn>
        get() = parentColumns.apply {
            addAll(childColumns)
        }

}