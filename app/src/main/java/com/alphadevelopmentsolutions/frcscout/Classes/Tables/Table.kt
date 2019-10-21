package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import com.alphadevelopmentsolutions.frcscout.Classes.Database
import java.util.*

abstract class Table protected constructor(
        private val TABLE_NAME: String,
        private val COLUMN_NAME_ID: String,
        private val CREATE_TABLE: String)
{

    abstract fun load(database: Database): Boolean
    abstract fun save(database: Database): Int
    abstract fun delete(database: Database): Boolean
    abstract override fun toString(): String

    companion object
    {
        val DEFAULT_STRING: String
            get()
            {
                return ""
            }

        val DEFAULT_INT: Int
            get()
            {
                return -1
            }

        val DEFAULT_LONG: Long
            get()
            {
                return -1
            }

        val DEFAULT_DOUBLE: Double
            get()
            {
                return -1.0
            }

        val DEFAULT_DATE: Date
            get()
            {
                return Date()
            }

        val DEFAULT_BOOLEAN: Boolean
            get()
            {
                return false
            }


        @JvmStatic
        protected fun clearTable(database: Database, tableName: String, clearDrafts: Boolean? = null)
        {
            database.clearTable(tableName, clearDrafts)
        }
    }
}
