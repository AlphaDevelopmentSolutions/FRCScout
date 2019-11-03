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

                    StringBuilder().apply {
                        for(i in 0 until size)
                        {
                            append("${Database.quote(columns[i].columnName)} ${columns[i].columnType}${if(isEmpty()) " PRIMARY KEY" else ""}${if(i + 1 < size) ", " else ""}")

                        }
                    }.toString()
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