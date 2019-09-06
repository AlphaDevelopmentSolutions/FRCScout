package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import com.alphadevelopmentsolutions.frcscout.Classes.Database

abstract class Table protected constructor(private val TABLE_NAME: String, private val COLUMN_NAME_ID: String, private val CREATE_TABLE: String)
{

    abstract fun load(database: Database): Boolean
    abstract fun save(database: Database): Int
    abstract fun delete(database: Database): Boolean
    abstract override fun toString(): String

    companion object
    {
        @JvmStatic
        protected fun clearTable(database: Database, tableName: String, clearDrafts: Boolean? = null)
        {
            database.clearTable(tableName, clearDrafts)
        }
    }
}
